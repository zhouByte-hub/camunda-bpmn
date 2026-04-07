CREATE TABLE IF NOT EXISTS "contract_ticket" (
    "ticket_id" varchar(64) NOT NULL,
    "ticket_name" varchar(128) NOT NULL,
    "contract_number" varchar(64) NOT NULL,
    "contract_type" varchar(64) NOT NULL,
    "contract_amount" int8 NOT NULL,
    "contract_status" varchar(32) NOT NULL,
    "crm_customer_id" varchar(64),
    "contract_document_id" varchar(128),
    "signed_document_id" varchar(128),
    "ticket_remark" varchar(512),
    "expected_completion_time" int8 NOT NULL,
    "expiry_time" int8,
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

COMMENT ON COLUMN "contract_ticket"."ticket_id" IS '工单 ID';
COMMENT ON COLUMN "contract_ticket"."ticket_name" IS '工单名称';
COMMENT ON COLUMN "contract_ticket"."contract_number" IS '合同编号';
COMMENT ON COLUMN "contract_ticket"."contract_type" IS '合同类型';
COMMENT ON COLUMN "contract_ticket"."contract_amount" IS '合同金额（分）';
COMMENT ON COLUMN "contract_ticket"."contract_status" IS '合同状态';
COMMENT ON COLUMN "contract_ticket"."crm_customer_id" IS 'CRM 客户 ID';
COMMENT ON COLUMN "contract_ticket"."contract_document_id" IS '合同文档 ID';
COMMENT ON COLUMN "contract_ticket"."signed_document_id" IS '签署后文档 ID';
COMMENT ON COLUMN "contract_ticket"."ticket_remark" IS '工单备注';
COMMENT ON COLUMN "contract_ticket"."expected_completion_time" IS '预计完成时间';
COMMENT ON COLUMN "contract_ticket"."expiry_time" IS '合同到期时间';
COMMENT ON COLUMN "contract_ticket"."is_notify" IS '是否提醒';
COMMENT ON COLUMN "contract_ticket"."pre_overdue_days" IS '到期前提醒天数';
COMMENT ON COLUMN "contract_ticket"."post_overdue_days" IS '到期后提醒天数';
COMMENT ON COLUMN "contract_ticket"."creator_name" IS '创建人名称';
COMMENT ON COLUMN "contract_ticket"."creator_id" IS '创建人 ID';
COMMENT ON COLUMN "contract_ticket"."create_time" IS '创建时间';
COMMENT ON COLUMN "contract_ticket"."update_time" IS '更新时间';


CREATE TABLE IF NOT EXISTS "contract_task" (
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
    "task_key" int8 NOT NULL,
    "candidate_groups" varchar(128),
    "assignee" varchar(64),
    "create_time" timestamp NOT NULL,
    "update_time" timestamp NOT NULL,
    PRIMARY KEY ("id")
);

COMMENT ON COLUMN "contract_task"."id" IS 'id';
COMMENT ON COLUMN "contract_task"."ticket_id" IS '工单 ID';
COMMENT ON COLUMN "contract_task"."task_type" IS '任务类型';
COMMENT ON COLUMN "contract_task"."process_instance_id" IS '流程实例 Id';
COMMENT ON COLUMN "contract_task"."process_instance_key" IS '流程实例 Key';
COMMENT ON COLUMN "contract_task"."element_id" IS '流程元素 ID';
COMMENT ON COLUMN "contract_task"."element_instance_key" IS ' 流程元素 Key';
COMMENT ON COLUMN "contract_task"."task_status" IS '任务状态';
COMMENT ON COLUMN "contract_task"."candidate_groups" IS '候选审批组';
COMMENT ON COLUMN "contract_task"."assignee" IS '处理人';
COMMENT ON COLUMN "contract_task"."create_time" IS '创建时间';
COMMENT ON COLUMN "contract_task"."update_time" IS '更新时间';
