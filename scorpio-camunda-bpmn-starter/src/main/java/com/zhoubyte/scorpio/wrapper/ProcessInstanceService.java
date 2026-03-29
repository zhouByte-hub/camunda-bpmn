package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.spi.ProcessEngineProvider;

public class ProcessInstanceService {

    private final ProcessEngineProvider processEngineProvider;

    public ProcessInstanceService(ProcessEngineProvider processEngineProvider) {
        this.processEngineProvider = processEngineProvider;
    }
}
