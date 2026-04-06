package com.zhoubyte.procure_flow.facade.dto.response;

import com.zhoubyte.procure_flow.domain.model.Ticket;
import lombok.Data;

import java.util.List;

@Data
public class TicketSearchResponse {

    private List<Ticket> ticketList;
    private Integer currentPage;
    private Long total;

}
