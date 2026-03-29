package org.zhoubyte.camunda_api.element;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.search.response.ElementInstance;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/element_instance")
public class ElementInstanceController {

    private final CamundaClient camundaClient;

    public ElementInstanceController(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }

    @GetMapping(value = "/{elementInstanceKey}:query")
    public ElementInstance queryElementInstanceFromKey(@PathVariable("elementInstanceKey") Long elementInstanceKey) {
        return camundaClient.newElementInstanceGetRequest(elementInstanceKey).send().join();
    }


    @GetMapping(value = "/search")
    public List<ElementInstance> queryElementInstanceSearchRequest() {
        return camundaClient.newElementInstanceSearchRequest()
                .page(p -> p.from(0).limit(10))
                .filter(f -> f.elementId("project_leader_approval"))
                .sort(s -> s.elementId().desc())
                .send().join().items();
    }
}
