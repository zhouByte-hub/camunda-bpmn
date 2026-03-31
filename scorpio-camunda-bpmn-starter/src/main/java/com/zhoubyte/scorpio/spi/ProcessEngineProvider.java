package com.zhoubyte.scorpio.spi;


import com.zhoubyte.scorpio.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// 流程引擎提供者
public interface ProcessEngineProvider {

    String engineName();

    DeployResult deployResource(List<String> resourcePaths);

    ElementInstanceResult queryElementInstanceFromKey(Long elementInstanceKey);

    List<ElementInstanceResult> executeElementInstanceSearchQuery(PageRequest pageRequest, ElementQuery instanceQuery);

    MessagePublishResult publishMessage(String messageName, String correlationKey, Map<String, Object> variables);

    List<ActivityMessageSubscription> searchActivityMessageSubscriptions(String messageName);

    List<CorrelationMessageSubscription> searchCorrelatedMessageSubscriptions(String messageName);

    Boolean completeUserTask(Long taskKey, Map<String, Object> variables);

    Optional<BpmnUserTask> searchUserTask(Long taskKey);

    Optional<BpmnProcessDefinition> queryProcessDefinition(Long processDefinitionKey);



}
