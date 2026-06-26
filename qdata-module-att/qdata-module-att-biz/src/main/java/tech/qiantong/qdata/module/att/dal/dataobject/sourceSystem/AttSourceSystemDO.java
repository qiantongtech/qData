/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 来源系统 DO 对象 ATT_SOURCE_SYSTEM
 *
 * @author qdata
 * @date 2026-04-03
 */
@Data
@TableName(value = "ATT_SOURCE_SYSTEM")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("ATT_SOURCE_SYSTEM_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttSourceSystemDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 系统名称 */
    private String name;

    /** 系统类型 */
    private String type;

    /** 排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 负责人 */
    private String responsiblePerson;

    /** 负责人名称 */
    @TableField(exist = false)
    private String responsiblePersonName;

    /** 对接人 */
    private String contactPerson;
    /** 对接人名称 */
    @TableField(exist = false)
    private String contactPersonName;


    /** 删除标志;1：已删除，0：未删除 */
    @TableLogic
    private Boolean delFlag;


}
