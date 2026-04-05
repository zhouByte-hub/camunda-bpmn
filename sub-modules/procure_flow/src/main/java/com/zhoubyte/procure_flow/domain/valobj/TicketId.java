package com.zhoubyte.procure_flow.domain.valobj;

import lombok.Data;

@Data
public class TicketId {

    private String value;

    private TicketId(String value){
        this.value=value;
    }

    public static TicketId form(String value) {
        return new TicketId(value);
    }

}
