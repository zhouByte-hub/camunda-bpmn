package com.zhoubyte.scorpio.wrapper;

import com.zhoubyte.scorpio.spi.EngineProviderRegistry;
import com.zhoubyte.scorpio.spi.ProcessEngineProvider;
import com.zhoubyte.scorpio.support.DeployResult;
import com.zhoubyte.scorpio.support.ElementInstanceResult;
import com.zhoubyte.scorpio.support.ElementQuery;
import com.zhoubyte.scorpio.support.PageRequest;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 对外界暴露的操作类
 */
public class ProcessService {

    private final ElementInstanceService elementInstanceService;
    private final FormResourceService formResourceService;
    private final MessageService messageService;
    private final ProcessDefinitionService processDefinitionService;
    private final ProcessInstanceService processInstanceService;
    private final UserTaskService userTaskService;
    private final DeployResourceService deployResourceService;

    public ProcessService(EngineProviderRegistry engineProviderRegistry) {
        ProcessEngineProvider defaultProcessEngineProvider = engineProviderRegistry.getDefaultProcessEngineProvider();
        this.elementInstanceService = new ElementInstanceService(defaultProcessEngineProvider);
        this.formResourceService = new FormResourceService(defaultProcessEngineProvider);
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


}
