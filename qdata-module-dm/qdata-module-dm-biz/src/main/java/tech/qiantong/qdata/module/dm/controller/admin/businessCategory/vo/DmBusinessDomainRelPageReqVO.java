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

package tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 业务分类数据域关联关系 Request VO 对象 DM_BUSINESS_DOMAIN_REL
 *
 * @author qdata
 * @date 2026-04-12
 */
@Schema(description = "业务分类数据域关联关系 Request VO")
@Data
public class DmBusinessDomainRelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "业务分类ID", example = "")
    private Long businessCategoryId;

    @Schema(description = "数据域ID", example = "")
    private Long dataDomainId;

    @Schema(description = "业务分类名称", example = "")
    private String businessCategoryName;

    @Schema(description = "数据域名称", example = "")
    private String dataDomainName;

    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;



}
