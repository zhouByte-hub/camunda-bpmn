package com.zhoubyte.contract_flow.infra.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoubyte.contract_flow.infra.persistence.po.ContractPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContractMapper extends BaseMapper<ContractPO> {
}
