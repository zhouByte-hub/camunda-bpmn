package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.spi.ProcessEngineProvider;

public class FormResourceService {

    private final ProcessEngineProvider processEngineProvider;

    public FormResourceService(ProcessEngineProvider processEngineProvider) {
        this.processEngineProvider = processEngineProvider;
    }
}
