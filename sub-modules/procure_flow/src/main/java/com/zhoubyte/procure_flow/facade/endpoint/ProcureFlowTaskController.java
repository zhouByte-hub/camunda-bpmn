package com.zhoubyte.procure_flow.facade.endpoint;

import com.zhoubyte.procure_flow.application.service.ProcureTaskCompleteService;
import com.zhoubyte.procure_flow.facade.dto.request.AuditRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class ProcureFlowTaskController {

    private final ProcureTaskCompleteService procureTaskCompleteService;

    @PostMapping(value = "/{task_key}:complete")
    public ResponseEntity<String> completeUserTask(
            @PathVariable("task_key") Long taskKey,
            @RequestBody @Validated AuditRequest auditRequest) {
        
        procureTaskCompleteService.completeUserTask(
            taskKey, 
            auditRequest.getAuditResult(), 
            auditRequest.getMsg(),
            auditRequest.getOperatorId(),
            auditRequest.getOperatorName()
        );
        
        return ResponseEntity.ok("Task completed successfully");
    }
}
