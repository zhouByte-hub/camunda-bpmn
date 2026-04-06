package com.zhoubyte.procure_flow.domain.valobj.task;

import lombok.Data;

@Data
public class TaskId {

    private String value;

    private TaskId(String value) {
        this.value = value;
    }

    public static TaskId form(String value) {
        return new TaskId(value);
    }
}
