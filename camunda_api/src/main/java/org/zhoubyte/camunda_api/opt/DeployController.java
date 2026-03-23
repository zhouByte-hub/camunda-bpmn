package org.zhoubyte.camunda_api.opt;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.DeploymentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/deploy")
public class DeployController {

    private final CamundaClient camundaClient;

    public DeployController(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }

    @GetMapping(value = "/resource")
    public DeploymentEvent deploy(){
        return camundaClient.newDeployResourceCommand()
                .addResourceFile("camunda.yml")
                .send()
                .join();
    }
}
