package org.zhoubyte.camunda_api.opt;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.DeploymentEvent;
import io.camunda.client.api.search.response.Form;
import io.camunda.client.api.search.response.ProcessDefinition;
import io.camunda.client.api.search.response.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/deploy")
@Slf4j
public class DeployController {

    private final CamundaClient camundaClient;

    public DeployController(CamundaClient camundaClient,
                            @Value("${camunda.client.rest-address}") String restAddress,
                            @Value("${camunda.client.auth.username}") String username,
                            @Value("${camunda.client.auth.password}") String password) {
        log.info("In DeployController, rest-address = {}, username = {}, password = {}", restAddress, username, password);
        this.camundaClient = camundaClient;
    }

    // 部署流程
    @GetMapping(value = "/bpmn_resource")
    public DeploymentEvent deployBpmnResource() {
        return camundaClient.newDeployResourceCommand()
                .addResourceFromClasspath("bpmn/purchase_server_process.bpmn")
                .send()
                .join();
    }

    // 部署 Form
    @GetMapping(value = "/form_resource")
    public DeploymentEvent deployFormResource() {
        return camundaClient.newDeployResourceCommand()
                .addResourceFromClasspath("bpmn/apply_form.form")
                .send()
                .join();
    }

    // 调用 API 删除部署的流程会失败，可以使用 Operate UI提供的删除操作
//    @DeleteMapping(value = "/{resourceKey}/resource:delete")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteResource(@PathVariable("resourceKey") Long resourceKey) {
//        camundaClient.newDeleteResourceCommand(resourceKey)
//                .send()
//                .join();
//    }


    // 查看所有流程和form信息
    @GetMapping(value = "/mapper")
    public Map<String, Object> mapper() {
        Map<String, Object> result = new HashMap<>();
        SearchResponse<ProcessDefinition> searchResponse = camundaClient.newProcessDefinitionSearchRequest().send().join();
        searchResponse.items().forEach(item -> {
            Form form = camundaClient.newProcessDefinitionGetFormRequest(item.getProcessDefinitionKey()).send().join();
            Map<String, Object> map = new HashMap<>();
            if(form != null) {
                if(form.getFormId() != null) {
                    map.put("formId", form.getFormId());
                }
//                map.put("formKey", form.getFormKey());
            }
            Map<String, Object> temp = new HashMap<>();
            temp.put("process", item);
            temp.put("form", map);
            result.put(item.getResourceName(), temp);
        });
        return result;
    }
}