package com.zhoubyte.procure_flow.domain.valobj;

import lombok.Getter;
import org.flywaydb.core.internal.util.StringUtils;

@Getter
public enum TicketPriority {

    CRITICAL("critical"),
    High("high"),
    Medium("medium"),
    Low("low");

    private final String value;

    TicketPriority(String value) {
        this.value = value;
    }


    public static TicketPriority fromValue(String value) {
        if(!StringUtils.hasText(value)){
            return CRITICAL;
        }
        for (TicketPriority ticketPriority : TicketPriority.values()) {
            if (ticketPriority.value.equals(value)) {
                return ticketPriority;
            }
        }
        return CRITICAL; // 默认紧急
    }
}
