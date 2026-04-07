package com.zhoubyte.contract_flow.application.service;

import com.zhoubyte.contract_flow.application.config.ContractConstant;
import com.zhoubyte.contract_flow.application.dto.ContractCreateParam;
import com.zhoubyte.contract_flow.domain.model.Contract;
import com.zhoubyte.contract_flow.domain.service.ContractService;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractId;
import com.zhoubyte.contract_flow.facade.dto.request.ContractSearchRequest;
import com.zhoubyte.contract_flow.facade.dto.response.ContractSearchResponse;
import com.zhoubyte.contract_flow.domain.repository.IContractRepository;
import com.zhoubyte.scorpio.dto.StartProcessInstanceResult;
import com.zhoubyte.scorpio.wrapper.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractCreateService {
    
    private final ContractService contractService;
    private final ProcessService processService;
    private final IContractRepository contractRepository;
    
    public ContractId createContract(ContractCreateParam param) {
        validateContractCreateParam(param);
        
        Contract contract = contractService.createContract(param);
        log.info("ContractService.createContract contract = {}", contract);
        
        Map<String, Object> variables = new HashMap<>();
        variables.put(ContractConstant.CONTRACT_ID, contract.getContractId().getValue());
        variables.put(ContractConstant.CONTRACT_NAME, contract.getContractName());
        variables.put(ContractConstant.CONTRACT_NUMBER, contract.getContractNumber());
        variables.put(ContractConstant.CONTRACT_TYPE, contract.getContractType().getValue());
        variables.put(ContractConstant.CONTRACT_AMOUNT, contract.getContractAmount().getValue());
        variables.put(ContractConstant.CONTRACT_STATUS, contract.getContractStatus().getValue());
        variables.put(ContractConstant.CRM_CUSTOMER_ID, contract.getCrmCustomerId());
        variables.put(ContractConstant.CONTRACT_DOCUMENT_ID, contract.getContractDocumentId());
        variables.put(ContractConstant.EXPECTED_COMPLETION_TIME, contract.getExpectedCompletionTime());
        variables.put(ContractConstant.EXPIRY_TIME, contract.getExpiryTime());
        variables.put(ContractConstant.IS_NOTIFY, contract.getIsNotify());
        variables.put(ContractConstant.PRE_OVERDUE_DAYS, contract.getPreOverdueDays());
        variables.put(ContractConstant.POST_OVERDUE_DAYS, contract.getPostOverdueDays());
        variables.put(ContractConstant.CREATOR_NAME, contract.getCreatorName());
        variables.put(ContractConstant.CREATOR_ID, contract.getCreatorId());
        
        String overdueDefinition = calculateOverdueDefinition(
                contract.getIsNotify(), 
                contract.getPreOverdueDays(), 
                contract.getPostOverdueDays()
        );
        variables.put(ContractConstant.OVERDUE_DEFINITION, overdueDefinition);
        
        log.info("ContractService.createContract variables = {}", variables);
        
        StartProcessInstanceResult result = processService.startProcessInstance(
                ContractConstant.PROCESS_INSTANCE_ID, null, null, variables);
        
        if (result == null) {
            throw new RuntimeException("流程实例启动失败");
        }
        
        return contract.getContractId();
    }
    
    private void validateContractCreateParam(ContractCreateParam param) {
        if (param.getContractAmount() == null || param.getContractAmount() <= 0) {
            throw new IllegalArgumentException("合同金额必须大于 0");
        }
        
        if (Boolean.TRUE.equals(param.getIsNotify())) {
            if (param.getPreOverdueDays() == null) {
                throw new IllegalArgumentException("当 isNotify 为 true 时，preOverdueDays 不能为空");
            }
            if (param.getPostOverdueDays() == null) {
                throw new IllegalArgumentException("当 isNotify 为 true 时，postOverdueDays 不能为空");
            }
            if (param.getPreOverdueDays() < 0) {
                throw new IllegalArgumentException("preOverdueDays 不能为负数");
            }
            if (param.getPostOverdueDays() < 0) {
                throw new IllegalArgumentException("postOverdueDays 不能为负数");
            }
        }
    }
    
    private String calculateOverdueDefinition(Boolean isNotify, Integer preOverdueDays, Integer postOverdueDays) {
        if (Boolean.FALSE.equals(isNotify)) {
            return null;
        }
        
        if (preOverdueDays == null || postOverdueDays == null) {
            return null;
        }
        
        int totalDays = preOverdueDays + postOverdueDays;
        if (totalDays == 0) {
            return "PT1H";
        }
        
        return String.format("P%dD", totalDays);
    }
    
    public ContractSearchResponse contractList(ContractSearchRequest request) {
        var contractPage = contractRepository.queryContractList(request);
        ContractSearchResponse response = new ContractSearchResponse();
        response.setContractList(contractPage.getRecords());
        response.setTotal(contractPage.getTotal());
        response.setCurrentPage(request.getOffset());
        return response;
    }
}
