package com.zhoubyte.procure_flow.domain.repository;

import com.zhoubyte.procure_flow.domain.model.Task;

import java.util.Optional;

public interface IProcureTaskRepository {

    Task saveOrUpdate(Task task);
    
    Optional<Task> findByTaskKey(Long taskKey);
}
