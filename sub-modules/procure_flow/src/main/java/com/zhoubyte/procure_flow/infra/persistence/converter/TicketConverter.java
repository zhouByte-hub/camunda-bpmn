package com.zhoubyte.procure_flow.infra.persistence.converter;

import com.zhoubyte.procure_flow.domain.model.Ticket;
import com.zhoubyte.procure_flow.infra.persistence.po.ProcureTicket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TicketConverter {

    @Mappings({
            @Mapping(source = "ticketId.value", target = "ticketId"),
            @Mapping(source = "ticketType.value", target = "ticketType"),
            @Mapping(source = "ticketPriority.value", target = "ticketPriority"),
            @Mapping(source = "ticketStatus.value", target = "ticketStatus")
    })
    ProcureTicket convertTicket(Ticket ticket);

}
