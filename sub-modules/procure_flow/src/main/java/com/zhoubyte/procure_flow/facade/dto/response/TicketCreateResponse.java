package com.zhoubyte.procure_flow.facade.dto.response;

import com.zhoubyte.procure_flow.domain.valobj.TicketId;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class TicketCreateResponse {

    private Integer code;
    private TicketId ticketId;
    private String msg;

    private TicketCreateResponse(Integer code, TicketId ticketId, String msg) {
        this.code = code;
        this.ticketId = ticketId;
        this.msg = msg;
    }

    public static TicketCreateResponse success(TicketId ticketId) {
        return new TicketCreateResponse(HttpStatus.OK.value(), ticketId, "success");
    }

    public static TicketCreateResponse fail(String msg) {
        return new TicketCreateResponse(HttpStatus.BAD_REQUEST.value(), null, msg);
    }


}
