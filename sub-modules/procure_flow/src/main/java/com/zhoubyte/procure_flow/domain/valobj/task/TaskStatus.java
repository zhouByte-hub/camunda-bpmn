package com.zhoubyte.procure_flow.domain.valobj.task;

import lombok.Getter;

@Getter
public enum TaskStatus {

    CREATE("CREATE", "已创建", "任务已创建，等待处理"),
    PROCESS("PROCESS", "处理中", "任务正在处理中"),
    COMPLETED("COMPLETED", "已完成", "任务已处理完成");

    private final String value;
    private final String name;
    private final String description;

    TaskStatus(String value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

}
