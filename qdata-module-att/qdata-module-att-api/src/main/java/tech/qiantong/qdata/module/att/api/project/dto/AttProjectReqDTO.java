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

package tech.qiantong.qdata.module.att.api.project.dto;

import lombok.Data;

/**
 * 项目 DTO 对象 ATT_PROJECT
 *
 * @author shu
 * @date 2025-01-20
 */
@Data
public class AttProjectReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 项目名称 */
    private String name;

    /** 项目编码 */
    private String code;

    /** 数据源id */
    private Long datasourceId;

    /** 项目管理员id */
    private Long managerId;

    /** 项目描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
