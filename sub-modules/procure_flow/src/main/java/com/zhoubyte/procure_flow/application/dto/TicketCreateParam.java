package com.zhoubyte.procure_flow.application.dto;

import lombok.Data;

@Data
public class TicketCreateParam {

    private String ticketName;
    private String ticketPriority;
    private String ticketRemark;
    private Long expectedCompletionTime;
    private Boolean isNotify;
    private Integer preOverdueDays;
    private Integer postOverdueDays;
    private String creatorName;
    private String creatorId;

}
