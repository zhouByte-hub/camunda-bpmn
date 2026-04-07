package com.zhoubyte.contract_flow.facade.endpoint;

import com.zhoubyte.contract_flow.application.service.ContractTaskCompleteService;
import com.zhoubyte.contract_flow.facade.dto.request.AuditRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
@Slf4j
@RequiredArgsConstructor
public class ContractFlowTaskController {
    
    private final ContractTaskCompleteService contractTaskCompleteService;
    
    @PostMapping("/{task_key}:complete")
    public ResponseEntity<String> completeUserTask(
            @PathVariable("task_key") Long taskKey,
            @RequestBody @Validated AuditRequest auditRequest) {
        
        log.info("Completing user task: taskKey = {}, auditResult = {}, operatorId = {}", 
                taskKey, auditRequest.getAuditResult(), auditRequest.getOperatorId());
        
        contractTaskCompleteService.completeUserTask(
                taskKey,
                auditRequest.getAuditResult(),
                auditRequest.getMsg(),
                auditRequest.getOperatorId(),
                auditRequest.getOperatorName()
        );
        
        log.info("User task completed successfully: taskKey = {}", taskKey);
        
        return ResponseEntity.ok("Task completed successfully");
    }
}
