package com.zhoubyte.scorpio.dto;

public class BpmnProcessDefinition {

    private Long processDefinitionKey;
    private String name;
    private String resourceName;
    private Integer version;
    private String versionTag;
    private String processDefinitionId;
    private String tenantId;

    public BpmnProcessDefinition() {
    }

    public BpmnProcessDefinition(Long processDefinitionKey, String name, String resourceName, Integer version, String versionTag, String processDefinitionId, String tenantId) {
        this.processDefinitionKey = processDefinitionKey;
        this.name = name;
        this.resourceName = resourceName;
        this.version = version;
        this.versionTag = versionTag;
        this.processDefinitionId = processDefinitionId;
        this.tenantId = tenantId;
    }

    public Long getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(Long processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getVersionTag() {
        return versionTag;
    }

    public void setVersionTag(String versionTag) {
        this.versionTag = versionTag;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
