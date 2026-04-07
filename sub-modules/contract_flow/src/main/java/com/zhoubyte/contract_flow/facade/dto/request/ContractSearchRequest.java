package com.zhoubyte.contract_flow.facade.dto.request;

import lombok.Data;

@Data
public class ContractSearchRequest {
    
    private String contractStatus;
    private String contractType;
    private String contractName;
    
    private Integer offset = 1;
    private Integer limit = 10;
}
