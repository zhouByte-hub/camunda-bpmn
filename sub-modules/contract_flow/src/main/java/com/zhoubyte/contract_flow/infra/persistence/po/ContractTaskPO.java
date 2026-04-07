package com.zhoubyte.contract_flow.infra.persistence.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("contract_task")
@Data
public class ContractTaskPO {
    
    @TableField("id")
    private String id;
    
    @TableField("ticket_id")
    private String ticketId;
    
    @TableField("task_type")
    private String taskType;
    
    @TableField("process_definition_version")
    private Integer processDefinitionVersion;
    
    @TableField("process_definition_key")
    private Long processDefinitionKey;
    
    @TableField("process_instance_id")
    private String processInstanceId;
    
    @TableField("process_instance_key")
    private Long processInstanceKey;
    
    @TableField("element_id")
    private String elementId;
    
    @TableField("element_instance_key")
    private Long elementInstanceKey;
    
    @TableField("task_status")
    private String taskStatus;
    
    @TableField("retries")
    private Integer retries;
    
    @TableField("variables")
    private String variables;
    
    @TableField("task_key")
    private Long taskKey;
    
    @TableField("candidate_groups")
    private String candidateGroups;
    
    @TableField("assignee")
    private String assignee;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
}
