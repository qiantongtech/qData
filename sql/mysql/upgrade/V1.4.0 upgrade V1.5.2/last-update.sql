-- ----------------------------
-- Table structure for dm_business_category
-- ----------------------------
DROP TABLE IF EXISTS `dm_business_category`;
CREATE TABLE `dm_business_category` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` VARCHAR(128) NOT NULL COMMENT '业务分类名称',
    `parent_id` BIGINT(20) DEFAULT NULL COMMENT '关联上级ID',
    `sort_order` INT(11) DEFAULT 0 NOT NULL COMMENT '排序',
    `description` VARCHAR(512) DEFAULT NULL COMMENT '描述',
    `eng_name` VARCHAR(128) DEFAULT NULL COMMENT '英文缩写名',
    `owner_phone` VARCHAR(128) DEFAULT NULL COMMENT '负责人手机号',
    `owner_id` BIGINT(20) DEFAULT NULL COMMENT '负责人ID',
    `domain_id` BIGINT(20) DEFAULT NULL COMMENT '数据域ID',
    `valid_flag` VARCHAR(1) DEFAULT '1' NOT NULL COMMENT '是否有效;0：无效，1：有效',
    `del_flag` VARCHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标志;1：已删除，0：未删除',
    `create_by` VARCHAR(32) DEFAULT NULL COMMENT '创建人',
    `creator_id` BIGINT(20) DEFAULT NULL COMMENT '创建人id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(32) DEFAULT NULL COMMENT '更新人',
    `updater_id` BIGINT(20) DEFAULT NULL COMMENT '更新人id',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
    `code` VARCHAR(128) DEFAULT NULL COMMENT '层级编码',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务分类';

-- ----------------------------
-- Table structure for dm_business_domain_rel
-- ----------------------------
DROP TABLE IF EXISTS `dm_business_domain_rel`;
CREATE TABLE `dm_business_domain_rel` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `business_category_id` BIGINT(20) NOT NULL COMMENT '业务分类ID',
    `data_domain_id` BIGINT(20) NOT NULL COMMENT '数据域ID',
    `business_category_name` VARCHAR(128) DEFAULT NULL COMMENT '业务分类名称',
    `data_domain_name` VARCHAR(128) DEFAULT NULL COMMENT '数据域名称',
    `sort_order` INT(11) DEFAULT 0 NOT NULL COMMENT '排序',
    `description` VARCHAR(512) DEFAULT NULL COMMENT '描述',
    `valid_flag` VARCHAR(1) DEFAULT '1' NOT NULL COMMENT '是否有效;0：无效，1：有效',
    `del_flag` VARCHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标志;1：已删除，0：未删除',
    `create_by` VARCHAR(32) DEFAULT NULL COMMENT '创建人',
    `creator_id` BIGINT(20) DEFAULT NULL COMMENT '创建人id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(32) DEFAULT NULL COMMENT '更新人',
    `updater_id` BIGINT(20) DEFAULT NULL COMMENT '更新人id',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务分类数据域关联关系';

-- 逻辑模型新增字段
ALTER TABLE `dp_model` 
ADD COLUMN `table_type` VARCHAR(10) DEFAULT '1' COMMENT '表类型;1:明细表 2:汇总表 3:维度表 4:应用表',
ADD COLUMN `data_layer_id` BIGINT(20) DEFAULT NULL COMMENT '数仓分层id',
ADD COLUMN `data_domain_id` BIGINT(20) DEFAULT NULL COMMENT '数据分域id;只有表类型为非应用表是才有值',
ADD COLUMN `business_category_id` BIGINT(20) DEFAULT NULL COMMENT '业务分类id;只有表类型为非应用表是才有值',
ADD COLUMN `business_category_code` VARCHAR(128) DEFAULT NULL COMMENT '业务分类层级编码',
ADD COLUMN `theme_domain_id` BIGINT(20) DEFAULT NULL COMMENT '所属主题id（主题规划）;只有表类型为应用表是才有值',
ADD COLUMN `theme_domain_code` VARCHAR(128) DEFAULT NULL COMMENT '所属主题层级编码',
ADD COLUMN `table_case` VARCHAR(10) DEFAULT '1' COMMENT '表名大小写;1：大写 2：小写';

-- 逻辑模型新增字段
ALTER TABLE `da_asset` 
ADD COLUMN `table_type` VARCHAR(10) DEFAULT '1' COMMENT '表类型;1:明细表 2:汇总表 3:维度表 4:应用表',
ADD COLUMN `data_layer_id` BIGINT(20) DEFAULT NULL COMMENT '数仓分层id',
ADD COLUMN `data_domain_id` BIGINT(20) DEFAULT NULL COMMENT '数据分域id;只有表类型为非应用表是才有值',
ADD COLUMN `business_category_id` BIGINT(20) DEFAULT NULL COMMENT '业务分类id;只有表类型为非应用表是才有值',
ADD COLUMN `business_category_code` VARCHAR(128) DEFAULT NULL COMMENT '业务分类层级编码',
ADD COLUMN `theme_domain_id` BIGINT(20) DEFAULT NULL COMMENT '所属主题id（主题规划）;只有表类型为应用表是才有值',
ADD COLUMN `theme_domain_code` VARCHAR(128) DEFAULT NULL COMMENT '所属主题层级编码',
ADD COLUMN `table_case` VARCHAR(10) DEFAULT '1' COMMENT '表名大小写;1：大写 2：小写',
ADD COLUMN `table_id` VARCHAR(128) DEFAULT NULL COMMENT '元数据表id';

-- ----------------------------
-- Table structure for dg_data_category
-- ----------------------------
DROP TABLE IF EXISTS `dg_data_category`;
CREATE TABLE `dg_data_category` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `cat_id` BIGINT(20) NOT NULL COMMENT '类目id',
    `cat_code` VARCHAR(32) NOT NULL COMMENT '类目编码',
    `name` VARCHAR(128) NOT NULL COMMENT '分类名称',
    `short_name` VARCHAR(128) DEFAULT NULL COMMENT '分类名称缩写名',
    `data_level_id` BIGINT(20) DEFAULT 0 NOT NULL COMMENT '数据分级',
    `priority` VARCHAR(32) DEFAULT 'MEDIUM' COMMENT '任务优先级;HIGHEST,HIGH,MEDIUM,LOW,LOWEST',
    `description` VARCHAR(512) DEFAULT NULL COMMENT '描述',
    `valid_flag` VARCHAR(1) DEFAULT '1' NOT NULL COMMENT '是否有效;0：无效，1：有效',
    `del_flag` VARCHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标志;1：已删除，0：未删除',
    `create_by` VARCHAR(32) DEFAULT NULL COMMENT '创建人',
    `creator_id` BIGINT(20) DEFAULT NULL COMMENT '创建人id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(32) DEFAULT NULL COMMENT '更新人',
    `updater_id` BIGINT(20) DEFAULT NULL COMMENT '更新人id',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据分类';

-- ----------------------------
-- Table structure for dg_desensitize_assetcolumn
-- ----------------------------
DROP TABLE IF EXISTS `dg_desensitize_assetcolumn`;
CREATE TABLE `dg_desensitize_assetcolumn` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `asset_id` BIGINT(20) NOT NULL COMMENT '资产ID',
    `assetcolumn_id` BIGINT(20) NOT NULL COMMENT '资产字段ID',
    `data_category_id` BIGINT(20) NOT NULL COMMENT '数据分类ID',
    `sort_order` INT(11) DEFAULT 0 NOT NULL COMMENT '排序',
    `description` VARCHAR(512) DEFAULT NULL COMMENT '描述',
    `valid_flag` VARCHAR(1) DEFAULT '1' NOT NULL COMMENT '是否有效;0：无效，1：有效',
    `del_flag` VARCHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标志;1：已删除，0：未删除',
    `create_by` VARCHAR(32) DEFAULT NULL COMMENT '创建人',
    `creator_id` BIGINT(20) DEFAULT NULL COMMENT '创建人id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(32) DEFAULT NULL COMMENT '更新人',
    `updater_id` BIGINT(20) DEFAULT NULL COMMENT '更新人id',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='脱敏清单关联关系';

ALTER TABLE `da_asset` MODIFY COLUMN `cat_code` VARCHAR(256) NULL COMMENT '分类编码';
ALTER TABLE `dp_model` MODIFY COLUMN `cat_code` VARCHAR(128) NULL COMMENT '分类编码';
ALTER TABLE `dp_model_materialized` ADD COLUMN `release_mode` CHAR(1) DEFAULT '1' NULL COMMENT '发布模式';
