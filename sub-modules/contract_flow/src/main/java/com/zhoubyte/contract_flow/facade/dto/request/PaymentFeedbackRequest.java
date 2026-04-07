package com.zhoubyte.contract_flow.facade.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentFeedbackRequest {
    
    @NotBlank(message = "contract_id 不能为空")
    private String contractId;
    
    @NotNull(message = "payment_success 不能为空")
    private Boolean paymentSuccess;
    
    private String paymentMsg;
}
