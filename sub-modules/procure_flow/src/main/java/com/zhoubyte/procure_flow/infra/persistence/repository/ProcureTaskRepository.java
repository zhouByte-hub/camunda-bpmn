package com.zhoubyte.procure_flow.infra.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhoubyte.procure_flow.domain.model.Task;
import com.zhoubyte.procure_flow.domain.repository.IProcureTaskRepository;
import com.zhoubyte.procure_flow.infra.persistence.converter.TaskConverter;
import com.zhoubyte.procure_flow.infra.persistence.dao.ProcureTaskMapper;
import com.zhoubyte.procure_flow.infra.persistence.po.ProcureTask;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class ProcureTaskRepository implements IProcureTaskRepository {

    private final ProcureTaskMapper procureTaskMapper;
    private final TaskConverter taskConverter;

    public ProcureTaskRepository(ProcureTaskMapper procureTaskMapper, TaskConverter taskConverter) {
        this.procureTaskMapper = procureTaskMapper;
        this.taskConverter = taskConverter;
    }

    @Override
    public Task saveOrUpdate(Task task) {
        if(task == null || task.getId() == null) {
            return null;
        }
        boolean exists = procureTaskMapper.exists(Wrappers.<ProcureTask>lambdaQuery().eq(ProcureTask::getId, task.getId().getValue()));
        if(exists) {
            task.setUpdateTime(LocalDateTime.now());
        }else {
            task.setCreateTime(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());
        }
        boolean result = procureTaskMapper.insertOrUpdate(taskConverter.toProcureTask(task));
        if(!result) {
            throw new RuntimeException("operator database fail form task");
        }
        return task;
    }
}
