package com.zhoubyte.procure_flow.facade.endpoint;

import com.zhoubyte.procure_flow.application.dto.TicketCreateParam;
import com.zhoubyte.procure_flow.application.service.ProcureCreateTicketService;
import com.zhoubyte.procure_flow.domain.service.UserService;
import com.zhoubyte.procure_flow.facade.converter.ProcureTicketFacadeConverter;
import com.zhoubyte.procure_flow.facade.dto.request.TicketCreateRequest;
import com.zhoubyte.procure_flow.facade.dto.response.TicketCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/ticket")
@RequiredArgsConstructor
public class ProcureFlowTicketController {

    private final ProcureCreateTicketService ticketService;
    private final UserService userService;  // 模拟登录的用户信息
    private final ProcureTicketFacadeConverter procureTicketFacadeConverter;

    @PostMapping(value = "/create")
    public TicketCreateResponse createTicket(@RequestBody @Validated TicketCreateRequest ticketCreateRequest){
        TicketCreateParam createParam = procureTicketFacadeConverter.toCreateParam(
                ticketCreateRequest,
                userService.getCurrentUserId(),
                userService.getCurrentUsername());
        TicketCreateResponse response;
        try{
            response = TicketCreateResponse.success(ticketService.createTicket(createParam));
        }catch (Exception e){
            return TicketCreateResponse.fail(e.getMessage());
        }
        return response;
    }
}
