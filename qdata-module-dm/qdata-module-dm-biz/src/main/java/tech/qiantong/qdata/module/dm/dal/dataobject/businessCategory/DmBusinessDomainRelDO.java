/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 业务分类数据域关联关系 DO 对象 DM_BUSINESS_DOMAIN_REL
 *
 * @author qdata
 * @date 2026-04-12
 */
@Data
@TableName(value = "DM_BUSINESS_DOMAIN_REL")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DM_BUSINESS_DOMAIN_REL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DmBusinessDomainRelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 业务分类ID */
    private Long businessCategoryId;

    /** 数据域ID */
    private Long dataDomainId;

    /** 业务分类名称 */
    private String businessCategoryName;

    /** 数据域名称 */
    private String dataDomainName;

    /** 排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    @TableLogic
    private Boolean delFlag;


}
