package com.zhoubyte.contract_flow.domain.valobj.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContractStatus {
    
    DRAFT("DRAFT", "草稿"),
    PENDING("PENDING", "待审批"),
    DEPARTMENT_REVIEW("DEPARTMENT_REVIEW", "部门审核中"),
    LEGAL_REVIEW("LEGAL_REVIEW", "法务审核中"),
    RISK_CONTROL("RISK_CONTROL", "风控校验中"),
    HIGH_LEVEL_APPROVAL("HIGH_LEVEL_APPROVAL", "高层审批中"),
    SEAL_APPLICATION("SEAL_APPLICATION", "用印申请中"),
    EXTERNAL_SIGNING("EXTERNAL_SIGNING", "外部签署中"),
    ARCHIVED("ARCHIVED", "已归档"),
    COMPLETED("COMPLETED", "已完成"),
    REJECTED("REJECTED", "已拒绝"),
    CANCELED("CANCELED", "已取消");
    
    private final String value;
    private final String name;
    
    public static ContractStatus fromValue(String value) {
        for (ContractStatus status : ContractStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown ContractStatus value: " + value);
    }
}
