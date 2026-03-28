package com.zhoubyte.scorpio.spi;

import com.zhoubyte.scorpio.autoconfigure.ScorpioCamundaEngineProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// 流程引擎提供者注册器
public class EngineProviderRegistry {

    private static final Logger log = LoggerFactory.getLogger(EngineProviderRegistry.class);
    private final String defaultProcessEngineName;
    private final Map<String, ProcessEngineProvider> providersMap;

    public EngineProviderRegistry(List<ProcessEngineProvider> providers, ScorpioCamundaEngineProperties properties) throws IllegalAccessException {
        log.info("init EngineProviderRegistry");
        Map<String, ProcessEngineProvider> collect = providers.stream().collect(Collectors.toMap(ProcessEngineProvider::engineName, provider -> provider));
        providersMap = Collections.unmodifiableMap(collect);

        ProcessEngineProvider defaultProcessEngineProvider = providersMap.get(properties.getDefaultProcessEngineName());
        if (defaultProcessEngineProvider == null) {
            throw new IllegalAccessException("scorpio.defaultProcessEngineName not found");
        }
        this.defaultProcessEngineName = defaultProcessEngineProvider.engineName();
    }


    public String getDefaultProcessEngineName() {
        return defaultProcessEngineName;
    }


    public Map<String, ProcessEngineProvider> getProvidersMap() {
        return providersMap;
    }

}
