package com.zhoubyte.contract_flow.infra.persistence.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("contract_ticket")
@Data
public class ContractPO {
    
    @TableField("ticket_id")
    private String ticketId;
    
    @TableField("ticket_name")
    private String ticketName;
    
    @TableField("contract_number")
    private String contractNumber;
    
    @TableField("contract_type")
    private String contractType;
    
    @TableField("contract_amount")
    private Long contractAmount;
    
    @TableField("contract_status")
    private String contractStatus;
    
    @TableField("crm_customer_id")
    private String crmCustomerId;
    
    @TableField("contract_document_id")
    private String contractDocumentId;
    
    @TableField("signed_document_id")
    private String signedDocumentId;
    
    @TableField("ticket_remark")
    private String ticketRemark;
    
    @TableField("expected_completion_time")
    private Long expectedCompletionTime;
    
    @TableField("expiry_time")
    private Long expiryTime;
    
    @TableField("is_notify")
    private Boolean isNotify;
    
    @TableField("pre_overdue_days")
    private Integer preOverdueDays;
    
    @TableField("post_overdue_days")
    private Integer postOverdueDays;
    
    @TableField("creator_name")
    private String creatorName;
    
    @TableField("creator_id")
    private String creatorId;
    
    @TableField("update_id")
    private String updateId;
    
    @TableField("update_name")
    private String updateName;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    @TableField(exist = false)
    private String currentAssignee;
}
