package com.zhoubyte.contract_flow.domain.valobj.task;

import lombok.Value;

@Value
public class TaskId {
    String value;
    
    public static TaskId form(String value) {
        return new TaskId(value);
    }
}
