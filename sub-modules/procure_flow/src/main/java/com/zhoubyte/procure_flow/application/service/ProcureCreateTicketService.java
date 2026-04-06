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
        Ticket ticket = ticketService.createTicket(ticketCreateParam);
        log.info("ProcureTicketService.createTicket ticket = {}", ticket);

        Map<String, Object> variables = new HashMap<>();
        variables.put(ProcureTicketConstant.TICKET_ID, ticket.getTicketId());
        variables.put(ProcureTicketConstant.TICKET_NAME, ticket.getTicketName());
        variables.put(ProcureTicketConstant.TICKET_TYPE, ticket.getTicketType());
        variables.put(ProcureTicketConstant.TICKET_PRIORITY, ticket.getTicketPriority());
        variables.put(ProcureTicketConstant.TICKET_STATUS, ticket.getTicketStatus());
        variables.put(ProcureTicketConstant.EXPECTED_COMPLETION_TIME, ticket.getExpectedCompletionTime());
        variables.put(ProcureTicketConstant.IS_NOTIFY, ticket.getIsNotify());
        variables.put(ProcureTicketConstant.PRE_OVERDUE_DAYS, ticket.getPreOverdueDays());
        variables.put(ProcureTicketConstant.POST_OVERDUE_DAYS, ticket.getPostOverdueDays());
        variables.put(ProcureTicketConstant.CREATOR_NAME, ticket.getCreatorName());
        variables.put(ProcureTicketConstant.CREATOR_ID, ticket.getCreatorId());
        log.info("ProcureTicketService.createTicket variables = {}", variables);

        StartProcessInstanceResult startProcessInstanceResult = processService.startProcessInstance(
                ProcureTicketConstant.PROCESS_INSTANCE_ID, null, null, variables);
        if(startProcessInstanceResult == null) {
            throw new RuntimeException("流程实例启动失败");
        }
        return ticket.getTicketId();
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
