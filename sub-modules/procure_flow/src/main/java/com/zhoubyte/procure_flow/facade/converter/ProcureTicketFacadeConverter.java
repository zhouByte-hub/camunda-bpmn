package com.zhoubyte.procure_flow.facade.converter;

import com.zhoubyte.procure_flow.application.dto.TicketCreateParam;
import com.zhoubyte.procure_flow.facade.dto.request.TicketCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProcureTicketFacadeConverter {

    @Mappings({
            @Mapping(target = "ticketName", source = "request.ticketName"),
            @Mapping(target = "ticketPriority", source = "request.ticketPriority"),
            @Mapping(target = "ticketRemark", source = "request.ticketRemark"),
            @Mapping(target = "expectedCompletionTime", source = "request.expectedCompletionTime"),
            @Mapping(target = "isNotify", source = "request.isNotify"),
            @Mapping(target = "preOverdueDays", source = "request.preOverdueDays"),
            @Mapping(target = "postOverdueDays", source = "request.postOverdueDays"),
            @Mapping(target = "creatorName", source = "creatorName"),
            @Mapping(target = "creatorId", source = "creatorId")
    })
    TicketCreateParam toCreateParam(TicketCreateRequest request, String creatorId, String creatorName);
}
