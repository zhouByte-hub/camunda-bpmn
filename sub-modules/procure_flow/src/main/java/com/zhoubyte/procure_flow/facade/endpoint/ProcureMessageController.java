package com.zhoubyte.procure_flow.facade.endpoint;

import com.zhoubyte.procure_flow.application.service.ProcureMessageService;
import com.zhoubyte.procure_flow.facade.dto.request.OaFeedbackRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class ProcureMessageController {

    private final ProcureMessageService procureMessageService;

    @PostMapping("/oa-feedback")
    public ResponseEntity<String> sendOaFeedback(@RequestBody @Validated OaFeedbackRequest request) {
        procureMessageService.sendOaFeedbackMessage(
                request.getTicketId(),
                request.getOaAccepted(),
                request.getFeedbackMsg()
        );
        return ResponseEntity.ok("OA feedback message sent successfully");
    }
}
