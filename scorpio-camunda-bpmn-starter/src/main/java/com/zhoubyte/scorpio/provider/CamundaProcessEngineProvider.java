package com.zhoubyte.scorpio.provider;

import com.zhoubyte.scorpio.dto.*;
import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import io.camunda.client.CamundaClient;
import io.camunda.client.api.command.CompleteUserTaskCommandStep1;
import io.camunda.client.api.command.DeployResourceCommandStep1;
import io.camunda.client.api.command.PublishMessageCommandStep1;
import io.camunda.client.api.response.DeploymentEvent;
import io.camunda.client.api.response.PublishMessageResponse;
import io.camunda.client.api.search.enums.ElementInstanceState;
import io.camunda.client.api.search.enums.ElementInstanceType;
import io.camunda.client.api.search.filter.ElementInstanceFilter;
import io.camunda.client.api.search.request.ElementInstanceSearchRequest;
import io.camunda.client.api.search.response.ElementInstance;
import io.camunda.client.api.search.response.UserTask;
import io.camunda.client.api.search.sort.ElementInstanceSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CamundaProcessEngineProvider implements ProcessEngineProvider {

    private final Logger log = LoggerFactory.getLogger(CamundaProcessEngineProvider.class);
    private final CamundaClient camundaClient;

    public CamundaProcessEngineProvider(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
        log.info("CamundaProcessEngineProvider created");
    }

    @Override
    public String engineName() {
        return "camunda";
    }

    @Override
    public DeployResult deployResource(List<String> resourcePaths) {
        log.info("Deploy resource from CamundaProcessEngineProvider");
        if(resourcePaths==null || resourcePaths.isEmpty()){
            throw new IllegalArgumentException("resourcePaths must not be empty");
        }
        DeployResourceCommandStep1.DeployResourceCommandStep2 deployResourceCommandStep2
                = camundaClient.newDeployResourceCommand().addResourceFromClasspath(resourcePaths.getFirst());
        resourcePaths.stream().skip(1).forEach(deployResourceCommandStep2::addResourceFromClasspath);
        DeploymentEvent event = deployResourceCommandStep2.send().join();

        DeployResult deployResult = new DeployResult();
        // 填充 Process信息
        List<DeployResult.DeployProcess> deployProcessList = event.getProcesses().stream()
                .map(process -> new DeployResult.DeployProcess(process.getProcessDefinitionKey(), process.getBpmnProcessId(), process.getVersion(), process.getResourceName(), process.getTenantId()))
                .toList();
        deployResult.setProcesses(deployProcessList);

        // 填充 Form 信息
        List<DeployResult.DeployForm> formList = event.getForm().stream()
                .map(form -> new DeployResult.DeployForm(form.getFormId(), form.getVersion(), form.getFormKey(), form.getResourceName(), form.getTenantId()))
                .toList();
        deployResult.setForm(formList);

        // 填充 Decision信息
        List<DeployResult.DeployDecision> decisionList = event.getDecisions().stream()
                .map(decision -> new DeployResult.DeployDecision(decision.getDmnDecisionId(), decision.getDmnDecisionName(), decision.getVersion(), decision.getDecisionKey(), decision.getDmnDecisionRequirementsId(), decision.getDecisionRequirementsKey(), deployResult.getTenantId()))
                .toList();
        deployResult.setDecisions(decisionList);

        deployResult.setKey(event.getKey());
        deployResult.setTenantId(event.getTenantId());
        return deployResult;
    }

    @Override
    public ElementInstanceResult queryElementInstanceFromKey(Long elementInstanceKey) {
        log.info("query element instance from CamundaProcessEngineProvider");
        ElementInstance instance = camundaClient.newElementInstanceGetRequest(elementInstanceKey).send().join();
        return this.instanceToResult(instance);
    }

    @Override
    public List<ElementInstanceResult> executeElementInstanceSearchQuery(PageRequest pageRequest, ElementQuery instanceQuery) {
        log.info("execute element instance search query from CamundaProcessEngineProvider");
        ElementInstanceSearchRequest req = camundaClient.newElementInstanceSearchRequest();
        if (pageRequest != null) {
            req.page(p -> p.from(pageRequest.getFrom())
                    .limit(pageRequest.getLimit())
                    .before(pageRequest.getCursorBefore())
                    .after(pageRequest.getCursorAfter()));
        }
        if (instanceQuery != null) {
            Map<ElementQuery.ElementInstance, ElementQuery.Sort> sort = instanceQuery.getSort();
            if (sort != null) {
                req.sort(s -> sort.forEach((key, value) -> {
                    ElementInstanceSort instanceSort = toSort(s, key);
                    if (ElementQuery.Sort.ASC.equals(value)) {
                        instanceSort.asc();
                    } else
                        instanceSort.desc();
                }));
            }
            Map<ElementQuery.ElementInstance, String> filter = instanceQuery.getFilter();
            if (filter != null) {
                req.filter(f -> filter.forEach((key, value) -> setFilter(f, key, value)));
            }
        }
        return req.send().join().items().stream().map(this::instanceToResult).toList();
    }


    private ElementInstanceResult instanceToResult(ElementInstance instance) {
        ElementInstanceResult elementInstanceResult = new ElementInstanceResult();
        elementInstanceResult.setElementInstanceKey(instance.getElementInstanceKey());
        elementInstanceResult.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        elementInstanceResult.setProcessDefinitionId(instance.getProcessDefinitionId());
        elementInstanceResult.setProcessInstanceKey(instance.getProcessInstanceKey());
        elementInstanceResult.setElementId(instance.getElementId());
        elementInstanceResult.setElementName(instance.getElementName());
        elementInstanceResult.setStartDate(instance.getStartDate());
        elementInstanceResult.setEndDate(instance.getEndDate());
        elementInstanceResult.setIncident(instance.getIncident());
        elementInstanceResult.setIncidentKey(instance.getIncidentKey());
        elementInstanceResult.setState(instance.getState().name());
        elementInstanceResult.setTenantId(instance.getTenantId());
        elementInstanceResult.setType(instance.getType().name());
        return elementInstanceResult;
    }

    private ElementInstanceSort toSort(ElementInstanceSort s, ElementQuery.ElementInstance key) {
        return switch (key) {
            case ELEMENT_INSTANCE_KEY -> s.elementInstanceKey();
            case PROCESS_INSTANCE_KEY -> s.processInstanceKey();
            case PROCESS_DEFINITION_ID -> s.processDefinitionId();
            case PROCESS_DEFINITION_KEY -> s.processDefinitionKey();
            case START_DATE -> s.startDate();
            case END_DATE -> s.endDate();
            case ELEMENT_ID -> s.elementId();
            case ELEMENT_NAME -> s.elementName();
            case TYPE -> s.type();
            case STATE -> s.state();
            case INCIDENT_KEY -> s.incidentKey();
            case TENANT_ID -> s.tenantId();
        };
    }

    private void setFilter(ElementInstanceFilter f, ElementQuery.ElementInstance key, String value) {
        switch (key) {
            case ELEMENT_INSTANCE_KEY -> f.elementInstanceKey(Long.parseLong(value));
            case PROCESS_INSTANCE_KEY -> f.processInstanceKey(Long.parseLong(value));
            case PROCESS_DEFINITION_ID -> f.processDefinitionId(value);
            case PROCESS_DEFINITION_KEY -> f.processDefinitionKey(Long.parseLong(value));
            case START_DATE -> f.startDate(OffsetDateTime.parse(value));
            case END_DATE -> f.endDate(OffsetDateTime.parse(value));
            case ELEMENT_ID -> f.elementId(value);
            case ELEMENT_NAME -> f.elementName(value);
            case TYPE -> f.type(ElementInstanceType.valueOf(value));
            case STATE -> f.state(ElementInstanceState.valueOf(value));
            case INCIDENT_KEY -> f.incidentKey(Long.parseLong(value));
            case TENANT_ID -> f.tenantId(value);
        }
    }



    @Override
    public MessagePublishResult publishMessage(String messageName, String correlationKey, Map<String, Object> variables) {
        if(!StringUtils.hasText(messageName)) {
            throw new IllegalArgumentException("messageName must not be empty");
        }
        if(!StringUtils.hasText(correlationKey)) {
            throw new IllegalArgumentException("correlationKey must not be empty");
        }
        PublishMessageCommandStep1.PublishMessageCommandStep3 publishMessageCommandStep3 = camundaClient.newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(correlationKey);
        if(variables != null) {
            publishMessageCommandStep3.variables(variables);
        }
        PublishMessageResponse response = publishMessageCommandStep3.send().join();
        return new MessagePublishResult(response.getMessageKey(), response.getTenantId());
    }

    @Override
    public List<ActivityMessageSubscription> searchActivityMessageSubscriptions(String messageName) {
        if(!StringUtils.hasText(messageName)) {
            throw new IllegalArgumentException("messageName must not be empty");
        }
        return camundaClient.newMessageSubscriptionSearchRequest()
                .filter(f -> f.messageName(messageName))
                .send()
                .join()
                .items().stream().map(item -> {
                    ActivityMessageSubscription activityMessageSubscription = new ActivityMessageSubscription();
                    activityMessageSubscription.setMessageSubscriptionKey(item.getMessageSubscriptionKey());
                    activityMessageSubscription.setProcessDefinitionId(item.getProcessDefinitionId());
                    activityMessageSubscription.setProcessDefinitionKey(item.getProcessDefinitionKey());
                    activityMessageSubscription.setElementInstanceKey(item.getElementInstanceKey());
                    activityMessageSubscription.setElementId(item.getElementId());
                    activityMessageSubscription.setElementInstanceKey(item.getElementInstanceKey());
                    activityMessageSubscription.setMessageSubscriptionState(item.getMessageSubscriptionState().name());
                    activityMessageSubscription.setLastUpdatedDate(item.getLastUpdatedDate());
                    activityMessageSubscription.setMessageName(item.getMessageName());
                    activityMessageSubscription.setCorrelationKey(item.getCorrelationKey());
                    activityMessageSubscription.setTenantId(item.getTenantId());
                    return activityMessageSubscription;
                }).toList();
    }

    @Override
    public List<CorrelationMessageSubscription> searchCorrelatedMessageSubscriptions(String messageName) {
        if(!StringUtils.hasText(messageName)) {
            throw new IllegalArgumentException("messageName must not be empty");
        }
        return camundaClient.newCorrelatedMessageSubscriptionSearchRequest()
                .filter(f -> f.messageName(messageName))
                .send().join()
                .items().stream().map(item -> {
                    CorrelationMessageSubscription correlationMessageSubscription = new CorrelationMessageSubscription();
                    correlationMessageSubscription.setCorrelationKey(item.getCorrelationKey());
                    correlationMessageSubscription.setCorrelationTime(item.getCorrelationTime());
                    correlationMessageSubscription.setElementId(item.getElementId());
                    correlationMessageSubscription.setElementInstanceKey(item.getElementInstanceKey());
                    correlationMessageSubscription.setMessageKey(item.getMessageKey());
                    correlationMessageSubscription.setMessageName(item.getMessageName());
                    correlationMessageSubscription.setPartitionId(item.getPartitionId());
                    correlationMessageSubscription.setProcessDefinitionId(item.getProcessDefinitionId());
                    correlationMessageSubscription.setProcessInstanceKey(item.getProcessInstanceKey());
                    correlationMessageSubscription.setProcessInstanceKey(item.getProcessInstanceKey());
                    correlationMessageSubscription.setSubscriptionKey(item.getSubscriptionKey());
                    correlationMessageSubscription.setTenantId(item.getTenantId());
                    return correlationMessageSubscription;
                }).toList();
    }


    @Override
    public Boolean completeUserTask(Long taskKey, Map<String, Object> variables) {
        if(taskKey == null) {
            throw new IllegalArgumentException("taskKey must not be empty");
        }
        try{
            CompleteUserTaskCommandStep1 completeUserTaskCommandStep1 = camundaClient.newCompleteUserTaskCommand(taskKey);
            if(variables != null) {
                completeUserTaskCommandStep1.variables(variables);
            }
            completeUserTaskCommandStep1.send().join();
        } catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Optional<BpmnUserTask> searchUserTask(Long taskKey) {
        if(taskKey == null) {
            throw new IllegalArgumentException("taskKey must not be empty");
        }
        UserTask task = camundaClient.newUserTaskGetRequest(taskKey).send().join();
        if(task != null) {
            BpmnUserTask bpmnUserTask = new BpmnUserTask();
            bpmnUserTask.setUserTaskKey(task.getUserTaskKey());
            bpmnUserTask.setName(task.getName());
            bpmnUserTask.setStatus(task.getState().name());
            bpmnUserTask.setAssignee(task.getAssignee());
            bpmnUserTask.setElementId(task.getElementId());
            bpmnUserTask.setElementInstanceKey(task.getElementInstanceKey());
            bpmnUserTask.setCandidateUsers(task.getCandidateUsers());
            bpmnUserTask.setCandidateGroups(task.getCandidateGroups());
            bpmnUserTask.setBpmnProcessId(task.getBpmnProcessId());
            bpmnUserTask.setProcessDefinitionKey(task.getProcessDefinitionKey());
            bpmnUserTask.setProcessInstanceKey(task.getProcessInstanceKey());
            bpmnUserTask.setFormKey(task.getFormKey());
            bpmnUserTask.setCreationDate(task.getCreationDate());
            bpmnUserTask.setCompletionDate(task.getCompletionDate());
            bpmnUserTask.setFollowUpdate(task.getFollowUpDate());
            bpmnUserTask.setDueDate(task.getDueDate());
            bpmnUserTask.setTenantId(task.getTenantId());
            bpmnUserTask.setExternalFormReference(task.getExternalFormReference());
            bpmnUserTask.setProcessDefinitionVersion(task.getProcessDefinitionVersion());
            bpmnUserTask.setCustomHeaders(task.getCustomHeaders());
            bpmnUserTask.setPriority(task.getPriority());
            return Optional.of(bpmnUserTask);
        }
        return Optional.empty();
    }
}
