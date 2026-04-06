package com.zhoubyte.procure_flow.facade.endpoint;

import com.zhoubyte.procure_flow.application.service.ProcureJobCreateService;
import com.zhoubyte.procure_flow.facade.dto.request.AuditRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
public class ProcureFlowTaskController {

    public final ProcureJobCreateService procureJobCreateService;

    public ProcureFlowTaskController(ProcureJobCreateService procureJobCreateService) {
        this.procureJobCreateService = procureJobCreateService;
    }

    @PostMapping(value = "/{task_key}:complete")
    public void completeUserTask(@PathVariable("task_key") String taskKey, @RequestBody @Validated AuditRequest auditRequest) {

    }
}
