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

package tech.qiantong.qdata.module.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 系统配置 创建/修改 Request VO system_content
 *
 * @author qdata
 * @date 2024-12-31
 */
@Schema(description = "系统配置 Response VO")
@Data
public class SystemContentSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "系统名称", example = "")
    @Size(max = 256, message = "系统名称长度不能超过256个字符")
    private String sysName;

    @Schema(description = "loginLogo", example = "")
    @Size(max = 256, message = "loginLogo长度不能超过256个字符")
    private String loginLogo;

    @Schema(description = "logo", example = "")
    @Size(max = 256, message = "logo长度不能超过256个字符")
    private String logo;

    @Schema(description = "轮播图", example = "")
    @Size(max = 256, message = "轮播图长度不能超过256个字符")
    private String carouselImage;

    @Schema(description = "联系电话", example = "")
    @Size(max = 256, message = "联系电话长度不能超过256个字符")
    private String contactNumber;

    @Schema(description = "电子邮箱", example = "")
    @Size(max = 256, message = "电子邮箱长度不能超过256个字符")
    private String email;

    @Schema(description = "版权方", example = "")
    @Size(max = 256, message = "版权方长度不能超过256个字符")
    private String copyright;

    @Schema(description = "备案号", example = "")
    @Size(max = 256, message = "备案号长度不能超过256个字符")
    private String recordNumber;

    @Schema(description = "状态", example = "")
    private Integer status;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remarks;


}
