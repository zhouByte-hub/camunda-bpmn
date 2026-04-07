package com.zhoubyte.contract_flow.facade.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class ContractCreateRequest {
    
    @NotBlank(message = "合同名称不能为空")
    private String contractName;
    
    @NotBlank(message = "合同编号不能为空")
    private String contractNumber;
    
    @NotBlank(message = "合同类型不能为空")
    private String contractType;
    
    @NotNull(message = "合同金额不能为空")
    @Positive(message = "合同金额必须大于 0")
    private Long contractAmount;
    
    private String crmCustomerId;
    private String contractDocumentId;
    private String contractRemark;
    
    @NotNull(message = "预计完成时间不能为空")
    private Long expectedCompletionTime;
    
    private Long expiryTime;
    private Boolean isNotify;
    private Integer preOverdueDays;
    private Integer postOverdueDays;
}
