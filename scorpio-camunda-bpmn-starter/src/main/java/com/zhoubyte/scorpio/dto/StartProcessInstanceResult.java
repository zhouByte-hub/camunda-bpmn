package com.zhoubyte.scorpio.dto;

import java.util.Set;

public class StartProcessInstanceResult {

    private long processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private long processInstanceKey;
    private String tenantId;
    private Set<String> tags;


    public StartProcessInstanceResult() {
    }

    public StartProcessInstanceResult(long processDefinitionKey, String bpmnProcessId, Integer version, long processInstanceKey, String tenantId, Set<String> tags) {
        this.processDefinitionKey = processDefinitionKey;
        this.bpmnProcessId = bpmnProcessId;
        this.version = version;
        this.processInstanceKey = processInstanceKey;
        this.tenantId = tenantId;
        this.tags = tags;
    }

    public long getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(long processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBpmnProcessId() {
        return bpmnProcessId;
    }

    public void setBpmnProcessId(String bpmnProcessId) {
        this.bpmnProcessId = bpmnProcessId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public long getProcessInstanceKey() {
        return processInstanceKey;
    }

    public void setProcessInstanceKey(long processInstanceKey) {
        this.processInstanceKey = processInstanceKey;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
