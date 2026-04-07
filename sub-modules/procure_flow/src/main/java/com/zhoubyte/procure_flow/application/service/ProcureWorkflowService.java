package com.zhoubyte.procure_flow.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcureWorkflowService {

    public void submitToOaSystem(String ticketId, String ticketName, String creatorName) {
        log.info("提交工单到 OA 系统: ticketId = {}, ticketName = {}, creatorName = {}", 
                ticketId, ticketName, creatorName);
    }

    public void sendEmailToApplier(String ticketId, String creatorName, String ticketName) {
        log.info("发送邮件通知申请人: ticketId = {}, creatorName = {}, ticketName = {}", 
                ticketId, creatorName, ticketName);
    }

    public void sendResultEmail(String ticketId, String creatorName, String ticketName, Boolean approved) {
        log.info("发送审批结果邮件: ticketId = {}, creatorName = {}, approved = {}", 
                ticketId, creatorName, approved);
    }

    public void acceptOaFeedback(String ticketId, Boolean oaAccepted) {
        log.info("接受 OA 反馈并更新任务状态: ticketId = {}, oaAccepted = {}", ticketId, oaAccepted);
    }

    public void overdueNotify(String ticketId, String creatorName, String ticketName) {
        log.info("发送延期提醒: ticketId = {}, creatorName = {}, ticketName = {}", 
                ticketId, creatorName, ticketName);
    }

    public void endProcess(String ticketId, String ticketName) {
        log.info("采购流程结束: ticketId = {}, ticketName = {}", ticketId, ticketName);
    }
}
