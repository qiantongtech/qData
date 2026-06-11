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

package tech.qiantong.qdata.module.att.api.cat.dto;

import lombok.Data;

/**
 * 清洗规则类目 DTO 对象 ATT_CLEAN_CAT
 *
 * @author qdata
 * @date 2025-08-11
 */
@Data
public class AttCleanCatReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long ID;

    /** 类别名称 */
    private String name;

    /** 关联上级ID */
    private Long parentId;

    /** 类别排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 层级编码 */
    private String CODE;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
