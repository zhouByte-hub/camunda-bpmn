package com.zhoubyte.procure_flow.domain.model;

import com.zhoubyte.procure_flow.domain.valobj.task.TaskId;
import com.zhoubyte.procure_flow.domain.valobj.task.TaskStatus;
import com.zhoubyte.procure_flow.domain.valobj.ticket.TicketId;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Task {

    private TaskId id;
    private Long taskKey;
    private TicketId ticketId;
    private String taskType;
    private Integer processDefinitionVersion;
    private Long processDefinitionKey;
    private String processInstanceId;
    private Long processInstanceKey;
    private String elementId;
    private Long elementInstanceKey;
    private TaskStatus taskStatus;
    private Integer retries;
    private String variables;
    private String candidateGroups;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    /**
     * 处理任务：Create -> Process
     */
    public void toProcess() {
        if(!TaskStatus.CREATE.equals(taskStatus)){
            throw new RuntimeException(taskStatus.getDescription());
        }
        this.taskStatus = TaskStatus.PROCESS;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 完成任务：Process -> Complete
     */
    public void toComplete() {
        if(!TaskStatus.PROCESS.equals(taskStatus)){
            throw new RuntimeException(taskStatus.getDescription());
        }
        this.taskStatus = TaskStatus.COMPLETED;
        this.updateTime = LocalDateTime.now();
    }

}
