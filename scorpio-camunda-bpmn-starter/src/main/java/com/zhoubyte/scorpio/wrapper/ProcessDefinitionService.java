package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.dto.BpmnProcessDefinition;
import com.zhoubyte.scorpio.spi.ProcessEngineProvider;

import java.util.Optional;
import java.util.logging.Logger;

public class ProcessDefinitionService {

    private final Logger log = Logger.getLogger(ProcessDefinitionService.class.getName());
    private final ProcessEngineProvider processEngineProvider;

    public ProcessDefinitionService(ProcessEngineProvider processEngineProvider) {
        this.processEngineProvider = processEngineProvider;
        log.info("ProcessEngineProvider init");
    }

    public Optional<BpmnProcessDefinition> queryProcessDefinition(Long processDefinitionKey) {
        log.info("queryProcessDefinition");
        if(processDefinitionKey==null){
            return Optional.empty();
        }
        return processEngineProvider.queryProcessDefinition(processDefinitionKey);
    }





}
