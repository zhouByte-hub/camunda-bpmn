package com.zhoubyte.contract_flow.domain.model;

import com.zhoubyte.contract_flow.domain.valobj.contract.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Contract {
    
    private ContractId contractId;
    private String contractName;
    private String contractNumber;
    private ContractType contractType;
    private ContractAmount contractAmount;
    private ContractStatus contractStatus;
    
    private String crmCustomerId;
    private String contractDocumentId;
    private String signedDocumentId;
    private String contractRemark;
    
    private Long expectedCompletionTime;
    private Long expiryTime;
    
    private Boolean isNotify;
    private Integer preOverdueDays;
    private Integer postOverdueDays;
    
    private String creatorName;
    private String creatorId;
    private String updateId;
    private String updateName;
    
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private String currentAssignee;
    
    public void submitForApproval(String operatorId, String operatorName) {
        if (!ContractStatus.DRAFT.equals(contractStatus)) {
            throw new RuntimeException("只有草稿状态的合同才能提交审批");
        }
        this.contractStatus = ContractStatus.PENDING;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    public void departmentApprove(String operatorId, String operatorName) {
        if (!ContractStatus.PENDING.equals(contractStatus)) {
            throw new RuntimeException("当前状态不允许部门审核");
        }
        this.contractStatus = ContractStatus.DEPARTMENT_REVIEW;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    public void legalApprove(String operatorId, String operatorName) {
        if (!ContractStatus.DEPARTMENT_REVIEW.equals(contractStatus)) {
            throw new RuntimeException("当前状态不允许法务审核");
        }
        this.contractStatus = ContractStatus.LEGAL_REVIEW;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    public void riskControlApprove(String operatorId, String operatorName) {
        if (!ContractStatus.LEGAL_REVIEW.equals(contractStatus)) {
            throw new RuntimeException("当前状态不允许风控校验");
        }
        this.contractStatus = ContractStatus.RISK_CONTROL;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    public void highLevelApprove(String operatorId, String operatorName) {
        if (!ContractStatus.RISK_CONTROL.equals(contractStatus)) {
            throw new RuntimeException("当前状态不允许高层审批");
        }
        this.contractStatus = ContractStatus.HIGH_LEVEL_APPROVAL;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    public void completeSigning(String operatorId, String operatorName) {
        this.contractStatus = ContractStatus.COMPLETED;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    public void reject(String operatorId, String operatorName, String reason) {
        this.contractStatus = ContractStatus.REJECTED;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.contractRemark = reason;
        this.updateTime = LocalDateTime.now();
    }
    
    public void cancel(String operatorId, String operatorName) {
        this.contractStatus = ContractStatus.CANCELED;
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.updateTime = LocalDateTime.now();
    }
    
    public boolean needHighLevelApproval() {
        return contractAmount != null && contractAmount.isHighLevelApprovalRequired();
    }
    
    public boolean isHighRisk() {
        return contractAmount != null && contractAmount.isHighRisk();
    }
}
