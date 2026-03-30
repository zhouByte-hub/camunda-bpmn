package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.dto.BpmnUserTask;
import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class UserTaskService {

    private final Logger logger = LoggerFactory.getLogger(UserTaskService.class);
    private final ProcessEngineProvider processEngineProvider;


    public UserTaskService(ProcessEngineProvider processEngineProvider) {
        this.processEngineProvider = processEngineProvider;
        logger.info("UserTaskService init");
    }

    public Boolean completeUserTask(Long taskKey, Map<String, Object> variables) {
        return this.processEngineProvider.completeUserTask(taskKey, variables);
    }


    public Optional<BpmnUserTask> searchUserTask(Long userTaskKey) {
        return this.processEngineProvider.searchUserTask(userTaskKey);
    }
}
