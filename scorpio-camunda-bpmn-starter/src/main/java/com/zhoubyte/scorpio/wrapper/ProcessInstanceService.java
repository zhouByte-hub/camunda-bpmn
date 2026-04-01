package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.dto.BpmnProcessInstance;
import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ProcessInstanceService {

    private final Logger logger = LoggerFactory.getLogger(ProcessInstanceService.class);
    private final ProcessEngineProvider processEngineProvider;

    public ProcessInstanceService(ProcessEngineProvider processEngineProvider) {
        this.processEngineProvider = processEngineProvider;
        logger.info("ProcessInstanceService init");
    }

    public Boolean cancelProcessInstance(Long processInstanceKey) {
        logger.info("cancelProcessInstance, processInstanceKey:{}", processInstanceKey);
        if(processInstanceKey == null){
            return false;
        }
        return processEngineProvider.cancelProcessInstance(processInstanceKey);
    }

    public Optional<BpmnProcessInstance> queryProcessInstance(Long processInstanceKey) {
        logger.info("queryProcessInstance, processInstanceKey:{}", processInstanceKey);
        if(processInstanceKey == null){
            return Optional.empty();
        }
        return processEngineProvider.queryProcessInstance(processInstanceKey);
    }


}
