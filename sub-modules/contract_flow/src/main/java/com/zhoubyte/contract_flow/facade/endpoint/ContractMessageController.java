package com.zhoubyte.contract_flow.facade.endpoint;

import com.zhoubyte.contract_flow.application.service.ContractMessageService;
import com.zhoubyte.contract_flow.facade.dto.request.ExternalSignFeedbackRequest;
import com.zhoubyte.contract_flow.facade.dto.request.PaymentFeedbackRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/message")
@Slf4j
@RequiredArgsConstructor
public class ContractMessageController {
    
    private final ContractMessageService contractMessageService;
    
    @PostMapping("/external-sign/feedback")
    public ResponseEntity<String> sendExternalSignFeedback(
            @RequestBody @Validated ExternalSignFeedbackRequest request) {
        
        log.info("Sending external sign feedback: contractId = {}, signed = {}", 
                request.getContractId(), request.getSigned());
        
        contractMessageService.sendExternalSignFeedback(
                request.getContractId(),
                request.getSigned(),
                request.getFeedbackMsg()
        );
        
        log.info("External sign feedback sent successfully: contractId = {}", request.getContractId());
        
        return ResponseEntity.ok("External sign feedback sent successfully");
    }
    
    @PostMapping("/payment/feedback")
    public ResponseEntity<String> sendPaymentFeedback(
            @RequestBody @Validated PaymentFeedbackRequest request) {
        
        log.info("Sending payment feedback: contractId = {}, paymentSuccess = {}", 
                request.getContractId(), request.getPaymentSuccess());
        
        contractMessageService.sendPaymentFeedback(
                request.getContractId(),
                request.getPaymentSuccess(),
                request.getPaymentMsg()
        );
        
        log.info("Payment feedback sent successfully: contractId = {}", request.getContractId());
        
        return ResponseEntity.ok("Payment feedback sent successfully");
    }
}
