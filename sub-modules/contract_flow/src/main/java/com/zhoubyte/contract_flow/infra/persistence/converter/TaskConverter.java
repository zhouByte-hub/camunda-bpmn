package com.zhoubyte.contract_flow.infra.persistence.converter;

import com.zhoubyte.contract_flow.domain.model.Task;
import com.zhoubyte.contract_flow.domain.valobj.task.TaskId;
import com.zhoubyte.contract_flow.domain.valobj.task.TaskStatus;
import com.zhoubyte.contract_flow.domain.valobj.contract.ContractId;
import com.zhoubyte.contract_flow.infra.persistence.po.ContractTaskPO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskConverter {
    
    default Task toDomain(ContractTaskPO po) {
        if (po == null) {
            return null;
        }
        
        return Task.builder()
                .id(TaskId.form(po.getId()))
                .taskKey(po.getTaskKey())
                .contractId(ContractId.form(po.getTicketId()))
                .taskType(po.getTaskType())
                .processDefinitionVersion(po.getProcessDefinitionVersion())
                .processDefinitionKey(po.getProcessDefinitionKey())
                .processInstanceId(po.getProcessInstanceId())
                .processInstanceKey(po.getProcessInstanceKey())
                .elementId(po.getElementId())
                .elementInstanceKey(po.getElementInstanceKey())
                .taskStatus(TaskStatus.fromValue(po.getTaskStatus()))
                .retries(po.getRetries())
                .variables(po.getVariables())
                .candidateGroups(po.getCandidateGroups())
                .assignee(po.getAssignee())
                .createTime(po.getCreateTime())
                .updateTime(po.getUpdateTime())
                .build();
    }
    
    default ContractTaskPO toPO(Task task) {
        if (task == null) {
            return null;
        }
        
        ContractTaskPO po = new ContractTaskPO();
        po.setId(task.getId().getValue());
        po.setTaskKey(task.getTaskKey());
        po.setTicketId(task.getContractId().getValue());
        po.setTaskType(task.getTaskType());
        po.setProcessDefinitionVersion(task.getProcessDefinitionVersion());
        po.setProcessDefinitionKey(task.getProcessDefinitionKey());
        po.setProcessInstanceId(task.getProcessInstanceId());
        po.setProcessInstanceKey(task.getProcessInstanceKey());
        po.setElementId(task.getElementId());
        po.setElementInstanceKey(task.getElementInstanceKey());
        po.setTaskStatus(task.getTaskStatus().getValue());
        po.setRetries(task.getRetries());
        po.setVariables(task.getVariables());
        po.setCandidateGroups(task.getCandidateGroups());
        po.setAssignee(task.getAssignee());
        po.setCreateTime(task.getCreateTime());
        po.setUpdateTime(task.getUpdateTime());
        
        return po;
    }
}
