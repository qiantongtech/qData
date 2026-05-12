-- ----------------------------
-- Table structure for mc_column
-- ----------------------------
DROP TABLE IF EXISTS `mc_column`;
CREATE TABLE `mc_column` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                             `task_id` bigint(20) DEFAULT NULL COMMENT '采集任务id;预留字段，暂时不用',
                             `db_id` bigint(20) DEFAULT NULL COMMENT '库id',
                             `table_id` bigint(20) DEFAULT NULL COMMENT '表信息id',
                             `datasource_id` bigint(20) DEFAULT NULL COMMENT '数据源id;冗余字段',
                             `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本',
                             `safety_level_id` bigint(20) DEFAULT NULL COMMENT '安全等级id',
                             `data_elem_id` bigint(20) DEFAULT NULL COMMENT '数据元id',
                             `column_name` varchar(256) DEFAULT NULL COMMENT '字段名称',
                             `column_comment` varchar(2000) DEFAULT NULL COMMENT '字段注释',
                             `column_type` varchar(128) DEFAULT NULL COMMENT '字段类型',
                             `column_length` int(11) DEFAULT NULL COMMENT '数据长度',
                             `column_precision` int(11) DEFAULT NULL COMMENT '数据精度',
                             `column_scale` int(11) DEFAULT NULL COMMENT '数据小数位',
                             `default_value` varchar(128) DEFAULT NULL COMMENT '数据默认值',
                             `pk_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否主键;0:否 1:是',
                             `fk_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否外键;0:否 1:是',
                             `nullable_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否可空;0:否 1:是',
                             `business_definition` varchar(128) DEFAULT NULL COMMENT '业务定义',
                             `measuring_unit` varchar(32) DEFAULT NULL COMMENT '度量单位',
                             `data_quality` int(11) NOT NULL DEFAULT '100' COMMENT '数据质量',
                             `audit_status` varchar(10) DEFAULT '2' COMMENT '审核状态;1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常',
                             `audit_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
                             `status` varchar(1) NOT NULL DEFAULT '0' COMMENT '状态;0：未发布，1：已发布',
                             `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                             `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                             `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                             `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                             `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `remark` varchar(512) DEFAULT NULL COMMENT '备注',
                             `description` varchar(512) DEFAULT NULL COMMENT '描述',
                             `portal_visible` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否在门户展示：0-不展示，1-展示',
                             `auto_increment_flag` char(1) DEFAULT '0' COMMENT '是否自增 (0:否, 1:是)',
                             `partition_flag` char(1) DEFAULT '0' COMMENT '是否分区字段 (0:否, 1:是)',
                             `column_standard` varchar(128) DEFAULT NULL COMMENT '字段规范',
                             `value_rule` varchar(128) DEFAULT NULL COMMENT '取值逻辑',
                             `responsible_dept` bigint(20) DEFAULT NULL COMMENT '责任部门',
                             `business_leader` bigint(20) DEFAULT NULL COMMENT '负责人',
                             `unique_flag` char(1) DEFAULT NULL COMMENT '是否唯一',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='元数据字段信息表';

