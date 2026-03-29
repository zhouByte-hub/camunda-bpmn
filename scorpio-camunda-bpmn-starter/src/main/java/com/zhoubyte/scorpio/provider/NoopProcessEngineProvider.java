package com.zhoubyte.scorpio.provider;

import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import com.zhoubyte.scorpio.support.DeployResult;
import com.zhoubyte.scorpio.support.ElementInstanceResult;
import com.zhoubyte.scorpio.support.ElementQuery;
import com.zhoubyte.scorpio.support.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NoopProcessEngineProvider implements ProcessEngineProvider {

    private final Logger log = LoggerFactory.getLogger(NoopProcessEngineProvider.class);

    public NoopProcessEngineProvider() {
        log.info("NoopProcessEngineProvider created");
    }

    @Override
    public String engineName() {
        return "noop";
    }

    @Override
    public DeployResult deployResource(List<String> resourcePaths) {
        log.info("Deploy resource from NoopProcessEngineProvider");
        return new DeployResult();
    }

    @Override
    public ElementInstanceResult queryElementInstanceFromKey(Long elementInstanceKey) {
        log.info("query element instance from NoopProcessEngineProvider");
        return new ElementInstanceResult();
    }

    @Override
    public List<ElementInstanceResult> executeElementInstanceSearchQuery(PageRequest pageRequest, ElementQuery instanceQuery) {
        log.info("execute element instance search query from NoopProcessEngineProvider");
        return List.of();
    }

}
