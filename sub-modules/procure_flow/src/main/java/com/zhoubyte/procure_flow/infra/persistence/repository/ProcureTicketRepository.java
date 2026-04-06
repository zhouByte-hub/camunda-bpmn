package com.zhoubyte.procure_flow.infra.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoubyte.procure_flow.domain.model.Ticket;
import com.zhoubyte.procure_flow.domain.repository.IProcureTicketRepository;
import com.zhoubyte.procure_flow.facade.dto.request.TicketSearchRequestParams;
import com.zhoubyte.procure_flow.infra.persistence.converter.TicketConverter;
import com.zhoubyte.procure_flow.infra.persistence.dao.ProcureTicketMapper;
import com.zhoubyte.procure_flow.infra.persistence.po.ProcureTicket;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
            procureTicket.setUpdateTime(LocalDateTime.now());
            result = procureTicketMapper.updateById(procureTicket);
        }else{
            procureTicket.setCreateTime(LocalDateTime.now());
            procureTicket.setUpdateTime(LocalDateTime.now());
            result = procureTicketMapper.insert(procureTicket);
        }
        if(result <= 0) {
            throw new RuntimeException("operator database failed");
        }
        return ticket;
    }

    @Override
    public Page<Ticket> queryTicketList(TicketSearchRequestParams ticketSearchRequestParams) {
        Page<ProcureTicket> page = new Page<>(ticketSearchRequestParams.getOffset(), ticketSearchRequestParams.getLimit());
        Wrapper<ProcureTicket> wrapper = Wrappers.<ProcureTicket>lambdaQuery()
                .like(StringUtils.isNotEmpty(ticketSearchRequestParams.getTicketName()), ProcureTicket::getTicketName, ticketSearchRequestParams.getTicketName())
                .like(StringUtils.isNotEmpty(ticketSearchRequestParams.getCreatorName()), ProcureTicket::getCreatorName, ticketSearchRequestParams.getCreatorName());

        Page<ProcureTicket> procureTicketPage = procureTicketMapper.selectPage(page, wrapper);
        Page<Ticket> result = new Page<>();
        result.setCurrent(page.getCurrent());
        result.setTotal(procureTicketPage.getTotal());
        List<Ticket> list = procureTicketPage.getRecords().stream().map(ticketConverter::convertProcureTicket).toList();
        result.setRecords(list);
        return result;
    }
}
