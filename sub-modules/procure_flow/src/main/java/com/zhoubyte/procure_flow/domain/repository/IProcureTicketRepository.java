package com.zhoubyte.procure_flow.domain.repository;

import com.zhoubyte.procure_flow.domain.model.Ticket;

public interface IProcureTicketRepository {

    Ticket saveOrUpdate(Ticket ticket);
}
