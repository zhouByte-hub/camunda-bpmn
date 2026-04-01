package com.zhoubyte.scorpio.dto;

import java.time.OffsetDateTime;
import java.util.Set;

public class BpmnProcessInstance {

    private Long processInstanceKey;
    private String processDefinitionId;
    private String processDefinitionName;
    private Integer processDefinitionVersion;
    private String processDefinitionVersionTag;
    private Long processDefinitionKey;
    private Long parentElementInstanceKey;
    private Long parentProcessInstanceKey;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String state;
    private Boolean hasIncident;
    private String tenantId;
    private Set<String> tags;


    public BpmnProcessInstance() {
    }

    public Long getProcessInstanceKey() {
        return processInstanceKey;
    }

    public void setProcessInstanceKey(Long processInstanceKey) {
        this.processInstanceKey = processInstanceKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public Integer getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getProcessDefinitionVersionTag() {
        return processDefinitionVersionTag;
    }

    public void setProcessDefinitionVersionTag(String processDefinitionVersionTag) {
        this.processDefinitionVersionTag = processDefinitionVersionTag;
    }

    public Long getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(Long processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public Long getParentElementInstanceKey() {
        return parentElementInstanceKey;
    }

    public void setParentElementInstanceKey(Long parentElementInstanceKey) {
        this.parentElementInstanceKey = parentElementInstanceKey;
    }

    public Long getParentProcessInstanceKey() {
        return parentProcessInstanceKey;
    }

    public void setParentProcessInstanceKey(Long parentProcessInstanceKey) {
        this.parentProcessInstanceKey = parentProcessInstanceKey;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getHasIncident() {
        return hasIncident;
    }

    public void setHasIncident(Boolean hasIncident) {
        this.hasIncident = hasIncident;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
