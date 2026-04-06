package com.zhoubyte.procure_flow.infra.persistence.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("procure_task")
public class ProcureTask {

    @TableField("id")
    private String id;                        // 任务 ID

    @TableField("ticket_id")
    private String ticketId;                // 对应的工单 ID

    @TableField("task_type")
    private String taskType;                // 任务类型

    @TableField("process_definition_version")
    private Integer processDefinitionVersion;   // 版本

    @TableField("process_definition_key")
    private Long processDefinitionKey;      // definitionKey

    @TableField("process_instance_id")
    private String processInstanceId;       // 流程实例 ID

    @TableField("process_instance_key")
    private Long processInstanceKey;        // 流程实例 Key

    @TableField("element_id")
    private String elementId;               // 元素 ID

    @TableField("element_instance_key")
    private Long elementInstanceKey;        // 元素实例 Key

    @TableField("task_status")
    private String taskStatus;              // 任务状态

    @TableField("retries")
    private Integer retries;                // 重试次数

    @TableField("variables")
    private String variables;               // 参数信息

    @TableField("create_time")
    private LocalDateTime createTime;       // 创建时间

    @TableField("update_time")
    private LocalDateTime updateTime;       // 创建时间
}
