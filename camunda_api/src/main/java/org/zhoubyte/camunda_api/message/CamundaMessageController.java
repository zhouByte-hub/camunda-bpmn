package org.zhoubyte.camunda_api.message;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.CorrelateMessageResponse;
import io.camunda.client.api.response.PublishMessageResponse;
import io.camunda.client.api.search.response.CorrelatedMessageSubscription;
import io.camunda.client.api.search.response.MessageSubscription;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/message")
public class CamundaMessageController {

    private final CamundaClient camundaClient;

    public CamundaMessageController(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }

    /**
     * 发布消息
     * 用于向 Camunda 发布消息，触发等待该消息的流程实例
     * 
     * @return 消息发布响应
     */
    @GetMapping(value = "/publish")
    public PublishMessageResponse publishMessage() {
        return camundaClient.newPublishMessageCommand()
                .messageName("MESSAGE_NAME")           // 消息名称
                .correlationKey("CORRELATION_KEY")     // 关联键，用于匹配流程实例
                .send()   // 异步发送请求
                .join();  // 阻塞等待结果
    }

    /**
     * 搜索消息订阅（PENDING状态）
     * 查询当前活动的消息订阅，即哪些流程实例正在等待特定消息
     * 
     * 【核心概念】：
     * - 查询对象：正在等待中的消息订阅
     * - 订阅状态：PENDING（等待被关联）
     * - 流程状态：流程阻塞，等待消息触发
     * - 时间维度：当前时刻的活动订阅
     * 
     * 【与 searchCorrelatedMessageSubscriptions 的区别】：
     * ┌─────────────────────────────────────────────────────────────┐
     * │ 本方法：查询 PENDING 状态（正在等待）                        │
     * │ 对方方法：查询 CORRELATED 状态（已关联完成）                 │
     * ├─────────────────────────────────────────────────────────────┤
     * │ 本方法：流程阻塞中，等待外部消息                             │
     * │ 对方方法：流程已继续执行，消息已处理                         │
     * ├─────────────────────────────────────────────────────────────┤
     * │ 本方法：用于监控、调试，查看当前等待队列                     │
     * │ 对方方法：用于审计、追踪，查看历史处理记录                   │
     * ├─────────────────────────────────────────────────────────────┤
     * │ 本方法：返回的消息订阅可以被关联                             │
     * │ 对方方法：返回的消息订阅已处理完成                           │
     * └─────────────────────────────────────────────────────────────┘
     * 
     * 【生命周期】：
     * 流程到达消息事件 → 创建订阅(PENDING) → 【本方法可查询】
     * → 消息关联 → 订阅变为CORRELATED → 【对方方法可查询】
     * 
     * 使用场景：
     * 1. 监控流程实例的消息等待状态
     * 2. 查看有多少流程实例在等待特定消息
     * 3. 调试消息事件流程，确认流程是否正确等待消息
     * 
     * @param messageName 消息名称（可选）
     * @return 消息订阅列表（PENDING状态）
     */
    @GetMapping(value = "/subscriptions")
    public List<MessageSubscription> searchMessageSubscriptions(
            @RequestParam(value = "messageName", required = false) String messageName) {
        
        var request = camundaClient.newMessageSubscriptionSearchRequest();
        
        // 如果指定了消息名称，则过滤
        if (messageName != null && !messageName.isEmpty()) {
            request.filter(f -> f.messageName(messageName));
        }

        return request.send()
                .join()
                .items();
    }

    /**
     * 关联消息到流程实例
     * 将消息关联到正在等待该消息的流程实例，并立即触发流程继续执行
     * 
     * 与 publishMessage 的区别：
     * - publishMessage: 发布消息，可能有多个订阅者接收
     * - correlateMessage: 精确关联到特定的流程实例，一对一关系
     * 
     * 使用场景：
     * 1. 精确控制消息关联到特定流程实例
     * 2. 携带变量更新流程状态
     * 3. 触发消息开始事件或消息中间事件
     * 
     * @param messageName     消息名称
     * @param correlationKey  关联键
     * @param variables       携带的流程变量（可选）
     * @return 消息关联响应
     */
    @GetMapping(value = "/correlate")
    public CorrelateMessageResponse correlateMessage(
            @RequestParam(value = "messageName") String messageName,
            @RequestParam(value = "correlationKey") String correlationKey,
            @RequestParam(value = "variables", required = false) Map<String, Object> variables) {
        
        var command = camundaClient.newCorrelateMessageCommand()
                .messageName(messageName)           // 消息名称
                .correlationKey(correlationKey);    // 关联键
        
        // 如果提供了变量，则添加到命令中
        if (variables != null && !variables.isEmpty()) {
            command.variables(variables);
        }
        return command.send()
                .join();
    }

    /**
     * 搜索已关联的消息订阅（CORRELATED状态）
     * 查询已经被关联的消息订阅，用于追踪消息处理历史
     * 
     * 【核心概念】：
     * - 查询对象：已经被处理的消息订阅
     * - 订阅状态：CORRELATED（已关联完成）
     * - 流程状态：流程已继续执行，不再是等待状态
     * - 时间维度：历史记录，用于追踪和审计
     * 
     * 【与 searchMessageSubscriptions 的区别】：
     * ┌─────────────────────────────────────────────────────────────┐
     * │ 本方法：查询 CORRELATED 状态（已关联完成）                   │
     * │ 对方方法：查询 PENDING 状态（正在等待）                      │
     * ├─────────────────────────────────────────────────────────────┤
     * │ 本方法：流程已继续执行，消息已处理                           │
     * │ 对方方法：流程阻塞中，等待外部消息                           │
     * ├─────────────────────────────────────────────────────────────┤
     * │ 本方法：用于审计、追踪，查看历史处理记录                     │
     * │ 对方方法：用于监控、调试，查看当前等待队列                   │
     * ├─────────────────────────────────────────────────────────────┤
     * │ 本方法：返回的消息订阅已处理完成                             │
     * │ 对方方法：返回的消息订阅可以被关联                           │
     * └─────────────────────────────────────────────────────────────┘
     * 
     * 【生命周期】：
     * 流程到达消息事件 → 创建订阅(PENDING) → 【对方方法可查询】
     * → 消息关联 → 订阅变为CORRELATED → 【本方法可查询】
     * 
     * 使用场景：
     * 1. 审计追踪：查看哪些消息已经被处理
     * 2. 故障排查：确认消息是否成功关联
     * 3. 监控统计：分析消息处理情况
     * 
     * @param messageName 消息名称（可选）
     * @return 已关联的消息订阅列表（CORRELATED状态）
     */
    @GetMapping(value = "/correlated-subscriptions")
    public List<CorrelatedMessageSubscription> searchCorrelatedMessageSubscriptions(
            @RequestParam(value = "messageName", required = false) String messageName) {
        
        var request = camundaClient.newCorrelatedMessageSubscriptionSearchRequest();
        
        // 如果指定了消息名称，则过滤
        if (messageName != null && !messageName.isEmpty()) {
            request.filter(f -> f.messageName(messageName));
        }
        
        return request.send()
                .join()
                .items();
    }
}
