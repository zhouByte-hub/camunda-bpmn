package com.zhoubyte.procure_flow.infra.external;

import com.zhoubyte.procure_flow.application.service.ProcureWorkflowService;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import io.camunda.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProcureServiceTaskWorker {

    private final ProcureWorkflowService procureWorkflowService;

    @JobWorker(type = "submit_apply_form_to_oa_system")
    public void submitApplyFormToOaSystem(JobClient client, ActivatedJob job) {
        log.info("submitApplyFormToOaSystem job = {}", job);
        
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String ticketId = (String) variables.get("ticket_id");
            String ticketName = (String) variables.get("ticket_name");
            String creatorName = (String) variables.get("creator_name");
            
            procureWorkflowService.submitToOaSystem(ticketId, ticketName, creatorName);
            
            Map<String, Object> resultVariables = new HashMap<>();
            resultVariables.put("oa_submitted", true);
            resultVariables.put("oa_submit_time", System.currentTimeMillis());
            
            client.newCompleteCommand(job.getKey())
                    .variables(resultVariables)
                    .send()
                    .join();
            
            log.info("工单已成功提交到 OA 系统: ticketId = {}", ticketId);
        } catch (Exception e) {
            log.error("提交工单到 OA 系统失败", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("提交 OA 系统失败: " + e.getMessage())
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "send_email_to_applier")
    public void sendEmailToApplier(JobClient client, ActivatedJob job) {
        log.info("sendEmailToApplier job = {}", job);
        
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String ticketId = (String) variables.get("ticket_id");
            String creatorName = (String) variables.get("creator_name");
            String ticketName = (String) variables.get("ticket_name");
            
            procureWorkflowService.sendEmailToApplier(ticketId, creatorName, ticketName);
            
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();
            
            log.info("邮件已发送给申请人: {}", creatorName);
        } catch (Exception e) {
            log.error("发送邮件给申请人失败", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("发送邮件失败: " + e.getMessage())
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "send_result_email")
    public void sendResultEmail(JobClient client, ActivatedJob job) {
        log.info("sendResultEmail job = {}", job);
        
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String ticketId = (String) variables.get("ticket_id");
            String creatorName = (String) variables.get("creator_name");
            String ticketName = (String) variables.get("ticket_name");
            Boolean approved = (Boolean) variables.get("approved");
            
            procureWorkflowService.sendResultEmail(ticketId, creatorName, ticketName, approved);
            
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();
            
            log.info("审批结果邮件已发送: {}", creatorName);
        } catch (Exception e) {
            log.error("发送审批结果邮件失败", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("发送结果邮件失败: " + e.getMessage())
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "accept_oa_update_task_status")
    public void acceptOaUpdateTaskStatus(JobClient client, ActivatedJob job) {
        log.info("acceptOaUpdateTaskStatus job = {}", job);
        
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String ticketId = (String) variables.get("ticket_id");
            Boolean oaAccepted = (Boolean) variables.get("oa_accepted");
            
            procureWorkflowService.acceptOaFeedback(ticketId, oaAccepted);
            
            Map<String, Object> resultVariables = new HashMap<>();
            resultVariables.put("oa_feedback_accepted", true);
            
            client.newCompleteCommand(job.getKey())
                    .variables(resultVariables)
                    .send()
                    .join();
            
            log.info("OA 反馈已接受，任务状态已更新: ticketId = {}", ticketId);
        } catch (Exception e) {
            log.error("接受 OA 反馈失败", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("接受 OA 反馈失败: " + e.getMessage())
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "overdue_notify")
    public void overdueNotify(JobClient client, ActivatedJob job) {
        log.info("overdueNotify job = {}", job);
        
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String ticketId = (String) variables.get("ticket_id");
            String creatorName = (String) variables.get("creator_name");
            String ticketName = (String) variables.get("ticket_name");
            
            procureWorkflowService.overdueNotify(ticketId, creatorName, ticketName);
            
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();
            
            log.info("延期提醒已发送: ticketId = {}", ticketId);
        } catch (Exception e) {
            log.error("发送延期提醒失败", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("发送延期提醒失败: " + e.getMessage())
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "end_procure_process")
    public void endProcureProcess(JobClient client, ActivatedJob job) {
        log.info("endProcureProcess job = {}", job);
        
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String ticketId = (String) variables.get("ticket_id");
            String ticketName = (String) variables.get("ticket_name");
            
            procureWorkflowService.endProcess(ticketId, ticketName);
            
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();
            
            log.info("采购流程已结束: ticketId = {}", ticketId);
        } catch (Exception e) {
            log.error("结束采购流程失败", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("结束流程失败: " + e.getMessage())
                    .send()
                    .join();
        }
    }
}
