package com.zhoubyte.procure_flow.infra.persistence.converter;

import com.zhoubyte.procure_flow.domain.model.Task;
import com.zhoubyte.procure_flow.domain.valobj.task.TaskId;
import com.zhoubyte.procure_flow.domain.valobj.task.TaskStatus;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketId;
import com.zhoubyte.procure_flow.infra.persistence.po.ProcureTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TaskConverter {

    @Mappings({
            @Mapping(source = "id.value", target = "id"),
            @Mapping(source = "ticketId.value", target = "ticketId"),
            @Mapping(source = "taskStatus.value", target = "taskStatus")
    })
    ProcureTask toProcureTask(Task task);

    @Mappings({
            @Mapping(target = "id", expression = "java(com.zhoubyte.procure_flow.domain.valobj.task.TaskId.form(procureTask.getId()))"),
            @Mapping(target = "ticketId", expression = "java(com.zhoubyte.procure_flow.domain.valobj.ticket.TicketId.form(procureTask.getTicketId()))"),
            @Mapping(target = "taskStatus", expression = "java(com.zhoubyte.procure_flow.domain.valobj.task.TaskStatus.fromValue(procureTask.getTaskStatus()))")
    })
    Task toTask(ProcureTask procureTask);
}
