package com.zhoubyte.procure_flow.infra.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhoubyte.procure_flow.domain.model.Ticket;
import com.zhoubyte.procure_flow.domain.repository.IProcureTicketRepository;
import com.zhoubyte.procure_flow.infra.persistence.converter.TicketConverter;
import com.zhoubyte.procure_flow.infra.persistence.dao.ProcureTicketMapper;
import com.zhoubyte.procure_flow.infra.persistence.po.ProcureTicket;
import org.springframework.stereotype.Repository;

@Repository
public class ProcureTicketRepository implements IProcureTicketRepository {

    private final ProcureTicketMapper procureTicketMapper;
    private final TicketConverter ticketConverter;

    public ProcureTicketRepository(ProcureTicketMapper procureTicketMapper, TicketConverter ticketConverter) {
        this.procureTicketMapper = procureTicketMapper;
        this.ticketConverter = ticketConverter;
    }

    @Override
    public Ticket saveOrUpdate(Ticket ticket) {
        if(ticket == null) {
            throw new IllegalArgumentException("ticket is null");
        }
        ProcureTicket procureTicket = ticketConverter.convertTicket(ticket);
        boolean exists = procureTicketMapper
                .exists(Wrappers.<ProcureTicket>lambdaQuery().eq(ProcureTicket::getTicketId, procureTicket.getTicketId()));
        int result;
        if(exists) {
            result = procureTicketMapper.updateById(procureTicket);
        }else{
            result = procureTicketMapper.insert(procureTicket);
        }
        if(result <= 0) {
            throw new RuntimeException("operator database failed");
        }
        return ticket;
    }
}
