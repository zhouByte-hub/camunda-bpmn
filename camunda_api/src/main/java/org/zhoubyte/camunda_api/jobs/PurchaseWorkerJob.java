package org.zhoubyte.camunda_api.jobs;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PurchaseWorkerJob {

    /**
     * 处理拨款购买 Service Task
     * BPMN 中对应：purchase_of_funds（拨款购买）
     * type 必须与 BPMN 中 <zeebe:taskDefinition type="to_do_purchase_service"> 完全一致
     */
    @JobWorker(type = "to_do_purchase_service", autoComplete = false)
    public void purchaseServerWorkerJob(final JobClient client, final ActivatedJob activatedJob) {
        log.info("[拨款购买] elementId={}, processInstanceKey={} 开始执行",
                activatedJob.getElementId(), activatedJob.getProcessInstanceKey());
        try {
            // TODO: 实际拨款购买业务逻辑

            // 业务处理完成，手动完成 Job
            client.newCompleteCommand(activatedJob.getKey())
                    .send()
                    .join();
            log.info("[拨款购买] elementId={} 执行完成", activatedJob.getElementId());
        } catch (Exception e) {
            log.error("[拨款购买] elementId={} 执行失败: {}", activatedJob.getElementId(), e.getMessage(), e);
            // 失败时上报错误，触发重试或事件子流程
            client.newFailCommand(activatedJob.getKey())
                    .retries(activatedJob.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }
}
