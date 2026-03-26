package org.zhoubyte.camunda_api.process;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.ProcessInstanceEvent;
import io.camunda.client.api.search.response.UserTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.zhoubyte.camunda_api.util.TimerCycleUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/process")
@Slf4j
public class ProcessController {

    private final CamundaClient camundaClient;

    public ProcessController(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }

    // 创建流程实例
    @GetMapping(value = "/start")
    public Map<String, Object> startProcess(@RequestParam(value = "money") Integer money, @RequestParam(value = "overdue", required = false) Integer overdue) {
        // 在流程实例初始化的时候添加流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(ProcessConstant.MONEY, money);
        if(overdue == null) {
            variables.put(ProcessConstant.OVERDUE_TIMER_CYCLE, TimerCycleUtil.never());
        }else{
            variables.put(ProcessConstant.OVERDUE_TIMER_CYCLE, TimerCycleUtil.ofDays(overdue, 1));
        }
        log.info("Process start， variables: {}", variables);

        // 创建并启动流程实例
        ProcessInstanceEvent processInstanceEvent = camundaClient.newCreateInstanceCommand()
                .bpmnProcessId(ProcessConstant.PROCESS_INSTANCE_ID)
                .latestVersion()
                .variables(variables)
                .send()
                .join();
        Map<String, Object> result = new HashMap<>();
        result.put("processInstance",  processInstanceEvent);

        // 会获取到空列表，因为具体的任务还未创建
        List<UserTask> items = camundaClient.newUserTaskSearchRequest()
                .filter(f -> f.processInstanceKey(processInstanceEvent.getProcessInstanceKey()))
                .send()
                .join().items();
        result.put("elements", items);
        return result;
    }


    // 结束流程实例
    @GetMapping(value = "/end")
    public String endProcess(@RequestParam("process_instance_id") Long processInstanceId) {
        camundaClient.newCancelInstanceCommand(processInstanceId).send().join();
        return "success";
    }


    /**
     * 1. 完成用户任务，因为用户任务需要人工介入，所以需要显示的调用 complete操作结束用户任务，流程才能继续向下执行。
     * 2. 任务中的返回值都会向上抛到流程实例中成为流程实例的变量。
     * @param taskKey 用户任务唯一标识
     * @param variables 任务参数
     */
    @PostMapping(value = "/userTask/{task_key}:complete")
    public Map<String, Object> completeUserTask(@PathVariable("task_key") Long taskKey, @RequestBody Map<String, Object> variables) {
        log.info("completing user task {} with variables {}", taskKey, variables);
        camundaClient.newCompleteUserTaskCommand(taskKey).variables(variables).send().join();
        UserTask userTask = camundaClient.newUserTaskGetRequest(taskKey).send().join();

        Object auditResult = variables.getOrDefault("audit_result", false);
        Map<String, Object> result = new HashMap<>();
        result.put(userTask.getName(), auditResult);
        result.put("audit_result", auditResult);
        return result;
    }
}
