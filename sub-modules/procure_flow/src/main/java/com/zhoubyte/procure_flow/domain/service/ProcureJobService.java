package com.zhoubyte.procure_flow.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhoubyte.procure_flow.application.config.ProcureTicketConstant;
import com.zhoubyte.procure_flow.application.dto.CreateJobParam;
import com.zhoubyte.procure_flow.domain.model.Task;
import com.zhoubyte.procure_flow.domain.repository.IProcureTaskRepository;
import com.zhoubyte.procure_flow.domain.utils.IdGenerator;
import com.zhoubyte.procure_flow.domain.valobj.task.TaskId;
import com.zhoubyte.procure_flow.domain.valobj.task.TaskStatus;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketId;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProcureJobService {

    private final IProcureTaskRepository procureTaskRepository;
    private final IdGenerator idGenerator;

    public ProcureJobService(IProcureTaskRepository procureTaskRepository, IdGenerator idGenerator) {
        this.procureTaskRepository = procureTaskRepository;
        this.idGenerator = idGenerator;
    }

    public Task persistenceTask(CreateJobParam createJobParam){
        String generateTaskId = idGenerator.generateTaskId(createJobParam.getTaskType());
        Map<String, Object> variables = createJobParam.getVariablesAsMap();
        if (variables == null) {
            throw new RuntimeException("variables is null");
        }
        String ticketId = (String) variables.get(ProcureTicketConstant.TICKET_ID);
        Task build = Task.builder()
                .id(TaskId.form(generateTaskId))
                .ticketId(TicketId.form(ticketId))
                .taskType(createJobParam.getTaskType())
                .processDefinitionVersion(createJobParam.getProcessDefinitionVersion())
                .processDefinitionKey(createJobParam.getProcessDefinitionKey())
                .processInstanceId(createJobParam.getProcessInstanceId())
                .processInstanceKey(createJobParam.getProcessInstanceKey())
                .elementId(createJobParam.getElementId())
                .elementInstanceKey(createJobParam.getElementInstanceKey())
                .taskStatus(TaskStatus.CREATE)
                .retries(createJobParam.getRetries())
                .variables(createJobParam.getVariables())
                .build();
        if(createJobParam.getCandidateGroups() != null && !createJobParam.getCandidateGroups().isEmpty()){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String candidateGroupsJson = objectMapper.writeValueAsString(createJobParam.getCandidateGroups());
                build.setCandidateGroups(candidateGroupsJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to convert candidate groups to JSON", e);
            }
        }
        return procureTaskRepository.saveOrUpdate(build);
    }

    public Boolean updateTask(Task task){
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task or TaskId cannot be null");
        }
        Task savedTask = procureTaskRepository.saveOrUpdate(task);
        return savedTask != null;
    }
}
