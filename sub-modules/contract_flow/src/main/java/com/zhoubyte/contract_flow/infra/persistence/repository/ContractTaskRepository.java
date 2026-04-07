package com.zhoubyte.contract_flow.infra.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhoubyte.contract_flow.domain.model.Task;
import com.zhoubyte.contract_flow.domain.repository.IContractTaskRepository;
import com.zhoubyte.contract_flow.infra.persistence.converter.TaskConverter;
import com.zhoubyte.contract_flow.infra.persistence.dao.ContractTaskMapper;
import com.zhoubyte.contract_flow.infra.persistence.po.ContractTaskPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContractTaskRepository implements IContractTaskRepository {
    
    private final ContractTaskMapper contractTaskMapper;
    private final TaskConverter taskConverter;
    
    @Override
    public Task saveOrUpdate(Task task) {
        ContractTaskPO po = taskConverter.toPO(task);
        
        LambdaQueryWrapper<ContractTaskPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractTaskPO::getTaskKey, po.getTaskKey());
        
        ContractTaskPO existing = contractTaskMapper.selectOne(wrapper);
        
        if (existing == null) {
            contractTaskMapper.insert(po);
        } else {
            po.setId(existing.getId());
            contractTaskMapper.update(po, wrapper);
        }
        
        return taskConverter.toDomain(po);
    }
    
    @Override
    public Optional<Task> findByTaskKey(Long taskKey) {
        LambdaQueryWrapper<ContractTaskPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractTaskPO::getTaskKey, taskKey);
        
        ContractTaskPO po = contractTaskMapper.selectOne(wrapper);
        
        if (po == null) {
            return Optional.empty();
        }
        
        return Optional.of(taskConverter.toDomain(po));
    }
}
