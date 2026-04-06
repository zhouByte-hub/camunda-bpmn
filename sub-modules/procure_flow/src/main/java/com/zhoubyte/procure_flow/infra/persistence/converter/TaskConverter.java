package com.zhoubyte.procure_flow.infra.persistence.converter;

import com.zhoubyte.procure_flow.domain.model.Task;
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
}
