package com.zhoubyte.procure_flow.infra.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoubyte.procure_flow.domain.model.Ticket;
import com.zhoubyte.procure_flow.domain.repository.IProcureTicketRepository;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketId;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketStatus;
import com.zhoubyte.procure_flow.facade.dto.request.TicketSearchRequestParams;
import com.zhoubyte.procure_flow.infra.persistence.converter.TicketConverter;
import com.zhoubyte.procure_flow.infra.persistence.dao.ProcureTaskMapper;
import com.zhoubyte.procure_flow.infra.persistence.dao.ProcureTicketMapper;
import com.zhoubyte.procure_flow.infra.persistence.po.ProcureTask;
import com.zhoubyte.procure_flow.infra.persistence.po.ProcureTicket;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProcureTicketRepository implements IProcureTicketRepository {

    private final ProcureTicketMapper procureTicketMapper;
    private final ProcureTaskMapper procureTaskMapper;
    private final TicketConverter ticketConverter;

    public ProcureTicketRepository(ProcureTicketMapper procureTicketMapper, 
                                   ProcureTaskMapper procureTaskMapper,
                                   TicketConverter ticketConverter) {
        this.procureTicketMapper = procureTicketMapper;
        this.procureTaskMapper = procureTaskMapper;
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
        
        List<Ticket> list = procureTicketPage.getRecords().stream()
                .map(this::convertTicketWithAssignee)
                .toList();
        result.setRecords(list);
        return result;
    }

    @Override
    public Optional<Ticket> findByTicketId(TicketId ticketId) {
        if (ticketId == null) {
            return Optional.empty();
        }
        ProcureTicket procureTicket = procureTicketMapper.selectOne(
                Wrappers.<ProcureTicket>lambdaQuery().eq(ProcureTicket::getTicketId, ticketId.getValue())
        );
        if (procureTicket == null) {
            return Optional.empty();
        }
        return Optional.of(convertTicketWithAssignee(procureTicket));
    }
    
    private Ticket convertTicketWithAssignee(ProcureTicket procureTicket) {
        Ticket ticket = ticketConverter.convertProcureTicket(procureTicket);
        
        if (isTicketFinished(ticket.getTicketStatus())) {
            ticket.setCurrentAssignee(null);
        } else {
            String currentAssignee = getCurrentAssignee(procureTicket.getTicketId());
            ticket.setCurrentAssignee(currentAssignee);
        }
        
        return ticket;
    }
    
    private boolean isTicketFinished(TicketStatus ticketStatus) {
        return TicketStatus.RESOLVED.equals(ticketStatus) 
                || TicketStatus.CANCELED.equals(ticketStatus) 
                || TicketStatus.CLOSED.equals(ticketStatus);
    }
    
    private String getCurrentAssignee(String ticketId) {
        List<ProcureTask> processingTasks = procureTaskMapper.selectList(
                Wrappers.<ProcureTask>lambdaQuery()
                        .eq(ProcureTask::getTicketId, ticketId)
                        .eq(ProcureTask::getTaskStatus, "PROCESS")
                        .orderByDesc(ProcureTask::getCreateTime)
                        .last("LIMIT 1")
        );
        
        if (processingTasks.isEmpty()) {
            return null;
        }
        
        return processingTasks.get(0).getAssignee();
    }
}
