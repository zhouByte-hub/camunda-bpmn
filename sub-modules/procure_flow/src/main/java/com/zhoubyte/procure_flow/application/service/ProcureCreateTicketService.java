package com.zhoubyte.procure_flow.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoubyte.procure_flow.application.config.ProcureTicketConstant;
import com.zhoubyte.procure_flow.application.dto.TicketCreateParam;
import com.zhoubyte.procure_flow.domain.model.Ticket;
import com.zhoubyte.procure_flow.domain.repository.IProcureTicketRepository;
import com.zhoubyte.procure_flow.domain.service.ProcureTicketService;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketId;
import com.zhoubyte.procure_flow.facade.dto.request.TicketSearchRequestParams;
import com.zhoubyte.procure_flow.facade.dto.response.TicketSearchResponse;
import com.zhoubyte.scorpio.dto.StartProcessInstanceResult;
import com.zhoubyte.scorpio.wrapper.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcureCreateTicketService {

    private final ProcureTicketService ticketService;
    private final ProcessService processService;
    private final IProcureTicketRepository iProcureTicketRepository;

    public TicketId createTicket(TicketCreateParam ticketCreateParam) {
        validateTicketCreateParam(ticketCreateParam);
        
        Ticket ticket = ticketService.createTicket(ticketCreateParam);
        log.info("ProcureTicketService.createTicket ticket = {}", ticket);

        Map<String, Object> variables = new HashMap<>();
        variables.put(ProcureTicketConstant.TICKET_ID, ticket.getTicketId().getValue());
        variables.put(ProcureTicketConstant.TICKET_NAME, ticket.getTicketName());
        variables.put(ProcureTicketConstant.TICKET_TYPE, ticket.getTicketType().getValue());
        variables.put(ProcureTicketConstant.TICKET_PRIORITY, ticket.getTicketPriority().getValue());
        variables.put(ProcureTicketConstant.TICKET_STATUS, ticket.getTicketStatus().getValue());
        variables.put(ProcureTicketConstant.EXPECTED_COMPLETION_TIME, ticket.getExpectedCompletionTime());
        variables.put(ProcureTicketConstant.IS_NOTIFY, ticket.getIsNotify());
        variables.put(ProcureTicketConstant.PRE_OVERDUE_DAYS, ticket.getPreOverdueDays());
        variables.put(ProcureTicketConstant.POST_OVERDUE_DAYS, ticket.getPostOverdueDays());
        variables.put(ProcureTicketConstant.CREATOR_NAME, ticket.getCreatorName());
        variables.put(ProcureTicketConstant.CREATOR_ID, ticket.getCreatorId());
        
        String overdueDefinition = calculateOverdueDefinition(ticket.getIsNotify(), 
                ticket.getPreOverdueDays(), ticket.getPostOverdueDays());
        variables.put(ProcureTicketConstant.OVERDUE_DEFINITION, overdueDefinition);
        
        log.info("ProcureTicketService.createTicket variables = {}", variables);

        StartProcessInstanceResult startProcessInstanceResult = processService.startProcessInstance(
                ProcureTicketConstant.PROCESS_INSTANCE_ID, null, null, variables);
        if(startProcessInstanceResult == null) {
            throw new RuntimeException("流程实例启动失败");
        }
        return ticket.getTicketId();
    }
    
    private void validateTicketCreateParam(TicketCreateParam param) {
        if (Boolean.TRUE.equals(param.getIsNotify())) {
            if (param.getPreOverdueDays() == null) {
                throw new IllegalArgumentException("当 isNotify 为 true 时，preOverdueDays 不能为空");
            }
            if (param.getPostOverdueDays() == null) {
                throw new IllegalArgumentException("当 isNotify 为 true 时，postOverdueDays 不能为空");
            }
            if (param.getPreOverdueDays() < 0) {
                throw new IllegalArgumentException("preOverdueDays 不能为负数");
            }
            if (param.getPostOverdueDays() < 0) {
                throw new IllegalArgumentException("postOverdueDays 不能为负数");
            }
        }
    }
    
    private String calculateOverdueDefinition(Boolean isNotify, Integer preOverdueDays, Integer postOverdueDays) {
        if (Boolean.FALSE.equals(isNotify)) {
            return null;
        }
        
        if (preOverdueDays == null || postOverdueDays == null) {
            return null;
        }
        
        int totalDays = preOverdueDays + postOverdueDays;
        if (totalDays == 0) {
            return "PT1H";
        }
        
        return String.format("P%dD", totalDays);
    }

    public TicketSearchResponse ticketList(TicketSearchRequestParams ticketSearchRequestParams) {
        Page<Ticket> ticketPage = iProcureTicketRepository.queryTicketList(ticketSearchRequestParams);
        TicketSearchResponse ticketSearchResponse = new TicketSearchResponse();
        ticketSearchResponse.setTicketList(ticketPage.getRecords());
        ticketSearchResponse.setTotal(ticketPage.getTotal());
        ticketSearchResponse.setCurrentPage(ticketSearchResponse.getCurrentPage());
        return ticketSearchResponse;
    }

}
