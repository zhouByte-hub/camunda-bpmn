package org.zhoubyte.camunda_api.jobs;

import io.camunda.client.CamundaClient;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zhoubyte.camunda_api.process.ProcessConstant;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class AuditLogsWorkerJob {

    private final CamundaClient camundaClient;

    public AuditLogsWorkerJob(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }

    /**
     * 处理审计日志 Task Listener
     * BPMN 中对应：project_leader_approval（项目组长审批）用户任务的 creating 事件监听器
     * type 必须与 BPMN 中 <zeebe:taskListener eventType="creating" type="record_audit_logs"> 完全一致
     *
     * 注意：此 Worker 处理的是 Task Listener（非普通 ServiceTask），
     * 必须手动完成 Job（newCompleteCommand），否则 UserTask 会卡在 CREATING 状态无法被审批人操作
     */
    @JobWorker(type = "record_audit_logs", autoComplete = false)
    public void recordAuditLogs(JobClient jobClient, ActivatedJob activatedJob) {
        String responsibility = this.getResponsibility(activatedJob.getElementId());
        try {
            if (!StringUtils.isEmpty(responsibility)) {
                // Task Listener 不支持完成时携带 variables（issue #23702），改为单独设置流程变量
                Map<String, Object> variables = new HashMap<>();
                variables.put(ProcessConstant.RESPONSIBILITY, responsibility);

                /*
                 *  send() 返回 CompletableFuture，
                 * .thenAccept() 在 setVariables 完成后异步回调执行 complete，不占用 job worker 线程。
                 *  这样 Task Listener Job 可以正常完成，project_leader_approval 从 CREATING → CREATED，newCreateInstanceCommand().join() 才能返回，HTTP 请求不再卡住。
                 */
                camundaClient.newSetVariablesCommand(activatedJob.getProcessInstanceKey())
                        .variables(variables)
                        .send()
                        .thenAccept(response -> jobClient.newCompleteCommand(activatedJob.getKey()).send().join());
            }else{
                // 必须完成此 Job，UserTask 才能从 CREATING 转为 CREATED 状态
                jobClient.newCompleteCommand(activatedJob.getKey())
                        .send()
                        .join();
            }
            log.info("[审计日志] responsibility = {}, elementId={},elementInstanceKey={}, userTaskKey={}, processInstanceKey={} 任务正在执行，流程实例参数如下：{}",
                    responsibility,
                    activatedJob.getElementId(),
                    activatedJob.getElementInstanceKey(),
                    activatedJob.getUserTask().getUserTaskKey(),
                    activatedJob.getProcessInstanceKey(),
                    activatedJob.getVariablesAsMap());
        } catch (Exception e) {
            log.error("[审计日志] elementId={} 执行失败: {}", activatedJob.getElementId(), e.getMessage(), e);
            jobClient.newFailCommand(activatedJob.getKey())
                    .retries(activatedJob.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }


    private String getResponsibility(String elementId) {
        return switch (elementId) {
            case ProcessConstant.PROJECT_LEADER_APPROVAL -> "项目组长";
            case ProcessConstant.DEPARTMENT_MANAGER_APPROVAL -> "部门经理";
            case ProcessConstant.FINANCIAL_APPROVAL -> "财务";
            case ProcessConstant.CEO_APPROVAL -> "CEO";
            default -> "未知";
        };
    }
}
