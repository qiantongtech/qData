USE qdata;

-- 1. 创建 AI 模型管理表
CREATE TABLE `ai_model` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` VARCHAR(32) NOT NULL COMMENT '模型名称;例如 qwen-max',
  `platform` VARCHAR(10) NOT NULL COMMENT '平台;1:通义千问 2:DeepSeek',
  `api_url` VARCHAR(512) DEFAULT NULL COMMENT 'API地址',
  `api_key` VARCHAR(256) DEFAULT NULL COMMENT 'API秘钥',
  `sort_order` INT(11) NOT NULL COMMENT '排序',
  `description` VARCHAR(512) DEFAULT NULL COMMENT '描述',
  `valid_flag` VARCHAR(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` VARCHAR(32) DEFAULT NULL COMMENT '创建人',
  `creator_id` BIGINT(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `update_by` VARCHAR(32) DEFAULT NULL COMMENT '更新人',
  `updater_id` BIGINT(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模型管理';

-- 2. 创建 AI 聊天对话表
CREATE TABLE `ai_chat_conversation` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户id',
  `title` VARCHAR(128) NOT NULL COMMENT '对话标题',
  `pinned` VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '是否置顶;0：不置顶，1：置顶',
  `pinned_time` DATETIME(6) DEFAULT NULL COMMENT '置顶时间',
  `datasource_id` BIGINT(20) NOT NULL COMMENT '数据源id',
  `fact_table_name` VARCHAR(256) NOT NULL COMMENT '事实表名称',
  `fact_table_comment` VARCHAR(2000) DEFAULT NULL COMMENT '事实表注释/事实表描述',
  `dimension_table` TEXT DEFAULT NULL COMMENT '维度表;格式 [{"tableName":"表名","tableComment":"表注释"}]',
  `associations` TEXT DEFAULT NULL COMMENT '关联信息;格式 [{"dimensionTable": "维度表名","factColumnName": "事实表外键字段名","dimensionColumnName": "维度表主键字段名","matchReason": "匹配依据"}]',
  `join_condition_match_flag` VARCHAR(1) DEFAULT '0' COMMENT '关联条件匹配状态;0：未匹配，1：已匹配',
  `join_condition_match_type` CHAR(1) DEFAULT '1' COMMENT '关联条件匹配类型;1：自动匹配 2:手动匹配',
  `valid_flag` VARCHAR(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` VARCHAR(32) DEFAULT NULL COMMENT '创建人',
  `creator_id` BIGINT(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `update_by` VARCHAR(32) DEFAULT NULL COMMENT '更新人',
  `updater_id` BIGINT(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
  `datasource_type` VARCHAR(32) DEFAULT NULL COMMENT '数据源类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ai聊天对话表';

-- 3. 创建 AI 聊天消息表
CREATE TABLE `ai_chat_message` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `conversation_id` BIGINT(20) NOT NULL COMMENT '对话id',
  `reply_id` BIGINT(20) DEFAULT NULL COMMENT '回复id',
  `reply_type` VARCHAR(8188) DEFAULT NULL COMMENT '回复类型;1:知识问答 2: 知识图表',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户id',
  `type` VARCHAR(10) DEFAULT '1' COMMENT '消息类型;1:用户 2：机器人',
  `content` TEXT DEFAULT NULL COMMENT '消息内容',
  `context_flag` VARCHAR(1) NOT NULL DEFAULT '1' COMMENT '是否携带上下文;0：否，1：是',
  `valid_flag` VARCHAR(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` VARCHAR(32) DEFAULT NULL COMMENT '创建人',
  `creator_id` BIGINT(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `update_by` VARCHAR(32) DEFAULT NULL COMMENT '更新人',
  `updater_id` BIGINT(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ai聊天消息表';
