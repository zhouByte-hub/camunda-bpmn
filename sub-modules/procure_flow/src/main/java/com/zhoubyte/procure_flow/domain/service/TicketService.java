package com.zhoubyte.procure_flow.domain.service;

import com.zhoubyte.procure_flow.application.dto.TicketCreateParam;
import com.zhoubyte.procure_flow.domain.model.Ticket;
import com.zhoubyte.procure_flow.domain.repository.IProcureTicketRepository;
import com.zhoubyte.procure_flow.domain.valobj.TicketId;
import com.zhoubyte.procure_flow.domain.valobj.TicketPriority;
import com.zhoubyte.procure_flow.domain.valobj.TicketStatus;
import com.zhoubyte.procure_flow.domain.valobj.TicketType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TicketService {

    private static final AtomicInteger sequence = new AtomicInteger(0);
    private static String currentDate;

    private final IProcureTicketRepository ticketRepository;

    public TicketService(IProcureTicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
        currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public Ticket createTicket(TicketCreateParam ticketCreateParam) {
        TicketId ticketId = TicketId.form(generateTicketId());
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
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        ticket = ticketRepository.saveOrUpdate(ticket);
        return this.changeTicketStatus(ticket, ticketCreateParam.getCreatorId(), ticketCreateParam.getCreatorName(), TicketStatus.PROCESSING);
    }


    private String generateTicketId() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        if(!today.equals(currentDate)){
            currentDate = today;
            sequence.set(0);
        }
        int seq = sequence.incrementAndGet();
        return "TICKET_" + currentDate + String.format("%05d", seq);
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
