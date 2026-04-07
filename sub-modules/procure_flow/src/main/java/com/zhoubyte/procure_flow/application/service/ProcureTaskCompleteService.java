package com.zhoubyte.procure_flow.application.service;

import com.zhoubyte.procure_flow.domain.model.Task;
import com.zhoubyte.procure_flow.domain.service.ProcureJobService;
import com.zhoubyte.procure_flow.domain.service.ProcureTicketService;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketStatus;
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
public class ProcureTaskCompleteService {

    private final ProcessService processService;
    private final ProcureJobService procureJobService;
    private final ProcureTicketService procureTicketService;

    @Transactional
    public void completeUserTask(Long taskKey, Integer auditResult, String msg, String operatorId, String operatorName) {
        log.info("completeUserTask taskKey = {}, auditResult = {}, msg = {}, operatorId = {}, operatorName = {}", 
                taskKey, auditResult, msg, operatorId, operatorName);
        
        Task task = procureJobService.findByTaskKey(taskKey)
                .orElseThrow(() -> new RuntimeException("Task not found with taskKey: " + taskKey));
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("audit_result", auditResult);
        variables.put("audit_msg", msg);
        
        boolean approved = auditResult != null && auditResult == 1;
        variables.put("approved", approved);
        
        processService.completeUserTask(taskKey, variables);
        
        task.toComplete();
        procureJobService.updateTask(task);
        
        updateTicketStatusByElementId(task.getElementId(), task.getTicketId().getValue(), 
                approved, operatorId, operatorName);
        
        log.info("UserTask 已完成: taskKey = {}, approved = {}", taskKey, approved);
    }
    
    private void updateTicketStatusByElementId(String elementId, String ticketId, 
            boolean approved, String operatorId, String operatorName) {
        switch (elementId) {
            case "leader_audit" -> {
                log.info("负责人审批完成，工单继续处理中: ticketId = {}", ticketId);
            }
            case "ceo_audit" -> {
                if (approved) {
                    procureTicketService.updateTicketStatus(
                            com.zhoubyte.procure_flow.domain.valobj.ticket.TicketId.form(ticketId),
                            operatorId,
                            operatorName,
                            TicketStatus.RESOLVED
                    );
                    log.info("CEO 审批通过，工单状态更新为已解决: ticketId = {}", ticketId);
                } else {
                    log.info("CEO 审批不通过，工单继续处理中: ticketId = {}", ticketId);
                }
            }
            default -> log.debug("节点 {} 不需要更新工单状态", elementId);
        }
    }
}
