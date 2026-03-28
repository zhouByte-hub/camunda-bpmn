package com.zhoubyte.scorpio.autoconfigure;

import com.zhoubyte.scorpio.provider.CamundaProcessEngineProvider;
import com.zhoubyte.scorpio.provider.NoopProcessEngineProvider;
import com.zhoubyte.scorpio.spi.EngineProviderRegistry;
import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import com.zhoubyte.scorpio.wrapper.ProcessService;
import io.camunda.client.CamundaClient;
import io.camunda.client.spring.configuration.CamundaAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration(
        after = {CamundaAutoConfiguration.class}
)
@ConditionalOnClass(CamundaClient.class)
@EnableConfigurationProperties({ScorpioCamundaEngineProperties.class})
public class CamundaEngineAutoConfigure {


    @Bean
    @ConditionalOnProperty(name = "scorpio.enabled", value = "true")
    @ConditionalOnProperty(name = "scorpio.default-process-engine-name", value = "camunda")
    @ConditionalOnMissingBean(CamundaProcessEngineProvider.class)
    public ProcessEngineProvider camundaEngineProvider(CamundaClient camundaClient) {
        return new CamundaProcessEngineProvider(camundaClient);
    }

    @Bean
    @ConditionalOnProperty(name = "scorpio.enabled", value = "false", matchIfMissing = true)
    @ConditionalOnMissingBean(NoopProcessEngineProvider.class)
    public ProcessEngineProvider noopProcessEngineProvider() {
        return new NoopProcessEngineProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ProcessEngineProvider.class)
    public EngineProviderRegistry engineProviderRegistry(
            List<ProcessEngineProvider> providerList,
            ScorpioCamundaEngineProperties properties) throws IllegalAccessException {
        return new EngineProviderRegistry(providerList, properties);
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(EngineProviderRegistry.class)
    public ProcessService processService(EngineProviderRegistry engineProviderRegistry) {
        return new ProcessService(engineProviderRegistry);
    }
}
