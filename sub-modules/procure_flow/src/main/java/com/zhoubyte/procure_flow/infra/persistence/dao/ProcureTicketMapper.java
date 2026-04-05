package com.zhoubyte.procure_flow.infra.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoubyte.procure_flow.infra.persistence.po.ProcureTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProcureTicketMapper extends BaseMapper<ProcureTicket> {
}
