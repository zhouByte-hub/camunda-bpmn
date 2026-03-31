package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessInstanceService {

    private final Logger logger = LoggerFactory.getLogger(ProcessInstanceService.class);
    private final ProcessEngineProvider processEngineProvider;

    public ProcessInstanceService(ProcessEngineProvider processEngineProvider) {
        this.processEngineProvider = processEngineProvider;
    }
}
