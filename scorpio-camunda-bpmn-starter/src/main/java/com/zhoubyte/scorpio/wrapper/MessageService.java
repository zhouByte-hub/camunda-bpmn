package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.spi.ProcessEngineProvider;

public class MessageService {

    private final ProcessEngineProvider processEngineProvider;

    public MessageService(ProcessEngineProvider processEngineProvider) {
        this.processEngineProvider = processEngineProvider;
    }
}
