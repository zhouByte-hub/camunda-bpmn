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
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


    @GetMapping(value = "/process_definition")
    public List<ProcessDefinition> bpmnProcessDefinition(){
        return camundaClient.newProcessDefinitionSearchRequest().send().join().items();
    }

    // 部署流程
    @GetMapping(value = "/bpmn_resource")
    public Optional<DeploymentEvent> deployBpmnResource() {
        // 查【流程定义】是否存在，而非查流程实例
//        boolean alreadyDeployed = !camundaClient.newProcessDefinitionSearchRequest()
//                .filter(f -> f.processDefinitionId("purchase_server_process"))
//                .send()
//                .join()
//                .items().isEmpty();
//        if (alreadyDeployed) {
//            log.info("流程定义已存在，跳过部署");
//            return Optional.empty();
//        }
        DeploymentEvent join = camundaClient.newDeployResourceCommand()
                .addResourceFromClasspath("bpmn/purchase_server_process.bpmn")
                .addResourceFromClasspath("bpmn/apply_form.form")
                .addResourceFromClasspath("bpmn/confirm_audit_form.form")
                .send()
                .join();
        return Optional.of(join);
    }

    // 调用 API 删除部署的流程会失败
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