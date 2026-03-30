package com.zhoubyte.scorpio.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public class BpmnUserTask {
    private Long userTaskKey;
    private String name;
    private String status;
    private String assignee;
    private String elementId;
    private Long elementInstanceKey;
    private List<String> candidateGroups;
    private List<String> candidateUsers;
    private String bpmnProcessId;
    private Long processDefinitionKey;
    private Long processInstanceKey;
    private Long formKey;
    private OffsetDateTime creationDate;
    private OffsetDateTime completionDate;
    private OffsetDateTime followUpdate;
    private OffsetDateTime dueDate;
    private String tenantId;
    private String externalFormReference;
    private Integer processDefinitionVersion;
    private Map<String, String> customHeaders;
    private Integer priority;

    public BpmnUserTask() {
    }

    public Long getUserTaskKey() {
        return userTaskKey;
    }

    public void setUserTaskKey(Long userTaskKey) {
        this.userTaskKey = userTaskKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public Long getElementInstanceKey() {
        return elementInstanceKey;
    }

    public void setElementInstanceKey(Long elementInstanceKey) {
        this.elementInstanceKey = elementInstanceKey;
    }

    public List<String> getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(List<String> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    public List<String> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(List<String> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public String getBpmnProcessId() {
        return bpmnProcessId;
    }

    public void setBpmnProcessId(String bpmnProcessId) {
        this.bpmnProcessId = bpmnProcessId;
    }

    public Long getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(Long processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public Long getProcessInstanceKey() {
        return processInstanceKey;
    }

    public void setProcessInstanceKey(Long processInstanceKey) {
        this.processInstanceKey = processInstanceKey;
    }

    public Long getFormKey() {
        return formKey;
    }

    public void setFormKey(Long formKey) {
        this.formKey = formKey;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(OffsetDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public OffsetDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(OffsetDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public OffsetDateTime getFollowUpdate() {
        return followUpdate;
    }

    public void setFollowUpdate(OffsetDateTime followUpdate) {
        this.followUpdate = followUpdate;
    }

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(OffsetDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getExternalFormReference() {
        return externalFormReference;
    }

    public void setExternalFormReference(String externalFormReference) {
        this.externalFormReference = externalFormReference;
    }

    public Integer getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public Map<String, String> getCustomHeaders() {
        return customHeaders;
    }

    public void setCustomHeaders(Map<String, String> customHeaders) {
        this.customHeaders = customHeaders;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
