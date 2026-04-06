package com.zhoubyte.procure_flow.domain.valobj.ticket;

import lombok.Getter;

@Getter
public enum TicketType {

    PROCURE_TICKET("procure_ticket");

    private final String value;

    TicketType(String value){
        this.value = value;
    }
}
