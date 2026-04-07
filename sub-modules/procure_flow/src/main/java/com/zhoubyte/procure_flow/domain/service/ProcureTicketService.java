package com.zhoubyte.procure_flow.domain.service;

import com.zhoubyte.procure_flow.application.dto.TicketCreateParam;
import com.zhoubyte.procure_flow.domain.model.Ticket;
import com.zhoubyte.procure_flow.domain.repository.IProcureTicketRepository;
import com.zhoubyte.procure_flow.domain.utils.IdGenerator;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketId;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketPriority;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketStatus;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProcureTicketService {

    private final IProcureTicketRepository ticketRepository;
    private final IdGenerator idGenerator;

    public Ticket createTicket(TicketCreateParam ticketCreateParam) {
        TicketId ticketId = TicketId.form(idGenerator.generateTicketId());
        Ticket ticket = Ticket.builder()
                .ticketId(ticketId)
                .ticketName(ticketCreateParam.getTicketName())
                .ticketType(TicketType.PROCURE_TICKET)
                .ticketPriority(TicketPriority.fromValue(ticketCreateParam.getTicketPriority()))
                .ticketStatus(TicketStatus.PENDING)
                .ticketRemark(ticketCreateParam.getTicketRemark())
                .expectedCompletionTime(ticketCreateParam.getExpectedCompletionTime())
                .isNotify(ticketCreateParam.getIsNotify())
                .preOverdueDays(ticketCreateParam.getPreOverdueDays())
                .postOverdueDays(ticketCreateParam.getPostOverdueDays())
                .creatorName(ticketCreateParam.getCreatorName())
                .creatorId(ticketCreateParam.getCreatorId())
                .build();
        ticket = ticketRepository.saveOrUpdate(ticket);
        return this.changeTicketStatus(ticket, ticketCreateParam.getCreatorId(), ticketCreateParam.getCreatorName(), TicketStatus.PROCESSING);
    }

    public Ticket updateTicketStatus(TicketId ticketId, String operatorId, String operatorName, TicketStatus expectedStatus) {
        Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketId.getValue()));
        return changeTicketStatus(ticket, operatorId, operatorName, expectedStatus);
    }

    private Ticket changeTicketStatus(Ticket ticket, String operatorId, String operatorName, TicketStatus expectedStatus) {
        switch (expectedStatus) {
            case TicketStatus.PROCESSING -> ticket.start(operatorId, operatorName);
            case TicketStatus.WAIT_CONFIRM -> ticket.waitConfirm(operatorId, operatorName);
            case TicketStatus.RESOLVED -> ticket.resolved(operatorId, operatorName);
            case TicketStatus.CLOSED -> ticket.close(operatorId, operatorName);
            case TicketStatus.CANCELED -> ticket.cancel(operatorId, operatorName);
            default -> throw new RuntimeException(expectedStatus.getValue() + "是一个不存在的工单类型");
        }
        return ticketRepository.saveOrUpdate(ticket);
    }
}
