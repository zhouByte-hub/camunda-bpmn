package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.spi.ProcessEngineProvider;

import java.util.logging.Logger;

public class ProcessDefinitionService {

    private final Logger log = Logger.getLogger(ProcessDefinitionService.class.getName());
    private final ProcessEngineProvider processEngineProvider;

    public ProcessDefinitionService(ProcessEngineProvider processEngineProvider) {
        this.processEngineProvider = processEngineProvider;
        log.info("ProcessEngineProvider init");
    }






}
