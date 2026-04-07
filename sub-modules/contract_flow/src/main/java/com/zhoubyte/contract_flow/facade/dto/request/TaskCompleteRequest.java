package com.zhoubyte.contract_flow.facade.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class TaskCompleteRequest {
    
    @NotBlank(message = "任务 ID 不能为空")
    private String taskId;
    
    @NotBlank(message = "处理人 ID 不能为空")
    private String assigneeId;
    
    @NotBlank(message = "处理人名称不能为空")
    private String assigneeName;
    
    private Boolean approved;
    private String remark;
}
