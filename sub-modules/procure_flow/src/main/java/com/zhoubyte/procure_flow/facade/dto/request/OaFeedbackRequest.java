package com.zhoubyte.procure_flow.facade.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OaFeedbackRequest {

    @NotNull(message = "ticket_id 不能为空")
    private String ticketId;
    
    @NotNull(message = "oa_accepted 不能为空")
    private Boolean oaAccepted;
    
    private String feedbackMsg;

}
