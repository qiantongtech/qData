USE qdata;

-- ----------------------------
-- 表：dm_data_domain（数据域管理）
-- ----------------------------
DROP TABLE IF EXISTS `dm_data_domain`;
CREATE TABLE `dm_data_domain` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `eng_name` varchar(32) NOT NULL COMMENT '英文缩写',
  `owner_user_id` bigint(20) NOT NULL COMMENT '负责人ID',
  `description` varchar(1024) DEFAULT NULL COMMENT '描述',
  `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据域管理';

-- ----------------------------
-- 表：dm_data_layer（数仓分层管理）
-- ----------------------------
DROP TABLE IF EXISTS `dm_data_layer`;
CREATE TABLE `dm_data_layer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `eng_name` varchar(32) NOT NULL COMMENT '英文缩写',
  `owner_user_id` bigint(20) NOT NULL COMMENT '负责人ID',
  `category` char(1) NOT NULL DEFAULT '1' COMMENT '分类;1：贴源层 2:公共层 3:应用层(字典：dm_data_layer_category)',
  `description` varchar(1024) DEFAULT NULL COMMENT '描述',
  `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数仓分层管理';

-- ----------------------------
-- 表：dm_data_layer_specification（数仓分层-规范管理）
-- ----------------------------
DROP TABLE IF EXISTS `dm_data_layer_specification`;
CREATE TABLE `dm_data_layer_specification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `data_layer_id` bigint(20) NOT NULL COMMENT '数仓分层ID',
  `prefix_name` varchar(32) NOT NULL COMMENT '表前缀',
  `business_eng_name` varchar(32) NOT NULL COMMENT '业务大类英文缩写',
  `owner_user_id` bigint(20) NOT NULL COMMENT '负责人ID',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态;0：关闭，1：开启',
  `description` varchar(1024) DEFAULT NULL COMMENT '描述',
  `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `序号` int(11) DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数仓分层-规范管理';

-- ----------------------------
-- 表：dm_theme_domain（主题域管理）
-- ----------------------------
DROP TABLE IF EXISTS `dm_theme_domain`;
CREATE TABLE `dm_theme_domain` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(128) NOT NULL COMMENT '层级编码',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `eng_name` varchar(32) NOT NULL COMMENT '英文缩写',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '关联上级ID',
  `owner_user_id` bigint(20) NOT NULL COMMENT '负责人ID',
  `data_layer_id` bigint(20) NOT NULL COMMENT '数仓分层ID',
  `description` varchar(1024) DEFAULT NULL COMMENT '描述',
  `valid_flag` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='主题域管理';
