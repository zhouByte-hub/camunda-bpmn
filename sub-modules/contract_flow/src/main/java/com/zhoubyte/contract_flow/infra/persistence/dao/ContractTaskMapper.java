package com.zhoubyte.contract_flow.infra.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoubyte.contract_flow.infra.persistence.po.ContractTaskPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContractTaskMapper extends BaseMapper<ContractTaskPO> {
}
