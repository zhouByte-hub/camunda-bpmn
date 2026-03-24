package org.zhoubyte.camunda_api.opt;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.DeleteResourceResponse;
import io.camunda.client.api.response.DeploymentEvent;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/deploy")
public class DeployController {

    private final CamundaClient camundaClient;

    public DeployController(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }

    @GetMapping(value = "/bpmn_resource")
    public DeploymentEvent deployBpmnResource() {
        return camundaClient.newDeployResourceCommand()
                .addResourceFromClasspath("bpmn/purchase_server_process.bpmn")
                .send()
                .join();
    }


    @GetMapping(value = "/form_resource")
    public DeploymentEvent deployFormResource() {
        return camundaClient.newDeployResourceCommand()
                .addResourceFromClasspath("bpmn/apply_form.form")
                .send()
                .join();
    }


    @DeleteMapping(value = "/{resourceKey}/resource:delete")
    public DeleteResourceResponse deleteResource(@PathVariable("resourceKey") Long resourceKey) {
         return camundaClient.newDeleteResourceCommand(resourceKey)
                .send()
                .join();
    }
}
