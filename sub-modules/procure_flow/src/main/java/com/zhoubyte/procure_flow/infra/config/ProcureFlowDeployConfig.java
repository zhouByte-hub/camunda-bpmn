package com.zhoubyte.procure_flow.infra.config;

import com.zhoubyte.scorpio.dto.DeployResult;
import com.zhoubyte.scorpio.wrapper.ProcessService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProcureFlowDeployConfig {

    private final ProcessService processService;

    public ProcureFlowDeployConfig(ProcessService processService) {
        this.processService = processService;
    }

    @PostConstruct
    public void deploy() {
        try{
            log.info("Starting to deploy BPMN resource from classpath: bpmn/procure_process.bpmn");
            DeployResult deployResult = processService.deployResourceFromClassPath("bpmn/procure_process.bpmn");
            if(deployResult != null) {
                log.info("Successfully deployed BPMN resource, deployment key = {}, process count = {}", deployResult.getKey(), deployResult.getProcesses() != null ? deployResult.getProcesses().size() : 0);
                if(deployResult.getProcesses() != null && !deployResult.getProcesses().isEmpty()) {
                    deployResult.getProcesses().forEach(process ->
                            log.info("Deployed process: id={}, key={}, version={}", process.getBpmnProcessId(), process.getProcessDefinitionKey(), process.getVersion()));
                }
            } else {
                log.warn("Deploy result is null");
            }
        }catch (Exception e){
            log.error("Failed to deploy BPMN resource from classpath", e);
        }
    }
}
