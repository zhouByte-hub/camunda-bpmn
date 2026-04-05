package com.zhoubyte.procure_flow.infra.persistence.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("procure_ticket")
@Data
public class ProcureTicket {

    @TableField("ticket_id")
    private String ticketId;                    // 工单 ID

    @TableField("ticket_name")
    private String ticketName;                  // 工单名称

    @TableField("ticket_type")
    private String ticketType;                  // 工单类型

    @TableField("ticket_priority")
    private String ticketPriority;              // 工单优先级

    @TableField("ticket_status")
    private String ticketStatus;               // 工单状态

    @TableField("ticket_remark")
    private String ticketRemark;                // 工单备注

    @TableField("expected_completion_time")
    private Long expectedCompletionTime;        // 预期完成时间（时间戳）

    @TableField("is_notify")
    private Boolean isNotify;                   // 是否预期提醒

    @TableField("pre_overdue_days")
    private Integer preOverdueDays;             // 到期前提醒天数

    @TableField("post_overdue_days")
    private Integer postOverdueDays;            // 到期后提醒天数

    @TableField("creator_name")
    private String creatorName;                 // 创建者名称

    @TableField("creator_id")
    private String creatorId;                   // 创建者 ID

    @TableField("update_id")
    private String updateId;                    // 修改者 ID

    @TableField("update_name")
    private String updateName;                  // 修改者名称

    @TableField("create_time")
    private LocalDateTime createTime;           // 创建时间

    @TableField("update_time")
    private LocalDateTime updateTime;           // 更新时间
}
