package com.zhoubyte.contract_flow.application.service;

import com.zhoubyte.scorpio.wrapper.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractMessageService {
    
    private final ProcessService processService;
    
    public void sendExternalSignFeedback(String contractId, Boolean signed, String feedbackMsg) {
        log.info("发送外部签署反馈消息: contractId = {}, signed = {}, feedbackMsg = {}", 
                contractId, signed, feedbackMsg);
        
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("external_signed", signed);
            variables.put("external_feedback_msg", feedbackMsg);
            variables.put("external_feedback_time", System.currentTimeMillis());
            
            processService.publishMessage("external_sign_feedback_message", contractId, variables);
            
            log.info("外部签署反馈消息已发送: contractId = {}", contractId);
        } catch (Exception e) {
            log.error("发送外部签署反馈消息失败: contractId = {}", contractId, e);
            throw new RuntimeException("发送外部签署反馈消息失败", e);
        }
    }
    
    public void sendPaymentFeedback(String contractId, Boolean paymentSuccess, String paymentMsg) {
        log.info("发送付款反馈消息: contractId = {}, paymentSuccess = {}, paymentMsg = {}", 
                contractId, paymentSuccess, paymentMsg);
        
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("payment_success", paymentSuccess);
            variables.put("payment_msg", paymentMsg);
            variables.put("payment_time", System.currentTimeMillis());
            
            processService.publishMessage("payment_feedback_message", contractId, variables);
            
            log.info("付款反馈消息已发送: contractId = {}", contractId);
        } catch (Exception e) {
            log.error("发送付款反馈消息失败: contractId = {}", contractId, e);
            throw new RuntimeException("发送付款反馈消息失败", e);
        }
    }
}
