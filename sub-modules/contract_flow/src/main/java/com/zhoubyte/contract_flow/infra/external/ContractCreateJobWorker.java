package com.zhoubyte.contract_flow.infra.external;

import com.zhoubyte.contract_flow.application.dto.CreateJobParam;
import com.zhoubyte.contract_flow.application.service.ContractJobCreateService;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.response.UserTaskProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ContractCreateJobWorker {
    
    private final ContractJobCreateService contractJobCreateService;
    
    @JobWorker(type = "create_job_and_record_log")
    public void createJobAndRecordLog(ActivatedJob activatedJob) {
        log.info("ContractCreateJobWorker.createJobAndRecordLog job = {}", activatedJob);
        
        CreateJobParam.CreateJobParamBuilder builder = CreateJobParam.builder()
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
                .variablesAsMap(activatedJob.getVariablesAsMap());
        
        UserTaskProperties userTask = activatedJob.getUserTask();
        if (userTask != null) {
            List<String> candidateGroups = userTask.getCandidateGroups();
            if (candidateGroups != null && !candidateGroups.isEmpty()) {
                builder.candidateGroups(candidateGroups);
            } else {
                builder.candidateGroups(List.of("System"));
            }
            
            String assignee = userTask.getAssignee();
            if (assignee != null && !assignee.isEmpty()) {
                builder.assignee(assignee);
            } else {
                builder.assignee("System");
            }
        } else {
            builder.candidateGroups(List.of("System"));
            builder.assignee("System");
        }
        
        contractJobCreateService.createJobAndRecordLog(builder.build());
        
        log.info("Job created and recorded: taskKey = {}, elementId = {}", 
                activatedJob.getKey(), activatedJob.getElementId());
    }
}
