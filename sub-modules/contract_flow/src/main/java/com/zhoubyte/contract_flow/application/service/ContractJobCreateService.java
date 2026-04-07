package com.zhoubyte.contract_flow.application.service;

import com.zhoubyte.contract_flow.application.dto.CreateJobParam;
import com.zhoubyte.contract_flow.domain.model.Task;
import com.zhoubyte.contract_flow.domain.service.ContractJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractJobCreateService {
    
    private final ContractJobService contractJobService;
    
    public void createJobAndRecordLog(CreateJobParam createJobParam) {
        if (createJobParam == null) {
            throw new IllegalArgumentException("CreateJobParam cannot be null");
        }
        
        Task task = contractJobService.persistenceTask(createJobParam);
        task.toProcess();
        
        if (!contractJobService.updateTask(task)) {
            throw new RuntimeException("任务持久化失败");
        }
        
        log.info("Task created successfully: taskId = {}, taskKey = {}, elementId = {}", 
                task.getId().getValue(), task.getTaskKey(), task.getElementId());
    }
}
