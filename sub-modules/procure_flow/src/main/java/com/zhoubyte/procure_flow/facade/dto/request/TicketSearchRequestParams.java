package com.zhoubyte.procure_flow.facade.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketSearchRequestParams extends PageParams{

    private String ticketName;
    private String creatorName;

}
