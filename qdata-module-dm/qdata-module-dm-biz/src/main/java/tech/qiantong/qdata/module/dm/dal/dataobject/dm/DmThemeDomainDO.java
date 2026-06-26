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

package tech.qiantong.qdata.module.dm.dal.dataobject.dm;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 主题域管理 DO 对象 DM_THEME_DOMAIN
 *
 * @author FXB
 * @date 2026-03-24
 */
@Data
@TableName(value = "DM_THEME_DOMAIN")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DM_THEME_DOMAIN_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DmThemeDomainDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 层级编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 英文缩写
     */
    private String engName;

    /**
     * 关联上级ID
     */
    private Long parentId;

    /**
     * 负责人ID
     */
    private Long ownerUserId;

    /**
     * 数仓分层ID
     */
    private Long dataLayerId;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否有效
     */
    private Boolean validFlag;

    /**
     * 删除标志
     */
    @TableLogic
    private Boolean delFlag;


    /**
     * 负责人名称
     */
    @TableField(exist = false)
    private String ownerUserName;

    /**
     * 数仓分层名称
     */
    @TableField(exist = false)
    private String dataLayerName;

    /**
     * 负责人联系方式
     */
    @TableField(exist = false)
    private String ownerUserPhoneNumber;

    /**
     * 统计数量
     */
    @TableField(exist = false)
    private Long num;
}
