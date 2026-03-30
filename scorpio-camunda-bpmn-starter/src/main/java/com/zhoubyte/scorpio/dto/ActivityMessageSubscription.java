package com.zhoubyte.scorpio.dto;

import java.time.OffsetDateTime;

public class ActivityMessageSubscription {

    private Long messageSubscriptionKey;
    private String processDefinitionId;
    private Long processDefinitionKey;
    private Long processInstanceKey;
    private String elementId;
    private Long elementInstanceKey;
    private String messageSubscriptionState;
    private OffsetDateTime lastUpdatedDate;
    private String messageName;
    private String correlationKey;
    private String tenantId;

    public ActivityMessageSubscription() {
    }

    public ActivityMessageSubscription(Long messageSubscriptionKey, String processDefinitionId, Long processDefinitionKey, Long processInstanceKey,
                                       String elementId, Long elementInstanceKey, String messageSubscriptionState, OffsetDateTime lastUpdatedDate,
                                       String messageName, String correlationKey, String tenantId) {
        this.messageSubscriptionKey = messageSubscriptionKey;
        this.processDefinitionId = processDefinitionId;
        this.processDefinitionKey = processDefinitionKey;
        this.processInstanceKey = processInstanceKey;
        this.elementId = elementId;
        this.elementInstanceKey = elementInstanceKey;
        this.messageSubscriptionState = messageSubscriptionState;
        this.lastUpdatedDate = lastUpdatedDate;
        this.messageName = messageName;
        this.correlationKey = correlationKey;
        this.tenantId = tenantId;
    }


    public Long getMessageSubscriptionKey() {
        return messageSubscriptionKey;
    }

    public void setMessageSubscriptionKey(Long messageSubscriptionKey) {
        this.messageSubscriptionKey = messageSubscriptionKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
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

    public String getMessageSubscriptionState() {
        return messageSubscriptionState;
    }

    public void setMessageSubscriptionState(String messageSubscriptionState) {
        this.messageSubscriptionState = messageSubscriptionState;
    }

    public OffsetDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(OffsetDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getCorrelationKey() {
        return correlationKey;
    }

    public void setCorrelationKey(String correlationKey) {
        this.correlationKey = correlationKey;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
