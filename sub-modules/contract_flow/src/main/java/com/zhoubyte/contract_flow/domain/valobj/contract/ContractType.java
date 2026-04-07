package com.zhoubyte.contract_flow.domain.valobj.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContractType {
    
    SALES("SALES", "销售合同"),
    PURCHASE("PURCHASE", "采购合同"),
    SERVICE("SERVICE", "服务合同"),
    LEASE("LEASE", "租赁合同"),
    PARTNERSHIP("PARTNERSHIP", "合作协议"),
    NDA("NDA", "保密协议"),
    OTHER("OTHER", "其他");
    
    private final String value;
    private final String name;
    
    public static ContractType fromValue(String value) {
        for (ContractType type : ContractType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ContractType value: " + value);
    }
}
