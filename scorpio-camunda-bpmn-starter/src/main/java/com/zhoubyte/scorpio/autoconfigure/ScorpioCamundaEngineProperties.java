package com.zhoubyte.scorpio.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scorpio")
public class ScorpioCamundaEngineProperties {

    private String defaultProcessEngineName;
    private Boolean enabled;

    public ScorpioCamundaEngineProperties() {
        this.defaultProcessEngineName = "camunda";
        this.enabled = true;
    }

    public ScorpioCamundaEngineProperties(String defaultProcessEngineName, Boolean enabled) {
        this.defaultProcessEngineName = defaultProcessEngineName;
        this.enabled = enabled;
    }

    public String getDefaultProcessEngineName() {
        return defaultProcessEngineName;
    }

    public void setDefaultProcessEngineName(String defaultProcessEngineName) {
        this.defaultProcessEngineName = defaultProcessEngineName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
