-- Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
--
-- This file is part of qData Data Middle Platform (Open Source Edition).
--
-- qData is licensed under Apache License 2.0 with additional qData terms.
-- You may use qData for commercial purposes, but you may not remove, hide,
-- modify, or replace the qData logo, copyright notices, license notices,
-- or attribution information without a separate commercial license.
--
-- White-label use, OEM distribution, rebranding, or presenting qData as
-- another product requires separate commercial authorization from
-- Jiangsu Qiantong Technology Co., Ltd.
--
-- Business License: https://docs.qdata.tech/business/policy.html
-- See the LICENSE file in the project root for full license information.

-- 创建版本跟踪表
CREATE TABLE `system_version_track`
(
    `id` BIGINT AUTO_INCREMENT NOT NULL COMMENT 'ID',
    `name` VARCHAR(128) COMMENT '项目名称',
    `curr_version` VARCHAR(128) NOT NULL COMMENT '项目版本号',
    `description` VARCHAR(512) COMMENT '描述',
    `author` VARCHAR(128) COMMENT '作者',
    `del_flag` VARCHAR(1) DEFAULT 0 NOT NULL COMMENT '删除标志;1：已删除，0：未删除',
    `create_by` VARCHAR(32) COMMENT '创建人',
    `creator_id` BIGINT COMMENT '创建人id',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by` VARCHAR(32) COMMENT '更新人',
    `updater_id` BIGINT COMMENT '更新人id',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '更新时间',
    `remark` VARCHAR(512) COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='版本跟踪';
