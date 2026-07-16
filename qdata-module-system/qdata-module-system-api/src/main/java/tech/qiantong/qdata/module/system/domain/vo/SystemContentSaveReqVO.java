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
 * System Config Create/Update Request VO system_content
 *
 * @author qdata
 * @date 2024-12-31
 */
@Schema(description = "System Config Response VO")
@Data
public class SystemContentSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "System Name", example = "")
    @Size(max = 256, message = "System Name must not exceed 256 characters")
    private String sysName;

    @Schema(description = "loginLogo", example = "")
    @Size(max = 256, message = "loginLogo must not exceed 256 characters")
    private String loginLogo;

    @Schema(description = "logo", example = "")
    @Size(max = 256, message = "logo must not exceed 256 characters")
    private String logo;

    @Schema(description = "Carousel Image", example = "")
    @Size(max = 256, message = "Carousel Image must not exceed 256 characters")
    private String carouselImage;

    @Schema(description = "Contact Number", example = "")
    @Size(max = 256, message = "Contact Number must not exceed 256 characters")
    private String contactNumber;

    @Schema(description = "Email", example = "")
    @Size(max = 256, message = "Email must not exceed 256 characters")
    private String email;

    @Schema(description = "Copyright", example = "")
    @Size(max = 256, message = "Copyright must not exceed 256 characters")
    private String copyright;

    @Schema(description = "Record Number", example = "")
    @Size(max = 256, message = "Record Number must not exceed 256 characters")
    private String recordNumber;

    @Schema(description = "Status", example = "")
    private Integer status;

    @Schema(description = "Remark", example = "")
    @Size(max = 256, message = "Remark must not exceed 256 characters")
    private String remarks;


}