-- ----------------------------
-- Table structure for mc_column_log
-- ----------------------------
DROP TABLE IF EXISTS `mc_column_log`;
CREATE TABLE `mc_column_log` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                 `data_type` varchar(10) NOT NULL DEFAULT '1' COMMENT '数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用',
                                 `task_id` bigint(20) DEFAULT NULL COMMENT '采集任务 id;预留字段，暂时不用',
                                 `column_id` bigint(20) NOT NULL COMMENT '字段 id',
                                 `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本',
                                 `db_id` bigint(20) NOT NULL COMMENT '库 id',
                                 `table_id` bigint(20) NOT NULL COMMENT '表信息 id',
                                 `datasource_id` bigint(20) NOT NULL COMMENT '数据源 id;冗余字段',
                                 `safety_level_id` bigint(20) DEFAULT NULL COMMENT '安全等级 id',
                                 `data_elem_id` bigint(20) DEFAULT NULL COMMENT '数据元 id',
                                 `column_name` varchar(256) NOT NULL COMMENT '字段名称',
                                 `column_comment` varchar(2000) DEFAULT NULL COMMENT '字段注释',
                                 `column_type` varchar(128) NOT NULL COMMENT '字段类型',
                                 `column_length` int(11) DEFAULT NULL COMMENT '数据长度',
                                 `column_precision` int(11) DEFAULT NULL COMMENT '数据精度',
                                 `column_scale` int(11) DEFAULT NULL COMMENT '数据小数位',
                                 `default_value` varchar(128) DEFAULT NULL COMMENT '数据默认值',
                                 `pk_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否主键;0:否 1:是',
                                 `fk_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否外键;0:否 1:是',
                                 `nullable_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否可空;0:否 1:是',
                                 `bus_definition` varchar(128) DEFAULT NULL COMMENT '业务定义',
                                 `measuring_unit` varchar(32) DEFAULT NULL COMMENT '度量单位',
                                 `data_quality` int(11) NOT NULL DEFAULT '100' COMMENT '数据质量',
                                 `update_type` varchar(10) DEFAULT NULL COMMENT '变更类型',
                                 `update_msg` varchar(512) DEFAULT NULL COMMENT '变更说明',
                                 `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                 `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                 `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                 `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人 id',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                 `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人 id',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `remark` varchar(512) DEFAULT NULL COMMENT '备注',
                                 `description` varchar(512) DEFAULT NULL COMMENT '描述',
                                 `change_type` varchar(1) DEFAULT '0' COMMENT '变更类型：0-无变化，1-新增，2-修改，3-删除',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='元数据字段信息 - 日志表';

-- ----------------------------
-- Table structure for mc_db
-- ----------------------------
DROP TABLE IF EXISTS `mc_db`;
CREATE TABLE `mc_db` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                         `task_id` bigint(20) DEFAULT NULL COMMENT '采集任务id;预留字段，暂时不用',
                         `source_system_id` bigint(20) DEFAULT NULL COMMENT '来源系统id',
                         `source_system_name` varchar(128) DEFAULT NULL COMMENT '来源系统名称',
                         `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本',
                         `datasource_id` bigint(20) NOT NULL COMMENT '数据源id',
                         `ip` varchar(32) NOT NULL COMMENT 'IP',
                         `port` int(11) NOT NULL COMMENT '端口号',
                         `datasource_config` varchar(1024) DEFAULT NULL COMMENT '数据源配置(json字符串)',
                         `db_type` varchar(32) NOT NULL COMMENT '数据库类型',
                         `db_name` varchar(128) NOT NULL COMMENT '数据库名称',
                         `schema_name` varchar(128) DEFAULT NULL COMMENT '模式名;可空',
                         `safety_level_id` bigint(20) DEFAULT NULL COMMENT '安全等级id',
                         `belonging_layer` varchar(10) DEFAULT NULL COMMENT '所属分层;1:ODS 2:DWD 3:DWS  4:ADS 5:外部系统）',
                         `belonging_system` varchar(128) DEFAULT NULL COMMENT '所属系统',
                         `business_leader` bigint(20) DEFAULT NULL COMMENT '业务责任人',
                         `business_leader_phone` varchar(32) DEFAULT NULL COMMENT '业务责任人电话',
                         `tech_leader` bigint(20) DEFAULT NULL COMMENT '技术责任人',
                         `tech_leader_phone` varchar(32) DEFAULT NULL COMMENT '技术责任人电话',
                         `storage_size` bigint(20) DEFAULT '0' COMMENT '存储大小',
                         `data_quality` int(11) NOT NULL DEFAULT '100' COMMENT '数据质量',
                         `audit_status` varchar(10) DEFAULT '2' COMMENT '审核状态;1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常',
                         `audit_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
                         `status` varchar(1) NOT NULL DEFAULT '0' COMMENT '状态;0：未发布，1：已发布',
                         `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                         `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                         `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                         `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                         `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `remark` varchar(512) DEFAULT NULL COMMENT '备注',
                         `description` varchar(512) DEFAULT NULL COMMENT '描述',
                         `portal_visible` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否在门户展示：0-不展示，1-展示',
                         `storage_engine` varchar(128) DEFAULT NULL COMMENT '存储引擎',
                         `responsible_dept` bigint(20) DEFAULT NULL COMMENT '责任部门',
                         `data_row_count` int(11) DEFAULT NULL COMMENT '数据行数',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据库表';

-- ----------------------------
-- Table structure for mc_domain
-- ----------------------------
DROP TABLE IF EXISTS `mc_domain`;
CREATE TABLE `mc_domain` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                             `name` varchar(128) NOT NULL COMMENT '业务名称',
                             `parent_id` bigint(20) DEFAULT NULL COMMENT '关联上级ID',
                             `sort_order` int(11) NOT NULL COMMENT '类别排序',
                             `code` varchar(128) NOT NULL COMMENT '层级编码',
                             `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                             `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                             `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                             `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                             `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `remark` varchar(512) DEFAULT NULL COMMENT '备注',
                             `description` varchar(512) DEFAULT NULL COMMENT '描述',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务域表';

