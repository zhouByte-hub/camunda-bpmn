package com.zhoubyte.procure_flow.domain.valobj.ticket;

import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public enum TicketStatus {

    PENDING("PENDING", "待受理", "工单已创建，等待负责人接单处理"),
    PROCESSING("PROCESSING", "处理中", "已分配负责人，正在处理"),
    WAIT_CONFIRM("WAIT_CONFIRM", "待确认", "问题已处理完成，等待用户确认"),
    RESOLVED("RESOLVED", "已解决", "问题已解决，流程正常完成"),
    CLOSED("CLOSED", "已关闭", "工单已归档，不可再操作"),
    CANCELED("CANCELED", "已取消", "工单主动取消或作废");

    private final String value;
    private final String name;
    private final String description;

    TicketStatus(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    public static TicketStatus fromValue(String value) {
        if(!StringUtils.hasText(value)){
            return PENDING;
        }
        for (TicketStatus ticketStatus : TicketStatus.values()) {
            if (ticketStatus.value.equals(value)) {
                return ticketStatus;
            }
        }
        return PENDING; // 默认待受理
    }
}
