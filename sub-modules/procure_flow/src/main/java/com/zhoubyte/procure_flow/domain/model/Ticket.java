package com.zhoubyte.procure_flow.domain.model;

import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketId;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketPriority;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketStatus;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Ticket {

    private TicketId ticketId;
    private String ticketName;
    private TicketType ticketType;
    private TicketPriority ticketPriority;
    private TicketStatus ticketStatus;

    private String ticketRemark;
    private Long expectedCompletionTime;
    private Boolean isNotify;
    private Integer preOverdueDays;
    private Integer postOverdueDays;
    private String creatorName;
    private String creatorId;
    private String updateId;
    private String updateName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 工单是否逾期
    public boolean isOverdue() {
        if (expectedCompletionTime == null) {
            return false;
        }
        return System.currentTimeMillis() >= expectedCompletionTime;
    }

    /**
     * 开启工单：Pending/WaitConfirm -> Process
     * @param operatorId 操作者 ID
     * @param operatorName 操作者名称
     */
    public void start(String operatorId, String operatorName) {
        if(!TicketStatus.PENDING.equals(ticketStatus) &&  !TicketStatus.WAIT_CONFIRM.equals(ticketStatus)) {
            throw new RuntimeException("当前工单的状态不是待受理或人工确认状态");
        }
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.ticketStatus = TicketStatus.PROCESSING;
    }

    /**
     * 处理工单：Process -> Resolved
     * @param operatorId 操作者 ID
     * @param operatorName 操作者名称
     */
    public void resolved(String operatorId, String operatorName) {
        if(!TicketStatus.PROCESSING.equals(ticketStatus)) {
            throw new RuntimeException("当前工单状态不是处理中");
        }
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.ticketStatus = TicketStatus.RESOLVED;
    }

    /**
     * 取消工单：Pending/Process/waitConfirm -> Cancel
     * @param operatorId 操作者 ID
     * @param operatorName 操作者名称
     */
    public void cancel(String operatorId, String operatorName) {
        if(!TicketStatus.PENDING.equals(ticketStatus) && !TicketStatus.PROCESSING.equals(ticketStatus) && !TicketStatus.WAIT_CONFIRM.equals(ticketStatus)) {
            throw new RuntimeException("当前工单状态为：" + ticketStatus.getName() + ", 不能取消");
        }
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.ticketStatus = TicketStatus.CANCELED;
    }

    /**
     * 关闭工单：Resolved/Cancel -> Close
     * @param operatorId  操作者 ID
     * @param operatorName 操作者名称
     */
    public void close(String operatorId, String operatorName) {
        if(!TicketStatus.RESOLVED.equals(ticketStatus) && !TicketStatus.CANCELED.equals(ticketStatus)) {
            throw new RuntimeException("当前工单状态为：" + ticketStatus.getName() + ", 关闭");
        }
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.ticketStatus = TicketStatus.CLOSED;
    }

    /**
     * 等待人工确认：Process -> WaitConfirm
     * @param operatorId 操作者 ID
     * @param operatorName 操作者名称
     */
    public void waitConfirm(String operatorId, String operatorName) {
        if(!TicketStatus.PROCESSING.equals(ticketStatus)) {
            throw new RuntimeException("当前工单的状态为：" +  ticketStatus.getName() + ", 无需人工确认");
        }
        this.updateId = operatorId;
        this.updateName = operatorName;
        this.ticketStatus = TicketStatus.WAIT_CONFIRM;
    }



}
