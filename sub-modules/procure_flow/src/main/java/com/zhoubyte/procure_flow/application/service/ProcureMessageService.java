package com.zhoubyte.procure_flow.application.service;

import com.zhoubyte.scorpio.wrapper.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcureMessageService {

    private final ProcessService processService;

    public void sendOaFeedbackMessage(String ticketId, Boolean oaAccepted, String feedbackMsg) {
        log.info("发送 OA 反馈消息: ticketId = {}, oaAccepted = {}, feedbackMsg = {}", 
                ticketId, oaAccepted, feedbackMsg);
        
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("oa_accepted", oaAccepted);
            variables.put("oa_feedback_msg", feedbackMsg);
            variables.put("oa_feedback_time", System.currentTimeMillis());
            
            processService.publishMessage("oa_feedback_message", ticketId, variables);
            
            log.info("OA 反馈消息已发送: ticketId = {}", ticketId);
        } catch (Exception e) {
            log.error("发送 OA 反馈消息失败: ticketId = {}", ticketId, e);
            throw new RuntimeException("发送 OA 反馈消息失败", e);
        }
    }
}
