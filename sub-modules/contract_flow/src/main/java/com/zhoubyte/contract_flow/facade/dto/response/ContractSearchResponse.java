package com.zhoubyte.contract_flow.facade.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ContractSearchResponse {
    
    private List<?> contractList;
    private Long total;
    private Integer currentPage;
    
    public static ContractSearchResponse success(List<?> contractList, Long total, Integer currentPage) {
        ContractSearchResponse response = new ContractSearchResponse();
        response.setContractList(contractList);
        response.setTotal(total);
        response.setCurrentPage(currentPage);
        return response;
    }
    
    public static ContractSearchResponse fail(String message) {
        ContractSearchResponse response = new ContractSearchResponse();
        return response;
    }
}
