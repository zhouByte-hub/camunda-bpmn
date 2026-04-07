package com.zhoubyte.contract_flow.infra.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoubyte.contract_flow.domain.model.Contract;
import com.zhoubyte.contract_flow.domain.repository.IContractRepository;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractId;
import com.zhoubyte.contract_flow.facade.dto.request.ContractSearchRequest;
import com.zhoubyte.contract_flow.infra.persistence.converter.ContractConverter;
import com.zhoubyte.contract_flow.infra.persistence.dao.ContractMapper;
import com.zhoubyte.contract_flow.infra.persistence.dao.ContractTaskMapper;
import com.zhoubyte.contract_flow.infra.persistence.po.ContractPO;
import com.zhoubyte.contract_flow.infra.persistence.po.ContractTaskPO;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractStatus;
import com.zhoubyte.contract_flow.domain.valobj.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContractRepository implements IContractRepository {
    
    private final ContractMapper contractMapper;
    private final ContractTaskMapper contractTaskMapper;
    private final ContractConverter contractConverter;
    
    @Override
    public Contract saveOrUpdate(Contract contract) {
        ContractPO po = contractConverter.toPO(contract);
        
        LambdaQueryWrapper<ContractPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractPO::getTicketId, po.getTicketId());
        
        ContractPO existing = contractMapper.selectOne(wrapper);
        
        if (existing == null) {
            contractMapper.insert(po);
        } else {
            contractMapper.update(po, wrapper);
        }
        
        return contractConverter.toDomain(po);
    }
    
    @Override
    public Page<Contract> queryContractList(ContractSearchRequest request) {
        Page<ContractPO> page = new Page<>(request.getOffset(), request.getLimit());
        
        LambdaQueryWrapper<ContractPO> wrapper = new LambdaQueryWrapper<>();
        
        if (request.getContractStatus() != null && !request.getContractStatus().isEmpty()) {
            wrapper.eq(ContractPO::getContractStatus, request.getContractStatus());
        }
        
        if (request.getContractType() != null && !request.getContractType().isEmpty()) {
            wrapper.eq(ContractPO::getContractType, request.getContractType());
        }
        
        if (request.getContractName() != null && !request.getContractName().isEmpty()) {
            wrapper.like(ContractPO::getTicketName, request.getContractName());
        }
        
        wrapper.orderByDesc(ContractPO::getCreateTime);
        
        Page<ContractPO> poPage = contractMapper.selectPage(page, wrapper);
        
        Page<Contract> domainPage = new Page<>();
        domainPage.setRecords(poPage.getRecords().stream()
                .map(contractConverter::toDomain)
                .toList());
        domainPage.setTotal(poPage.getTotal());
        domainPage.setCurrent(poPage.getCurrent());
        domainPage.setSize(poPage.getSize());
        
        for (Contract contract : domainPage.getRecords()) {
            String currentAssignee = getCurrentAssignee(contract);
            contract.setCurrentAssignee(currentAssignee);
        }
        
        return domainPage;
    }
    
    @Override
    public Optional<Contract> findByContractId(ContractId contractId) {
        LambdaQueryWrapper<ContractPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractPO::getTicketId, contractId.getValue());
        
        ContractPO po = contractMapper.selectOne(wrapper);
        
        if (po == null) {
            return Optional.empty();
        }
        
        Contract contract = contractConverter.toDomain(po);
        String currentAssignee = getCurrentAssignee(contract);
        contract.setCurrentAssignee(currentAssignee);
        
        return Optional.of(contract);
    }
    
    private String getCurrentAssignee(Contract contract) {
        ContractStatus status = contract.getContractStatus();
        
        if (status == ContractStatus.COMPLETED || 
            status == ContractStatus.REJECTED || 
            status == ContractStatus.CANCELED) {
            return null;
        }
        
        LambdaQueryWrapper<ContractTaskPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractTaskPO::getTicketId, contract.getContractId().getValue())
                .eq(ContractTaskPO::getTaskStatus, TaskStatus.PROCESS.getValue())
                .orderByDesc(ContractTaskPO::getCreateTime)
                .last("LIMIT 1");
        
        ContractTaskPO task = contractTaskMapper.selectOne(wrapper);
        
        if (task != null) {
            return task.getAssignee();
        }
        
        return null;
    }
}
