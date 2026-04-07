package com.zhoubyte.contract_flow.domain.valobj.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatus {
    
    CREATE("CREATE", "已创建"),
    PROCESS("PROCESS", "处理中"),
    COMPLETED("COMPLETED", "已完成"),
    FAILED("FAILED", "失败");
    
    private final String value;
    private final String description;
    
    public static TaskStatus fromValue(String value) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown TaskStatus value: " + value);
    }
}
