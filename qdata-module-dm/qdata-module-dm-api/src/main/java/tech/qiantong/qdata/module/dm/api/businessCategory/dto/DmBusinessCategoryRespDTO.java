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

package tech.qiantong.qdata.module.dm.api.businessCategory.dto;

import lombok.Data;

/**
 * 业务分类 DTO 对象 DM_BUSINESS_CATEGORY
 *
 * @author qdata
 * @date 2026-04-08
 */
@Data
public class DmBusinessCategoryRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 层级编码 */
    private String code;

    /** 业务分类名称 */
    private String name;

    /** 关联上级ID */
    private Long parentId;

    /** 排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 英文缩写名 */
    private String engName;

    /** 负责人手机号 */
    private String ownerPhone;

    /** 负责人ID */
    private Long ownerId;

    /** 数据域ID */
    private Long domainId;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    private Boolean delFlag;


}
