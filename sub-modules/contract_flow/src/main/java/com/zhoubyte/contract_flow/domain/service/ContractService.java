package com.zhoubyte.contract_flow.domain.service;

import com.zhoubyte.contract_flow.application.dto.ContractCreateParam;
import com.zhoubyte.contract_flow.domain.model.Contract;
import com.zhoubyte.contract_flow.domain.repository.IContractRepository;
import com.zhoubyte.contract_flow.domain.utils.IdGenerator;
import com.zhoubyte.contract_flow.domain.valobj.contract.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ContractService {
    
    private final IContractRepository contractRepository;
    private final IdGenerator idGenerator;
    
    public ContractService(IContractRepository contractRepository, IdGenerator idGenerator) {
        this.contractRepository = contractRepository;
        this.idGenerator = idGenerator;
    }
    
    public Contract createContract(ContractCreateParam param) {
        String contractId = idGenerator.generateContractId();
        
        Contract contract = Contract.builder()
                .contractId(ContractId.form(contractId))
                .contractName(param.getContractName())
                .contractNumber(param.getContractNumber())
                .contractType(ContractType.fromValue(param.getContractType()))
                .contractAmount(new ContractAmount(param.getContractAmount()))
                .contractStatus(ContractStatus.DRAFT)
                .crmCustomerId(param.getCrmCustomerId())
                .contractDocumentId(param.getContractDocumentId())
                .contractRemark(param.getContractRemark())
                .expectedCompletionTime(param.getExpectedCompletionTime())
                .expiryTime(param.getExpiryTime())
                .isNotify(param.getIsNotify())
                .preOverdueDays(param.getPreOverdueDays())
                .postOverdueDays(param.getPostOverdueDays())
                .creatorName(param.getCreatorName())
                .creatorId(param.getCreatorId())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        
        return contractRepository.saveOrUpdate(contract);
    }
    
    public void updateContractStatus(ContractId contractId, String operatorId, 
                                     String operatorName, ContractStatus newStatus) {
        Contract contract = contractRepository.findByContractId(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        
        contract.setContractStatus(newStatus);
        contract.setUpdateId(operatorId);
        contract.setUpdateName(operatorName);
        contract.setUpdateTime(LocalDateTime.now());
        
        contractRepository.saveOrUpdate(contract);
    }
}
