package com.zhoubyte.contract_flow.domain.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGenerator {
    
    public String generateContractId() {
        return "CONTRACT_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    public String generateTaskId() {
        return "TASK_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
