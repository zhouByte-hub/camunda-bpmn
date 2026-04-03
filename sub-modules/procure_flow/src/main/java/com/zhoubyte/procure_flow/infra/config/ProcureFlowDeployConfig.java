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
            DeployResult deployResult = processService.deployResourceFromClassPath("");
            if(deployResult != null) {
                log.info("deploy resource from classpath, processKey = {}", deployResult.getKey());
            }
        }catch (Exception e){
            log.error("deploy resource from classpath, processKey = {}", e.getMessage());
        }
    }
}
