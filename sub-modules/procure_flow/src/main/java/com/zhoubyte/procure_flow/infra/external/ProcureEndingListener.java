package com.zhoubyte.procure_flow.infra.external;

import com.zhoubyte.procure_flow.application.service.ProcureProcessEndService;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProcureEndingListener {

    private final ProcureProcessEndService procureProcessEndService;

    public ProcureEndingListener(ProcureProcessEndService procureProcessEndService) {
        this.procureProcessEndService = procureProcessEndService;
    }

    @JobWorker(type = "end_procure_process")
    public void endProcureProcess(ActivatedJob activatedJob){
        log.info("endProcureProcess job = {}", activatedJob);

    }
}
