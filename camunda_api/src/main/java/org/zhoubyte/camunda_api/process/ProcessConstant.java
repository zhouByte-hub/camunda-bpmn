package org.zhoubyte.camunda_api.process;

public interface ProcessConstant {

    // 流程名称
    String PROCESS_INSTANCE_ID = "purchase_server_process";

    // 流程实例属性
    String MONEY = "money";
    String OVERDUE_TIMER_CYCLE = "overdue_timer_cycle";
    String RESPONSIBILITY = "responsibility";

    String TASK_REVOKE = "task_revoke";

    // 角色信息
    String PROJECT_LEADER_APPROVAL = "project_leader_approval";
    String DEPARTMENT_MANAGER_APPROVAL = "department_manager_approval";
    String CEO_APPROVAL = "ceo_approval";
    String FINANCIAL_APPROVAL = "financial_approval";



}
