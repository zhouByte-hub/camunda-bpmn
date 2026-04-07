package com.zhoubyte.contract_flow.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhoubyte.contract_flow.application.config.ContractConstant;
import com.zhoubyte.contract_flow.application.dto.CreateJobParam;
import com.zhoubyte.contract_flow.domain.model.Task;
import com.zhoubyte.contract_flow.domain.repository.IContractTaskRepository;
import com.zhoubyte.contract_flow.domain.utils.IdGenerator;
import com.zhoubyte.contract_flow.domain.valobj.task.TaskId;
import com.zhoubyte.contract_flow.domain.valobj.task.TaskStatus;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractId;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ContractJobService {
    
    private final IContractTaskRepository contractTaskRepository;
    private final IdGenerator idGenerator;
    private final ObjectMapper objectMapper;
    
    public ContractJobService(IContractTaskRepository contractTaskRepository, 
                              IdGenerator idGenerator,
                              ObjectMapper objectMapper) {
        this.contractTaskRepository = contractTaskRepository;
        this.idGenerator = idGenerator;
        this.objectMapper = objectMapper;
    }
    
    public Task persistenceTask(CreateJobParam createJobParam) {
        if (createJobParam == null) {
            throw new IllegalArgumentException("CreateJobParam cannot be null");
        }
        
        String generateTaskId = idGenerator.generateTaskId();
        Map<String, Object> variables = createJobParam.getVariablesAsMap();
        
        if (variables == null) {
            throw new RuntimeException("Variables cannot be null");
        }
        
        String contractId = (String) variables.get(ContractConstant.CONTRACT_ID);
        if (contractId == null || contractId.isEmpty()) {
            throw new RuntimeException("Contract ID not found in variables");
        }
        
        Task task = Task.builder()
                .id(TaskId.form(generateTaskId))
                .taskKey(createJobParam.getTaskKey())
                .contractId(ContractId.form(contractId))
                .taskType(createJobParam.getTaskType())
                .processDefinitionVersion(createJobParam.getProcessDefinitionVersion())
                .processDefinitionKey(createJobParam.getProcessDefinitionKey())
                .processInstanceId(createJobParam.getProcessInstanceId())
                .processInstanceKey(createJobParam.getProcessInstanceKey())
                .elementId(createJobParam.getElementId())
                .elementInstanceKey(createJobParam.getElementInstanceKey())
                .taskStatus(TaskStatus.CREATE)
                .variables(createJobParam.getVariables())
                .assignee(createJobParam.getAssignee())
                .build();
        
        if (createJobParam.getCandidateGroups() != null && !createJobParam.getCandidateGroups().isEmpty()) {
            try {
                String candidateGroupsJson = objectMapper.writeValueAsString(createJobParam.getCandidateGroups());
                task.setCandidateGroups(candidateGroupsJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to convert candidate groups to JSON", e);
            }
        }
        
        return contractTaskRepository.saveOrUpdate(task);
    }
    
    public Boolean updateTask(Task task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task or TaskId cannot be null");
        }
        
        Task savedTask = contractTaskRepository.saveOrUpdate(task);
        return savedTask != null;
    }
    
    public Optional<Task> findByTaskKey(Long taskKey) {
        if (taskKey == null) {
            throw new IllegalArgumentException("TaskKey cannot be null");
        }
        
        return contractTaskRepository.findByTaskKey(taskKey);
    }
}
