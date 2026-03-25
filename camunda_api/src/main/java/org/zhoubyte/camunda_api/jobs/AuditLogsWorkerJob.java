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
     * 必须手动完成 Job，否则 UserTask 会卡在 creating 状态无法分配给审批人
     */
    @JobWorker(type = "record_audit_logs", autoComplete = false)
    public void recordAuditLogs(final JobClient client, final ActivatedJob activatedJob) {
        log.info("[审计日志] elementId={}, processInstanceKey={} 任务正在执行，流程实例参数如下：{}",
                activatedJob.getElementId(),
                activatedJob.getProcessInstanceKey(),
                activatedJob.getVariablesAsMap());
        try {
            // TODO: 实际审计日志业务逻辑（如写入数据库）

            // 必须完成此 Job，否则 UserTask 将卡在 creating 状态
            client.newCompleteCommand(activatedJob.getKey())
                    .send()
                    .join();
            log.info("[审计日志] elementId={} Task Listener 执行完成，UserTask 可继续流转",
                    activatedJob.getElementId());
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
