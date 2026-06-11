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

package tech.qiantong.qdata.module.da.api.datasource.dto;

import lombok.Data;

/**
 * 数据源与项目关联关系 DTO 对象 DA_DATASOURCE_PROJECT_REL
 *
 * @author qdata
 * @date 2025-03-13
 */
@Data
public class DaDatasourceProjectRelRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 项目id */
    private Long projectId;

    /** 项目编码 */
    private String projectCode;

    /** 数据源id */
    private Long datasourceId;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;


}
