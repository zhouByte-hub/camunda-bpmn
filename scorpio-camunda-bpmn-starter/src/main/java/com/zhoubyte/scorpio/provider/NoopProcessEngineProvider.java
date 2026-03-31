package com.zhoubyte.scorpio.provider;

import com.zhoubyte.scorpio.dto.*;
import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Override
    public MessagePublishResult publishMessage(String messageName, String correlationKey, Map<String, Object> variables) {
        log.info("publish message from NoopProcessEngineProvider");
        return null;
    }

    @Override
    public List<ActivityMessageSubscription> searchActivityMessageSubscriptions(String messageName) {
        log.info("search activity message subscription from NoopProcessEngineProvider");
        return List.of();
    }

    @Override
    public List<CorrelationMessageSubscription> searchCorrelatedMessageSubscriptions(String messageName) {
        log.info("search correlated message subscription from NoopProcessEngineProvider");
        return List.of();
    }

    @Override
    public Boolean completeUserTask(Long taskKey, Map<String, Object> variables) {
        log.info("complete user task from NoopProcessEngineProvider");
        return null;
    }

    @Override
    public Optional<BpmnUserTask> searchUserTask(Long taskKey) {
        log.info("search user task from NoopProcessEngineProvider");
        return Optional.empty();
    }

    @Override
    public Optional<BpmnProcessDefinition> queryProcessDefinition(Long processDefinitionKey) {
        log.info("query process definition from NoopProcessEngineProvider");
        return Optional.empty();
    }
}
