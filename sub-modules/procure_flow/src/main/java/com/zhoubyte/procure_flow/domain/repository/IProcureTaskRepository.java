package com.zhoubyte.procure_flow.domain.repository;

import com.zhoubyte.procure_flow.domain.model.Task;

public interface IProcureTaskRepository {

    Task saveOrUpdate(Task task);
}
