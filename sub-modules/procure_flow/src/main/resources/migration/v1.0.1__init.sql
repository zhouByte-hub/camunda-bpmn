CREATE TABLE " tickets" (
    "ticket_id" varchar(255) NOT NULL,
    "ticket_name" varchar(255) NOT NULL,
    "ticket_type" varchar(64) NOT NULL,
    "ticket_priority" varchar(32) NOT NULL,
    "ticket_status" int4 NOT NULL,
    "ticket_remark" varchar(255),
    "expected_completion_time" int8 NOT NULL,
    "is_notify" bool DEFAULT false,
    "pre_overdue_days" int4,
    "post_overdue_days" int4,
    "serial_no" varchar(255) NOT NULL,
    "creator_name" varchar(64) NOT NULL,
    "creator_id" varchar(255) NOT NULL,
    "create_time" timestamp NOT NULL,
    "update_time" timestamp NOT NULL,
    PRIMARY KEY ("ticket_id")
);

COMMENT ON COLUMN " tickets"."ticket_id" IS '工单 ID';
COMMENT ON COLUMN " tickets"."ticket_name" IS ' 工单名称';
COMMENT ON COLUMN " tickets"."ticket_type" IS '工单类型';
COMMENT ON COLUMN " tickets"."ticket_priority" IS '工单优先级';
COMMENT ON COLUMN " tickets"."ticket_status" IS '工单状态';
COMMENT ON COLUMN " tickets"."ticket_remark" IS '工单备注';
COMMENT ON COLUMN " tickets"."expected_completion_time" IS '预计完成时间';
COMMENT ON COLUMN " tickets"."is_notify" IS '是否提醒';
COMMENT ON COLUMN " tickets"."pre_overdue_days" IS '到期前提醒天数';
COMMENT ON COLUMN " tickets"."post_overdue_days" IS '到期后提醒天数';
COMMENT ON COLUMN " tickets"."serial_no" IS '流程实例 ID';
COMMENT ON COLUMN " tickets"."creator_name" IS '创建人名称';
COMMENT ON COLUMN " tickets"."creator_id" IS '创建人 ID';
COMMENT ON COLUMN " tickets"."create_time" IS '创建时间';
COMMENT ON COLUMN " tickets"."update_time" IS '更新时间';