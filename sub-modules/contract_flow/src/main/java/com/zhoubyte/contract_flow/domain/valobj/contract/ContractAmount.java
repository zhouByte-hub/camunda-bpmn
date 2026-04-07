package com.zhoubyte.contract_flow.domain.valobj.contract;

import lombok.Value;

@Value
public class ContractAmount {
    Long value;
    
    public ContractAmount(Long value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Contract amount must be non-negative");
        }
        this.value = value;
    }
    
    public boolean isHighLevelApprovalRequired() {
        return value > 100_000_000L;
    }
    
    public boolean isHighRisk() {
        return value > 500_000_000L;
    }
}
