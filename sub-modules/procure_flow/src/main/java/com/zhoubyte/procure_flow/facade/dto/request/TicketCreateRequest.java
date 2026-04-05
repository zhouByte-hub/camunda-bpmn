package com.zhoubyte.procure_flow.facade.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketCreateRequest {

    @NotNull(message = "ticket_name 不能为空")
    private String ticketName;

    @NotNull(message = "ticket_priority 不能为空")
    private String ticketPriority;

    private String ticketRemark;

    @NotNull(message = "expected_completion_time 不能为空")
    private Long expectedCompletionTime;
    private Boolean isNotify;
    private Integer preOverdueDays;
    private Integer postOverdueDays;

}
