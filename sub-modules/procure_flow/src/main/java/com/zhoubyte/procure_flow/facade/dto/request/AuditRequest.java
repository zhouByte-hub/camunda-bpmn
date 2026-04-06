package com.zhoubyte.procure_flow.facade.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuditRequest {

    @NotNull(message = "audit_result 不能为空")
    private Integer auditResult;
    private String msg;

}
