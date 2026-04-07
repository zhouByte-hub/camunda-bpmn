package com.zhoubyte.contract_flow.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoubyte.contract_flow.domain.model.Contract;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractId;
import com.zhoubyte.contract_flow.facade.dto.request.ContractSearchRequest;

import java.util.Optional;

public interface IContractRepository {
    
    Contract saveOrUpdate(Contract contract);
    
    Page<Contract> queryContractList(ContractSearchRequest request);
    
    Optional<Contract> findByContractId(ContractId contractId);
}
