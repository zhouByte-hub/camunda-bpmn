package org.zhoubyte.camunda_api.opt;

import io.camunda.client.CamundaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/topology")
public class Topology {

    private final CamundaClient camundaClient;

    public Topology(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }


    /**
     * 查询 Camunda 集群的拓扑信息。
     * <p>
     * {@code newTopologyRequest()} 用于创建一个拓扑查询请求，
     * 该请求会向 Camunda Gateway 发起查询，返回当前集群中所有
     * Broker 节点的状态、分区分布及 Gateway 版本等信息。
     * {@code send()} 异步发送请求，{@code join()} 阻塞等待结果返回。
     * </p>
     *
     * @return 集群拓扑信息，包含 Broker 列表、分区及版本详情
     */
    @GetMapping(value = "/all")
    public io.camunda.client.api.response.Topology showTopology() {
        return camundaClient.newTopologyRequest().send().join();
    }
}