-- ----------------------------
-- Table structure for mc_table
-- ----------------------------
DROP TABLE IF EXISTS `mc_table`;
CREATE TABLE `mc_table` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `task_id` bigint(20) DEFAULT NULL COMMENT '采集任务id;预留字段，暂时不用',
                            `db_id` bigint(20) DEFAULT NULL COMMENT '库id',
                            `datasource_id` bigint(20) DEFAULT NULL COMMENT '数据源id;冗余字段',
                            `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本',
                            `table_name` varchar(256) DEFAULT NULL COMMENT '表名称（表英文名称）',
                            `table_comment` varchar(2000) DEFAULT NULL COMMENT '表注释/表描述（表中文名称）',
                            `safety_level_id` bigint(20) DEFAULT NULL COMMENT '安全等级id',
                            `db_name` varchar(64) DEFAULT NULL COMMENT '数据库名',
                            `schema_name` varchar(64) DEFAULT NULL COMMENT '模式名;可空',
                            `storage_type` varchar(32) DEFAULT NULL COMMENT '存储类型',
                            `storage_size` bigint(20) DEFAULT NULL COMMENT '存储大小',
                            `business_leader` bigint(20) DEFAULT NULL COMMENT '业务责任人',
                            `business_leader_phone` varchar(32) DEFAULT NULL COMMENT '业务责任人电话',
                            `tech_leader` bigint(20) DEFAULT NULL COMMENT '技术责任人',
                            `tech_leader_phone` varchar(32) DEFAULT NULL COMMENT '技术责任人电话',
                            `master_flag` varchar(1) DEFAULT NULL COMMENT '是否主表;0：否，1：是',
                            `temp_flag` varchar(1) DEFAULT NULL COMMENT '是否临时表;0：否，1：是',
                            `data_quality` int(11) NOT NULL DEFAULT '100' COMMENT '数据质量',
                            `audit_status` varchar(10) DEFAULT '2' COMMENT '审核状态;1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常',
                            `audit_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
                            `status` varchar(1) NOT NULL DEFAULT '0' COMMENT '状态;0：未发布，1：已发布',
                            `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                            `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                            `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                            `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                            `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `remark` varchar(512) DEFAULT NULL COMMENT '备注',
                            `description` varchar(512) DEFAULT NULL COMMENT '描述',
                            `portal_visible` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否在门户展示：0-不展示，1-展示',
                            `column_count` int(11) DEFAULT NULL COMMENT '字段数量',
                            `tb_index` varchar(1024) DEFAULT NULL COMMENT '索引',
                            `row_count` int(11) DEFAULT NULL COMMENT '行数',
                            `partition_key` varchar(128) DEFAULT NULL COMMENT '分区字段',
                            `storage_engine` varchar(128) DEFAULT NULL COMMENT '存储引擎',
                            `responsible_dept` bigint(20) DEFAULT NULL COMMENT '责任部门',
                            `primary_key` varchar(128) DEFAULT NULL COMMENT '主键字段',
                            `tb_create_time` datetime DEFAULT NULL COMMENT '表创建时间',
                            `data_update_time` datetime DEFAULT NULL COMMENT '数据更新时间',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='元数据表信息表';

-- ----------------------------
-- Table structure for mc_table_column_rel_log
-- ----------------------------
DROP TABLE IF EXISTS `mc_table_column_rel_log`;
CREATE TABLE `mc_table_column_rel_log` (
                                           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                           `data_type` varchar(10) NOT NULL DEFAULT '1' COMMENT '数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用',
                                           `task_id` bigint(20) DEFAULT NULL COMMENT '采集任务 id;预留字段，暂时不用',
                                           `db_id` bigint(20) NOT NULL COMMENT '库 id',
                                           `db_version` int(11) NOT NULL COMMENT '库版本',
                                           `table_id` bigint(20) NOT NULL COMMENT '表 id',
                                           `table_version` int(11) NOT NULL COMMENT '表版本',
                                           `column_id` bigint(20) NOT NULL COMMENT '字段 id',
                                           `column_version` int(11) NOT NULL COMMENT '字段版本',
                                           `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                           `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                           `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                           `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人 id',
                                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                           `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人 id',
                                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           `remark` varchar(512) DEFAULT NULL COMMENT '备注',
                                           `description` varchar(512) DEFAULT NULL COMMENT '描述',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='元数据数据库与表信息及字段信息关系-日志表';

