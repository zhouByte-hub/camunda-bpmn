package com.zhoubyte.scorpio.dto;

public class MessagePublishResult {

    private Long messageKey;
    private String tenantId;

    public MessagePublishResult() {
    }

    public MessagePublishResult(Long messageKey, String tenantId) {
        this.messageKey = messageKey;
        this.tenantId = tenantId;
    }

    public Long getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(Long messageKey) {
        this.messageKey = messageKey;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
