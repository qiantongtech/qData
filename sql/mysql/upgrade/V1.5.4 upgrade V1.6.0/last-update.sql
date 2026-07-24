CREATE TABLE `quartz_job` (
    `job_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `job_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '任务名称',
    `job_group` VARCHAR(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
    `invoke_target` VARCHAR(500) NOT NULL COMMENT '调用目标字符串',
    `cron_expression` VARCHAR(255) DEFAULT '' COMMENT 'cron执行表达式',
    `misfire_policy` VARCHAR(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
    `concurrent` CHAR(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
    `execution_type` VARCHAR(32) DEFAULT NULL COMMENT '执行类型 PARALLEL（并行）、SERIAL_WAIT（串行等待）、SERIAL_DISCARD（串行丢弃）、SERIAL_PRIORITY（串行优先级）',
    `status` CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` TIMESTAMP(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` TIMESTAMP(0) NULL DEFAULT NULL COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT '' COMMENT '备注信息',
    PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Quartz业务调度任务表';

CREATE TABLE `quartz_job_log` (
    `job_log_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
    `job_name` VARCHAR(64) NOT NULL COMMENT '任务名称',
    `job_group` VARCHAR(64) NOT NULL COMMENT '任务组名',
    `invoke_target` VARCHAR(500) NOT NULL COMMENT '调用目标字符串',
    `job_message` VARCHAR(500) DEFAULT NULL COMMENT '日志信息',
    `status` CHAR(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
    `exception_info` VARCHAR(2000) DEFAULT '' COMMENT '异常信息',
    `create_time` TIMESTAMP(0) NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Quartz业务调度任务日志表';

ALTER TABLE `dpp_etl_task`
    ADD COLUMN `quartz_id` BIGINT DEFAULT NULL COMMENT 'Quartz调度任务ID',
    ADD COLUMN `scheduler` VARCHAR(32) DEFAULT NULL COMMENT '调度器',
    ADD COLUMN `actuator` VARCHAR(32) DEFAULT NULL COMMENT '执行器';

ALTER TABLE `dpp_etl_scheduler`
    ADD COLUMN `quartz_id` BIGINT DEFAULT NULL COMMENT 'Quartz调度任务ID';

ALTER TABLE `mc_task_scheduler`
    ADD COLUMN `scheduler` VARCHAR(32) DEFAULT NULL COMMENT '调度引擎';
