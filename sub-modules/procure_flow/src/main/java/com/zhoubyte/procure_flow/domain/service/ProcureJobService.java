package com.zhoubyte.procure_flow.domain.service;

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

    public Task persistenceTask(CreateJobParam procureJobService){
        String generateTaskId = idGenerator.generateTaskId(procureJobService.getTaskType());
        Map<String, Object> variables = procureJobService.getVariablesAsMap();
        if (variables == null) {
            throw new RuntimeException("variables is null");
        }
        String ticketId = (String) variables.get(ProcureTicketConstant.TICKET_ID);
        Task build = Task.builder()
                .id(TaskId.form(generateTaskId))
                .ticketId(TicketId.form(ticketId))
                .taskType(procureJobService.getTaskType())
                .processDefinitionVersion(procureJobService.getProcessDefinitionVersion())
                .processDefinitionKey(procureJobService.getProcessDefinitionKey())
                .processInstanceId(procureJobService.getProcessInstanceId())
                .processInstanceKey(procureJobService.getProcessInstanceKey())
                .elementId(procureJobService.getElementId())
                .elementInstanceKey(procureJobService.getElementInstanceKey())
                .taskStatus(TaskStatus.CREATE)
                .retries(procureJobService.getRetries())
                .variables(procureJobService.getVariables())
                .build();

        return procureTaskRepository.saveOrUpdate(build);
    }
}
