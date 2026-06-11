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
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 业务分类数据域关联关系 创建/修改 Request VO DM_BUSINESS_DOMAIN_REL
 *
 * @author qdata
 * @date 2026-04-12
 */
@Schema(description = "业务分类数据域关联关系 Response VO")
@Data
public class DmBusinessDomainRelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "业务分类ID", example = "")
    private Long businessCategoryId;

    @Schema(description = "数据域ID", example = "")
    private Long dataDomainId;

    @Schema(description = "业务分类名称", example = "")
    @Size(max = 256, message = "业务分类名称长度不能超过256个字符")
    private String businessCategoryName;

    @Schema(description = "数据域名称", example = "")
    @Size(max = 256, message = "数据域名称长度不能超过256个字符")
    private String dataDomainName;

    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;


}
