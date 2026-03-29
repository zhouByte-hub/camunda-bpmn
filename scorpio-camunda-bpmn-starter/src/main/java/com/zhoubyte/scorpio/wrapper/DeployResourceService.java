package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import com.zhoubyte.scorpio.support.DeployResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DeployResourceService {

    private final Logger logger = LoggerFactory.getLogger(DeployResourceService.class);
    private final ProcessEngineProvider processEngineProvider;

    public DeployResourceService(ProcessEngineProvider processEngineProvider) {
        this.processEngineProvider = processEngineProvider;
        logger.info("DeployResourceService init");
    }

    public DeployResult deployResourceFromClassPath(List<String> resourceList) {
        return processEngineProvider.deployResource(resourceList);
    }

    public DeployResult deployResourceFromClassPath(String resourcePath) {
        return this.deployResourceFromClassPath(List.of(resourcePath));
    }
}
