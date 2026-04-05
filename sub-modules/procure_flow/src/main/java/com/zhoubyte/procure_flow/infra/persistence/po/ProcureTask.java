package com.zhoubyte.procure_flow.infra.persistence.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("procure_task")
public class ProcureTask {

    @TableField("id")
    private Long id;                    // 任务 ID

    @TableField("ticket_id")
    private String ticketId;            // 对应的工单 ID

    @TableField("process_instance_id")
    private Long processInstanceId;     // 流程实例 ID

    @TableField("process_instance_key")
    private String processInstanceKey;  // 流程实例 Key

    @TableField("element_id")
    private Long elementId;             // 元素 ID

    @TableField("element_instance_key")
    private String elementInstanceKey;  // 元素实例 Key

    @TableField("task_status")
    private String taskStatus;          // 任务状态

    @TableField("create_time")
    private LocalDateTime createTime;   // 创建时间

    @TableField("update_time")
    private LocalDateTime updateTime;   // 创建时间
}
