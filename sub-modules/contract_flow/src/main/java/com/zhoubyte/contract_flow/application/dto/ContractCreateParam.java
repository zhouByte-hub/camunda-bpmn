package com.zhoubyte.contract_flow.application.dto;

import lombok.Data;

@Data
public class ContractCreateParam {
    
    private String contractName;
    private String contractNumber;
    private String contractType;
    private Long contractAmount;
    private String crmCustomerId;
    private String contractDocumentId;
    private String contractRemark;
    private Long expectedCompletionTime;
    private Long expiryTime;
    private Boolean isNotify;
    private Integer preOverdueDays;
    private Integer postOverdueDays;
    private String creatorName;
    private String creatorId;
}
