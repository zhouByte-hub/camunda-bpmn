package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.dto.*;
import com.zhoubyte.scorpio.spi.EngineProviderRegistry;
import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 对外界暴露的操作类
 */
public class ProcessService {

    private final ElementInstanceService elementInstanceService;
    private final MessageService messageService;
    private final ProcessDefinitionService processDefinitionService;
    private final ProcessInstanceService processInstanceService;
    private final UserTaskService userTaskService;
    private final DeployResourceService deployResourceService;

    public ProcessService(EngineProviderRegistry engineProviderRegistry) {
        ProcessEngineProvider defaultProcessEngineProvider = engineProviderRegistry.getDefaultProcessEngineProvider();
        this.elementInstanceService = new ElementInstanceService(defaultProcessEngineProvider);
        this.messageService = new MessageService(defaultProcessEngineProvider);
        this.processDefinitionService = new ProcessDefinitionService(defaultProcessEngineProvider);
        this.processInstanceService = new ProcessInstanceService(defaultProcessEngineProvider);
        this.userTaskService = new UserTaskService(defaultProcessEngineProvider);
        this.deployResourceService = new DeployResourceService(defaultProcessEngineProvider);
    }


    public DeployResult deployResourceListFromClassPath(List<String> resourcePath) {
        if(resourcePath==null || resourcePath.isEmpty()){
            return new DeployResult();
        }
        return this.deployResourceService.deployResourceFromClassPath(resourcePath);
    }

    public DeployResult deployResourceFromClassPath(String resourcePath) {
        if(!StringUtils.hasText(resourcePath)){
            throw new IllegalArgumentException("resourcePath must not be empty");
        }
        return this.deployResourceService.deployResourceFromClassPath(resourcePath);
    }

    /**
     * 根据 ElementInstanceKey获取实例信息
     * @param elementInstanceKey key
     * @return 实例信息
     */
    public ElementInstanceResult queryElementInstanceFromKey(Long elementInstanceKey) {
        if(elementInstanceKey == null){
            return new ElementInstanceResult();
        }
        return this.elementInstanceService.queryElementInstanceFromKey(elementInstanceKey);
    }

    /**
     * 获取实例信息
     * @param pageRequest 分页信息
     * @param instanceQuery 过滤和排序信息
     * @return 实例列表
     */
    public List<ElementInstanceResult> executeElementInstanceSearchQuery(PageRequest pageRequest, ElementQuery instanceQuery) {
        return this.elementInstanceService.executeElementInstanceSearchQuery(pageRequest, instanceQuery);
    }


    /**
     * 发布消息
     * @param messageName 消息名称
     * @param correlationName 关联 Key
     * @param variables 参数信息
     * @return 发布结果
     */
    public MessagePublishResult publishMessage(String messageName, String correlationName, Map<String, Object> variables) {
        return this.messageService.publishMessage(messageName, correlationName, variables);
    }


    /**
     * 查询订阅了 MessageName但还未关联的订阅者信息
     * @param messageName 消息名称
     * @return 订阅者列表
     */
    public List<ActivityMessageSubscription> searchActivityMessageSubscription(String messageName) {
        return this.messageService.searchActivityMessageSubscription(messageName);
    }

    /**
     * 查询订阅了 MessageName且已经关联的订阅者信息
     * @param messageName 消息名称
     * @return 订阅者列表
     */
    public List<CorrelationMessageSubscription> searchCorrelationMessageSubscription(String messageName) {
        return this.messageService.searchCorrelationMessageSubscription(messageName);
    }


    /**
     * 完成人工任务
     * @param userTaskKey 人工任务 Key
     * @param variables 需要添加到流程实例的参数信息
     * @return 是否成功
     */
    public Boolean completeUserTask(Long userTaskKey, Map<String, Object> variables) {
        return this.userTaskService.completeUserTask(userTaskKey, variables);
    }

    /**
     * 根据人工任务 Key 获取任务信息
     * @param userTaskKey 人工任务 Key
     * @return 任务信息
     */
    public Optional<BpmnUserTask> searchUserTask(Long userTaskKey) {
        return this.userTaskService.searchUserTask(userTaskKey);
    }


    /**
     * 查询流程定义
     * @param processDefinitionKey 流程 Key
     * @return 定义信息
     */
    public Optional<BpmnProcessDefinition> queryProcessDefinition(Long processDefinitionKey) {
        return this.processDefinitionService.queryProcessDefinition(processDefinitionKey);
    }

    /**
     * 查询流程实例信息
     * @param processInstanceKey 流程实例 Key
     * @return 流程实例信息
     */
    public Optional<BpmnProcessInstance> queryProcessInstance(Long processInstanceKey) {
        return this.processInstanceService.queryProcessInstance(processInstanceKey);
    }

    /**
     * 取消流程实例
     * @param processInstanceKey 流程实例 Key
     * @return 是否取消成功
     */
    public Boolean cancelProcessInstance(Long processInstanceKey) {
        return this.processInstanceService.cancelProcessInstance(processInstanceKey);
    }

}
