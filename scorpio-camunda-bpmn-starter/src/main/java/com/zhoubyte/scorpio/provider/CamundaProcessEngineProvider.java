package com.zhoubyte.scorpio.provider;

import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import io.camunda.client.CamundaClient;

public class CamundaProcessEngineProvider implements ProcessEngineProvider {

    private final CamundaClient camundaClient;

    public CamundaProcessEngineProvider(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }

    @Override
    public String engineName() {
        return "camunda";
    }

}
