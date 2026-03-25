package org.zhoubyte.camunda_api.jobs;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendMessageWorkerJob {

    /**
     * 处理发送邮件 Send Task
     * BPMN 中对应：send_message（发送邮件）
     * type 必须与 BPMN 中 <zeebe:taskDefinition type="send_message"> 完全一致
     */
    @JobWorker(type = "send_message", autoComplete = false)
    public void sendMessage(final JobClient client, final ActivatedJob activatedJob) {
        log.info("[发送邮件] elementId={}, processInstanceKey={} 开始执行",
                activatedJob.getElementId(), activatedJob.getProcessInstanceKey());
        try {
            // TODO: 实际发送邮件业务逻辑（如调用邮件服务）

            // 业务处理完成，手动完成 Job
            client.newCompleteCommand(activatedJob.getKey())
                    .send()
                    .join();
            log.info("[发送邮件] elementId={} 执行完成", activatedJob.getElementId());
        } catch (Exception e) {
            log.error("[发送邮件] elementId={} 执行失败: {}", activatedJob.getElementId(), e.getMessage(), e);
            client.newFailCommand(activatedJob.getKey())
                    .retries(activatedJob.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }
}
