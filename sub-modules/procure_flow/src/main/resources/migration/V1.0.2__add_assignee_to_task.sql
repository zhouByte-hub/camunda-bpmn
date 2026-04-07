ALTER TABLE "procure_task" ADD COLUMN "assignee" varchar(64);

COMMENT ON COLUMN "procure_task"."assignee" IS '处理人';