-- ----------------------------
-- Table structure for mc_table_log
-- ----------------------------
DROP TABLE IF EXISTS `mc_table_log`;
CREATE TABLE `mc_table_log` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                `data_type` varchar(10) NOT NULL DEFAULT '1' COMMENT '数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用',
                                `task_id` bigint(20) DEFAULT NULL COMMENT '采集任务 id;预留字段，暂时不用',
                                `table_id` bigint(20) NOT NULL COMMENT '表 id',
                                `version` varchar(50) NOT NULL DEFAULT '1' COMMENT '版本',
                                `db_id` bigint(20) NOT NULL COMMENT '库 id',
                                `datasource_id` bigint(20) NOT NULL COMMENT '数据源 id;冗余字段',
                                `table_name` varchar(256) NOT NULL COMMENT '表名称（表英文名称）',
                                `table_comment` varchar(2000) DEFAULT NULL COMMENT '表注释/表描述（表中文名称）',
                                `safety_level_id` bigint(20) DEFAULT NULL COMMENT '安全等级 id',
                                `db_name` varchar(128) NOT NULL COMMENT '数据库名',
                                `schema_name` varchar(128) DEFAULT NULL COMMENT '模式名;可空',
                                `storage_type` varchar(32) DEFAULT NULL COMMENT '存储类型',
                                `storage_size` int(11) DEFAULT NULL COMMENT '存储大小',
                                `business_leader` bigint(20) DEFAULT NULL COMMENT '业务责任人',
                                `business_leader_phone` varchar(32) DEFAULT NULL COMMENT '业务责任人电话',
                                `tech_leader` bigint(20) DEFAULT NULL COMMENT '技术责任人',
                                `tech_leader_phone` varchar(32) DEFAULT NULL COMMENT '技术责任人电话',
                                `master_flag` varchar(1) DEFAULT NULL COMMENT '是否主表;0：否，1：是',
                                `temp_flag` varchar(1) DEFAULT NULL COMMENT '是否临时表;0：否，1：是',
                                `data_quality` int(11) NOT NULL DEFAULT '100' COMMENT '数据质量',
                                `update_type` varchar(10) DEFAULT NULL COMMENT '变更类型',
                                `update_msg` varchar(512) DEFAULT NULL COMMENT '变更说明',
                                `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人 id',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人 id',
                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                `remark` varchar(512) DEFAULT NULL COMMENT '备注',
                                `description` varchar(512) DEFAULT NULL COMMENT '描述',
                                `instance_id` bigint(20) DEFAULT NULL COMMENT '实例 ID',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='元数据表信息 - 日志表';

-- ----------------------------
-- Table structure for mc_task
-- ----------------------------
DROP TABLE IF EXISTS `mc_task`;
CREATE TABLE `mc_task` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                           `source_system_id` bigint(20) DEFAULT NULL COMMENT '来源系统id',
                           `source_system_name` varchar(128) DEFAULT NULL COMMENT '来源系统名称',
                           `name` varchar(128) NOT NULL COMMENT '任务名称',
                           `datasource_id` bigint(20) NOT NULL COMMENT '数据连接id',
                           `db_type` varchar(32) NOT NULL COMMENT '数据库类型;冗余字段',
                           `leader` bigint(20) DEFAULT NULL COMMENT '责任人',
                           `leader_phone` varchar(32) DEFAULT NULL COMMENT '责任人电话',
                           `collection_mode` varchar(10) NOT NULL DEFAULT '1' COMMENT '采集模式;1:增量 2:全量 3:CDC 捕获',
                           `collection_scope` varchar(10) NOT NULL DEFAULT '1' COMMENT '采集范围;1:自定义库  2:整个数据源（整个数据源时采集范围表中不需要存储数据，每次同步时现查即可）',
                           `status` varchar(1) NOT NULL DEFAULT '0' COMMENT '任务状态;0:下线 1:上线',
                           `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                           `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                           `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                           `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                           `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           `remark` varchar(512) DEFAULT NULL COMMENT '备注',
                           `description` varchar(512) DEFAULT NULL COMMENT '描述',
                           `collect_type` smallint(6) NOT NULL DEFAULT '1' COMMENT '采集任务类型：1-采集，2-DDL',
                           `blacklist` text COMMENT '采集黑名单',
                           `responsible_dept` bigint(20) DEFAULT NULL COMMENT '所属部门',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采集任务表';

