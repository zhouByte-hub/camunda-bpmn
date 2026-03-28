package com.zhoubyte.scorpio.provider;

import com.zhoubyte.scorpio.spi.ProcessEngineProvider;

public class NoopProcessEngineProvider implements ProcessEngineProvider {

    @Override
    public String engineName() {
        return "noop";
    }

}
