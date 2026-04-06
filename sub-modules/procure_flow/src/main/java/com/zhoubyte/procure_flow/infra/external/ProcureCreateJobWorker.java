package com.zhoubyte.procure_flow.infra.external;

import com.zhoubyte.procure_flow.application.dto.CreateJobParam;
import com.zhoubyte.procure_flow.application.service.ProcureJobCreateService;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.response.UserTaskProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

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
                .taskKey(activatedJob.getKey())
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
        UserTaskProperties userTask = activatedJob.getUserTask();
        if(userTask != null){
            List<String> candidateGroups = userTask.getCandidateGroups();
            if(candidateGroups != null && !candidateGroups.isEmpty()){
                build.setCandidateGroups(candidateGroups);
            }else{
                build.setCandidateGroups(List.of("System"));
            }
        }
        procureJobCreateService.createJobAndRecordLog(build);
    }
}
