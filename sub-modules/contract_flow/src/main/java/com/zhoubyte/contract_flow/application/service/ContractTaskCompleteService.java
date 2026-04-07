package com.zhoubyte.contract_flow.application.service;

import com.zhoubyte.contract_flow.domain.model.Task;
import com.zhoubyte.contract_flow.domain.service.ContractJobService;
import com.zhoubyte.contract_flow.domain.service.ContractService;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractId;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractStatus;
import com.zhoubyte.scorpio.wrapper.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractTaskCompleteService {
    
    private final ProcessService processService;
    private final ContractJobService contractJobService;
    private final ContractService contractService;
    
    @Transactional
    public void completeUserTask(Long taskKey, Integer auditResult, String msg, 
                                  String operatorId, String operatorName) {
        log.info("completeUserTask taskKey = {}, auditResult = {}, msg = {}, operatorId = {}, operatorName = {}", 
                taskKey, auditResult, msg, operatorId, operatorName);
        
        Task task = contractJobService.findByTaskKey(taskKey)
                .orElseThrow(() -> new RuntimeException("Task not found with taskKey: " + taskKey));
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("audit_result", auditResult);
        variables.put("audit_msg", msg);
        
        boolean approved = auditResult != null && auditResult == 1;
        variables.put("approved", approved);
        
        processService.completeUserTask(taskKey, variables);
        
        task.toComplete();
        task.setAssignee(operatorId);
        contractJobService.updateTask(task);
        
        updateContractStatusByElementId(task.getElementId(), task.getContractId().getValue(), 
                approved, operatorId, operatorName, msg);
        
        log.info("UserTask 已完成: taskKey = {}, approved = {}", taskKey, approved);
    }
    
    private void updateContractStatusByElementId(String elementId, String contractId, 
            boolean approved, String operatorId, String operatorName, String msg) {
        
        ContractId contractIdObj = ContractId.form(contractId);
        
        switch (elementId) {
            case "department_review" -> {
                if (approved) {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.DEPARTMENT_REVIEW);
                    log.info("部门审核通过，合同状态更新为部门审核中: contractId = {}", contractId);
                } else {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.REJECTED);
                    log.info("部门审核不通过，合同状态更新为已拒绝: contractId = {}", contractId);
                }
            }
            case "legal_review" -> {
                if (approved) {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.LEGAL_REVIEW);
                    log.info("法务审核通过，合同状态更新为法务审核中: contractId = {}", contractId);
                } else {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.REJECTED);
                    log.info("法务审核不通过，合同状态更新为已拒绝: contractId = {}", contractId);
                }
            }
            case "director_approval" -> {
                if (approved) {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.HIGH_LEVEL_APPROVAL);
                    log.info("总监审批通过，合同状态更新为高层审批中: contractId = {}", contractId);
                } else {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.REJECTED);
                    log.info("总监审批不通过，合同状态更新为已拒绝: contractId = {}", contractId);
                }
            }
            case "ceo_approval" -> {
                if (approved) {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.SEAL_APPLICATION);
                    log.info("CEO 审批通过，合同状态更新为用印申请中: contractId = {}", contractId);
                } else {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.REJECTED);
                    log.info("CEO 审批不通过，合同状态更新为已拒绝: contractId = {}", contractId);
                }
            }
            case "seal_application" -> {
                if (approved) {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.EXTERNAL_SIGNING);
                    log.info("用印申请通过，合同状态更新为外部签署中: contractId = {}", contractId);
                } else {
                    contractService.updateContractStatus(contractIdObj, operatorId, operatorName, 
                            ContractStatus.REJECTED);
                    log.info("用印申请不通过，合同状态更新为已拒绝: contractId = {}", contractId);
                }
            }
            default -> log.debug("节点 {} 不需要更新合同状态", elementId);
        }
    }
}
