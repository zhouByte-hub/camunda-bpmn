package com.zhoubyte.contract_flow.domain.repository;

import com.zhoubyte.contract_flow.domain.model.Task;

import java.util.Optional;

public interface IContractTaskRepository {
    
    Task saveOrUpdate(Task task);
    
    Optional<Task> findByTaskKey(Long taskKey);
}
