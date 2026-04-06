package com.zhoubyte.procure_flow.application.service;

import com.zhoubyte.procure_flow.application.dto.CreateJobParam;
import com.zhoubyte.procure_flow.domain.model.Task;
import com.zhoubyte.procure_flow.domain.service.ProcureJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcureJobCreateService {

    private final ProcureJobService procureJobService;

    public void createJobAndRecordLog(CreateJobParam createJobParam) {
        Task task = procureJobService.persistenceTask(createJobParam);
        task.toProcess();
        if(!procureJobService.updateTask(task)) {
            throw new RuntimeException("任务持久化失败");
        }
        log.info("create task, task = {}", task);
    }


}
