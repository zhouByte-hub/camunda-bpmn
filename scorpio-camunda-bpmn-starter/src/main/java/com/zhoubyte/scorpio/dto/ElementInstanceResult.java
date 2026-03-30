package com.zhoubyte.scorpio.dto;

import java.time.OffsetDateTime;

public class ElementInstanceResult {

    private Long elementInstanceKey;
    private Long processDefinitionKey;
    private String processDefinitionId;
    private Long processInstanceKey;
    private String elementId;
    private String elementName;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Boolean incident;
    private Long incidentKey;
    private String state;
    private String tenantId;
    private String type;

    public ElementInstanceResult() {
    }

    public Long getElementInstanceKey() {
        return elementInstanceKey;
    }

    public void setElementInstanceKey(Long elementInstanceKey) {
        this.elementInstanceKey = elementInstanceKey;
    }

    public Long getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(Long processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public Long getProcessInstanceKey() {
        return processInstanceKey;
    }

    public void setProcessInstanceKey(Long processInstanceKey) {
        this.processInstanceKey = processInstanceKey;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
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

    public Boolean getIncident() {
        return incident;
    }

    public void setIncident(Boolean incident) {
        this.incident = incident;
    }

    public Long getIncidentKey() {
        return incidentKey;
    }

    public void setIncidentKey(Long incidentKey) {
        this.incidentKey = incidentKey;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