-- ----------------------------
-- Table structure for mc_task_instance
-- ----------------------------
DROP TABLE IF EXISTS `mc_task_instance`;
CREATE TABLE `mc_task_instance` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                    `source_system_id` bigint(20) DEFAULT NULL COMMENT '来源系统id',
                                    `source_system_name` varchar(128) DEFAULT NULL COMMENT '来源系统名称',
                                    `task_id` bigint(20) NOT NULL COMMENT '采集任务id',
                                    `collection_mode` varchar(10) NOT NULL DEFAULT '1' COMMENT '采集模式;1:增量 2:全量 3:CDC 捕获',
                                    `collection_scope` varchar(10) NOT NULL DEFAULT '1' COMMENT '采集范围;1:自定义库  2:整个数据源（整个数据源时采集范围表中不需要存储数据，每次同步时现查即可）',
                                    `total_count` int(11) NOT NULL DEFAULT '0' COMMENT '采集表总数量',
                                    `success_count` int(11) DEFAULT '0' COMMENT '采集表成功数量',
                                    `fail_count` int(11) DEFAULT '0' COMMENT '采集表失败数量',
                                    `fail_cause` varchar(512) DEFAULT NULL COMMENT '失败原因;任务的失败原因和某个表采集是否与否无关',
                                    `add_count` int(11) DEFAULT '0' COMMENT '新增数量',
                                    `del_count` int(11) DEFAULT '0' COMMENT '删减数量',
                                    `update_count` int(11) DEFAULT '0' COMMENT '变更数量',
                                    `start_time` datetime NOT NULL COMMENT '开始时间',
                                    `end_time` datetime DEFAULT NULL COMMENT '结束时间',
                                    `duration` int(11) DEFAULT '0' COMMENT '耗时;单位：秒',
                                    `status` varchar(10) NOT NULL DEFAULT '1' COMMENT '状态;1：正在执行 2：失败 9：成功',
                                    `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                    `del_flag` varchar(1) NOT NULL COMMENT '删除标志;1：已删除，0：未删除',
                                    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                    `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                    `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `remark` varchar(512) DEFAULT NULL COMMENT '备注',
                                    `description` varchar(512) DEFAULT NULL COMMENT '描述',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采集任务实例表';

-- ----------------------------
-- Table structure for mc_task_instance_log
-- ----------------------------
DROP TABLE IF EXISTS `mc_task_instance_log`;
CREATE TABLE `mc_task_instance_log` (
                                        `task_instance_id` bigint(20) NOT NULL COMMENT '任务实例id',
                                        `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
                                        `task_id` bigint(20) NOT NULL COMMENT '任务id;采集任务的id',
                                        `log_content` text NOT NULL COMMENT '日志内容',
                                        `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                        `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                        `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                        `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
                                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                        `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        `remark` varchar(512) DEFAULT NULL COMMENT '备注',
                                        PRIMARY KEY (`task_instance_id`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采集任务实例-日志表';

-- ----------------------------
-- Table structure for mc_task_scheduler
-- ----------------------------
DROP TABLE IF EXISTS `mc_task_scheduler`;
CREATE TABLE `mc_task_scheduler` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                     `task_id` bigint(20) NOT NULL COMMENT '任务id',
                                     `job_id` bigint(20) DEFAULT NULL COMMENT '调度器id',
                                     `task_code` varchar(32) DEFAULT NULL COMMENT '调度器任务编码',
                                     `start_time` datetime DEFAULT NULL COMMENT '开始时间',
                                     `end_time` datetime DEFAULT NULL COMMENT '结束时间',
                                     `timezone_id` varchar(128) DEFAULT 'Asia/Shanghai' COMMENT '时区;直接默认 Asia/Shanghai',
                                     `cron_expression` varchar(32) NOT NULL COMMENT 'cron表达式',
                                     `failure_strategy` varchar(1) DEFAULT '1' COMMENT '失败策略;是否继续 0:否 1:是',
                                     `status` varchar(10) NOT NULL DEFAULT '0' COMMENT '调度状态;0:未上线，1:已上线 2:草稿',
                                     `remark` varchar(512) DEFAULT NULL COMMENT '备注',
                                     `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                     `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                     `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                     `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                     `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                                     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据集成调度信息';

