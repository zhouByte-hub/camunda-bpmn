package com.zhoubyte.contract_flow.infra.external;

import com.zhoubyte.contract_flow.application.service.ContractWorkflowService;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import io.camunda.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ContractServiceTaskWorker {
    
    private final ContractWorkflowService contractWorkflowService;
    
    @JobWorker(type = "fetch_risk_control_data")
    public void fetchRiskControlData(JobClient client, ActivatedJob job) {
        log.info("fetchRiskControlData job = {}", job);
        
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String contractId = (String) variables.get("contract_id");
            String crmCustomerId = (String) variables.get("crm_customer_id");
            
            contractWorkflowService.fetchRiskControlData(contractId, crmCustomerId);
            
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();
            
            log.info("风控数据获取完成: contractId = {}", contractId);
        } catch (Exception e) {
            log.error("获取风控数据失败", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("获取风控数据失败: " + e.getMessage())
                    .send()
                    .join();
        }
    }
    
    @JobWorker(type = "send_to_esign_platform")
    public void sendToEsignPlatform(JobClient client, ActivatedJob job) {
        log.info("sendToEsignPlatform job = {}", job);
        
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String contractId = (String) variables.get("contract_id");
            String contractDocumentId = (String) variables.get("contract_document_id");
            
            contractWorkflowService.sendToEsignPlatform(contractId, contractDocumentId);
            
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();
            
            log.info("已发送到电子签平台: contractId = {}", contractId);
        } catch (Exception e) {
            log.error("发送到电子签平台失败", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("发送到电子签平台失败: " + e.getMessage())
                    .send()
                    .join();
        }
    }
    
    @JobWorker(type = "archive_contract")
    public void archiveContract(JobClient client, ActivatedJob job) {
        log.info("archiveContract job = {}", job);
        
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String contractId = (String) variables.get("contract_id");
            String signedDocumentId = (String) variables.get("signed_document_id");
            
            contractWorkflowService.archiveContract(contractId, signedDocumentId);
            
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();
            
            log.info("合同已归档: contractId = {}", contractId);
        } catch (Exception e) {
            log.error("归档合同失败", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("归档合同失败: " + e.getMessage())
                    .send()
                    .join();
        }
    }
    
    @JobWorker(type = "update_crm_status")
    public void updateCrmStatus(JobClient client, ActivatedJob job) {
        log.info("updateCrmStatus job = {}", job);
        
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String contractId = (String) variables.get("contract_id");
            String crmCustomerId = (String) variables.get("crm_customer_id");
            
            contractWorkflowService.updateCrmStatus(contractId, crmCustomerId);
            
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();
            
            log.info("CRM 状态已更新: contractId = {}", contractId);
        } catch (Exception e) {
            log.error("更新 CRM 状态失败", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("更新 CRM 状态失败: " + e.getMessage())
                    .send()
                    .join();
        }
    }
    
    @JobWorker(type = "trigger_payment_process")
    public void triggerPaymentProcess(JobClient client, ActivatedJob job) {
        log.info("triggerPaymentProcess job = {}", job);
        
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String contractId = (String) variables.get("contract_id");
            
            contractWorkflowService.triggerPaymentProcess(contractId);
            
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();
            
            log.info("付款流程已触发: contractId = {}", contractId);
        } catch (Exception e) {
            log.error("触发付款流程失败", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("触发付款流程失败: " + e.getMessage())
                    .send()
                    .join();
        }
    }
    
    @JobWorker(type = "send_expiry_reminder")
    public void sendExpiryReminder(JobClient client, ActivatedJob job) {
        log.info("sendExpiryReminder job = {}", job);
        
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String contractId = (String) variables.get("contract_id");
            String creatorName = (String) variables.get("creator_name");
            
            contractWorkflowService.sendExpiryReminder(contractId, creatorName);
            
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();
            
            log.info("到期提醒已发送: contractId = {}", contractId);
        } catch (Exception e) {
            log.error("发送到期提醒失败", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("发送到期提醒失败: " + e.getMessage())
                    .send()
                    .join();
        }
    }
}
