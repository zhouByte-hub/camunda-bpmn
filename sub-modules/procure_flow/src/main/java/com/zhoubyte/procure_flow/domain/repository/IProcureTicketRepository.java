package com.zhoubyte.procure_flow.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoubyte.procure_flow.domain.model.Ticket;
import com.zhoubyte.procure_flow.facade.dto.request.TicketSearchRequestParams;

public interface IProcureTicketRepository {

    Ticket saveOrUpdate(Ticket ticket);

    Page<Ticket> queryTicketList(TicketSearchRequestParams ticketSearchRequestParams);
}
