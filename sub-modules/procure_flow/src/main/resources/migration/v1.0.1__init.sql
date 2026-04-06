CREATE TABLE IF NOT EXISTS "procure_ticket" (
    "ticket_id" varchar(64) NOT NULL,
    "ticket_name" varchar(64) NOT NULL,
    "ticket_type" varchar(64) NOT NULL,
    "ticket_priority" varchar(32) NOT NULL,
    "ticket_status" varchar(16) NOT NULL,
    "ticket_remark" varchar(255),
    "expected_completion_time" int8 NOT NULL,
    "is_notify" bool DEFAULT false,
    "pre_overdue_days" int4,
    "post_overdue_days" int4,
    "creator_name" varchar(64) NOT NULL,
    "creator_id" varchar(64) NOT NULL,
    "update_name" varchar(64),
    "update_id" varchar(64),
    "create_time" timestamp NOT NULL,
    "update_time" timestamp NOT NULL,
    PRIMARY KEY ("ticket_id")
);

COMMENT ON COLUMN "procure_ticket"."ticket_id" IS '工单 ID';
COMMENT ON COLUMN "procure_ticket"."ticket_name" IS ' 工单名称';
COMMENT ON COLUMN "procure_ticket"."ticket_type" IS '工单类型';
COMMENT ON COLUMN "procure_ticket"."ticket_priority" IS '工单优先级';
COMMENT ON COLUMN "procure_ticket"."ticket_status" IS '工单状态';
COMMENT ON COLUMN "procure_ticket"."ticket_remark" IS '工单备注';
COMMENT ON COLUMN "procure_ticket"."expected_completion_time" IS '预计完成时间';
COMMENT ON COLUMN "procure_ticket"."is_notify" IS '是否提醒';
COMMENT ON COLUMN "procure_ticket"."pre_overdue_days" IS '到期前提醒天数';
COMMENT ON COLUMN "procure_ticket"."post_overdue_days" IS '到期后提醒天数';
COMMENT ON COLUMN "procure_ticket"."creator_name" IS '创建人名称';
COMMENT ON COLUMN "procure_ticket"."creator_id" IS '创建人 ID';
COMMENT ON COLUMN "procure_ticket"."create_time" IS '创建时间';
COMMENT ON COLUMN "procure_ticket"."update_time" IS '更新时间';



CREATE TABLE IF NOT EXISTS "procure_task" (
    "id" varchar(64) NOT NULL,
    "ticket_id" varchar(64) NOT NULL,
    "task_type" varchar(64) NOT NULL,
    "process_definition_version" int4 NOT NULL,
    "process_definition_key" int8 NOT NULL,
    "process_instance_id" varchar(64) NOT NULL,
    "process_instance_key" int8 NOT NULL,
    "element_id" varchar(64) NOT NULL,
    "element_instance_key" int8 NOT NULL,
    "task_status" varchar(32) NOT NULL,
    "retries" int4 NOT NULL DEFAULT 0,
    "variables" json,
    "create_time" timestamp NOT NULL,
    "update_time" timestamp NOT NULL,
    PRIMARY KEY ("id")
);

COMMENT ON COLUMN "procure_task"."id" IS 'id';
COMMENT ON COLUMN "procure_task"."ticket_id" IS '工单 ID';
COMMENT ON COLUMN "procure_task"."process_instance_id" IS '流程实例 Id';
COMMENT ON COLUMN "procure_task"."process_instance_key" IS '流程实例 Key';
COMMENT ON COLUMN "procure_task"."element_id" IS '流程元素 ID';
COMMENT ON COLUMN "procure_task"."element_instance_key" IS ' 流程元素 Key';
COMMENT ON COLUMN "procure_task"."task_status" IS '任务状态';
COMMENT ON COLUMN "procure_task"."create_time" IS '创建时间';
COMMENT ON COLUMN "procure_task"."update_time" IS '更新时间';