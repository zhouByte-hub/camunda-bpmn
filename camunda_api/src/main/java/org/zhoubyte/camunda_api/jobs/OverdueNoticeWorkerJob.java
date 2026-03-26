package org.zhoubyte.camunda_api.jobs;

import io.camunda.client.CamundaClient;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zhoubyte.camunda_api.process.ProcessConstant;

@Component
@Slf4j
public class OverdueNoticeWorkerJob {

    private final CamundaClient camundaClient;

    public OverdueNoticeWorkerJob(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }

    @JobWorker(type = "send-overdue-notice")
    public void sendOverdueNotice(ActivatedJob activatedJob) {
        String responsibility = (String) activatedJob.getVariable(ProcessConstant.RESPONSIBILITY);
        if(!StringUtils.isEmpty(responsibility)) {
            camundaClient.newPublishMessageCommand()
                    .messageName(ProcessConstant.TASK_REVOKE)
                    .correlationKey(responsibility)
                    .send()
                    .join();
        }
        log.info("Send overdue-notice job start, responsibility={}", responsibility);
    }


    @JobWorker(type = "send_overdue_message")
    public void sendOverdueMessage(ActivatedJob activatedJob) {
        log.info("Send overdue-message job start");
    }

}
