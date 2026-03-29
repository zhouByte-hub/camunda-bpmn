package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.spi.ProcessEngineProvider;

public class UserTaskService {

    private final ProcessEngineProvider processEngineProvider;


    public UserTaskService(ProcessEngineProvider processEngineProvider) {
        this.processEngineProvider = processEngineProvider;
    }
}
