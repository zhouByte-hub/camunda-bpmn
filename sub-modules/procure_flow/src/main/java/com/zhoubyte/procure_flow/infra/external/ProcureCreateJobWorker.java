package com.zhoubyte.procure_flow.infra.external;

import com.zhoubyte.procure_flow.application.dto.CreateJobParam;
import com.zhoubyte.procure_flow.application.service.ProcureJobCreateService;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProcureCreateJobWorker {

    private final ProcureJobCreateService procureJobCreateService;

    public ProcureCreateJobWorker(ProcureJobCreateService procureJobCreateService) {
        this.procureJobCreateService = procureJobCreateService;
    }

    @JobWorker(type = "create_job_and_record_log")
    public void createJobAndRecordLog(ActivatedJob activatedJob){
        log.info("createJobWorker.createJobAndRecordLog job = {}", activatedJob);
        CreateJobParam build = CreateJobParam.builder()
                .taskType(activatedJob.getType())
                .processDefinitionVersion(activatedJob.getProcessDefinitionVersion())
                .processDefinitionKey(activatedJob.getProcessDefinitionKey())
                .processInstanceId(activatedJob.getBpmnProcessId())
                .processInstanceKey(activatedJob.getProcessInstanceKey())
                .elementId(activatedJob.getElementId())
                .elementInstanceKey(activatedJob.getElementInstanceKey())
                .retries(activatedJob.getRetries())
                .variables(activatedJob.getVariables())
                .variablesAsMap(activatedJob.getVariablesAsMap())
                .build();
        procureJobCreateService.createJobAndRecordLog(build);
    }
}
