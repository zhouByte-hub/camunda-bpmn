package com.zhoubyte.contract_flow.domain.valobj.contract;

import lombok.Value;

@Value
public class ContractId {
    String value;
    
    public static ContractId form(String value) {
        return new ContractId(value);
    }
}