-- ----------------------------
-- Table structure for mc_task_scope
-- ----------------------------
DROP TABLE IF EXISTS `mc_task_scope`;
CREATE TABLE `mc_task_scope` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                 `task_id` bigint(20) NOT NULL COMMENT '任务id;采集任务id',
                                 `db_name` varchar(64) NOT NULL COMMENT '数据库名称',
                                 `schema_name` varchar(64) DEFAULT NULL COMMENT '模式名',
                                 `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
                                 `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
                                 `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                 `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                 `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `remark` varchar(512) DEFAULT NULL COMMENT '备注',
                                 `description` varchar(512) DEFAULT NULL COMMENT '描述',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采集范围表';

-- ----------------------------
-- Table structure for att_source_system
-- ----------------------------
DROP TABLE IF EXISTS `att_source_system`;
CREATE TABLE `att_source_system` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` varchar(128) NOT NULL COMMENT '系统名称',
    `type` char(1) DEFAULT NULL COMMENT '系统类型',
    `sort_order` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
    `description` varchar(512) DEFAULT NULL COMMENT '描述',
    `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
    `responsible_person` varchar(32) DEFAULT NULL COMMENT '负责人',
    `contact_person` varchar(32) DEFAULT NULL COMMENT '对接人',
    `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(512) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='来源系统';

SET FOREIGN_KEY_CHECKS=1;
