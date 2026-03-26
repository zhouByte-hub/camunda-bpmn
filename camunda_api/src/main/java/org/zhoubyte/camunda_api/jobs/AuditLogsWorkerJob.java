package org.zhoubyte.camunda_api.jobs;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuditLogsWorkerJob {

    /**
     * 处理审计日志 Task Listener
     * BPMN 中对应：project_leader_approval（项目组长审批）用户任务的 creating 事件监听器
     * type 必须与 BPMN 中 <zeebe:taskListener eventType="creating" type="record_audit_logs"> 完全一致
     *
     * 注意：此 Worker 处理的是 Task Listener（非普通 ServiceTask），
     * 必须手动完成 Job（newCompleteCommand），否则 UserTask 会卡在 CREATING 状态无法被审批人操作
     */
    @JobWorker(type = "record_audit_logs", autoComplete = false)
    public void recordAuditLogs(JobClient client, ActivatedJob activatedJob) {
        try {
            // 必须完成此 Job，UserTask 才能从 CREATING 转为 CREATED 状态
            client.newCompleteCommand(activatedJob.getKey())
                    .send()
                    .join();

            log.info("[审计日志] elementId={}, userTaskKey={}, processInstanceKey={} 任务正在执行，流程实例参数如下：{}",
                    activatedJob.getElementId(),
                    activatedJob.getUserTask().getUserTaskKey(),
                    activatedJob.getProcessInstanceKey(),
                    activatedJob.getVariablesAsMap());
        } catch (Exception e) {
            log.error("[审计日志] elementId={} 执行失败: {}", activatedJob.getElementId(), e.getMessage(), e);
            client.newFailCommand(activatedJob.getKey())
                    .retries(activatedJob.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }
}
