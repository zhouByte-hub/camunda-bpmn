package com.zhoubyte.scorpio.dto;

import java.time.OffsetDateTime;

public class CorrelationMessageSubscription {

    private String correlationKey;
    private OffsetDateTime correlationTime;
    private String elementId;
    private Long elementInstanceKey;
    private Long messageKey;
    private String messageName;
    private Integer partitionId;
    private String processDefinitionId;
    private Long processDefinitionKey;
    private Long processInstanceKey;
    private Long subscriptionKey;
    private String tenantId;

    public CorrelationMessageSubscription() {
    }

    public String getCorrelationKey() {
        return correlationKey;
    }

    public void setCorrelationKey(String correlationKey) {
        this.correlationKey = correlationKey;
    }

    public OffsetDateTime getCorrelationTime() {
        return correlationTime;
    }

    public void setCorrelationTime(OffsetDateTime correlationTime) {
        this.correlationTime = correlationTime;
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

    public Long getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(Long messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public Integer getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(Integer partitionId) {
        this.partitionId = partitionId;
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

    public Long getSubscriptionKey() {
        return subscriptionKey;
    }

    public void setSubscriptionKey(Long subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
