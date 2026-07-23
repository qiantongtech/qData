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

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * System Config Response VO object system_content
 *
 * @author qdata
 * @date 2024-12-31
 */
@Schema(description = "System Config Response VO")
@Data
public class SystemContentRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "System Name")
    @Schema(description = "System Name", example = "")
    private String sysName;

    @Excel(name = "loginLogo")
    @Schema(description = "loginLogo", example = "")
    private String loginLogo;

    @Excel(name = "logo")
    @Schema(description = "logo", example = "")
    private String logo;

    @Excel(name = "Carousel Image")
    @Schema(description = "Carousel Image", example = "")
    private String carouselImage;

    @Excel(name = "Contact Number")
    @Schema(description = "Contact Number", example = "")
    private String contactNumber;

    @Excel(name = "Email")
    @Schema(description = "Email", example = "")
    private String email;

    @Excel(name = "Copyright")
    @Schema(description = "Copyright", example = "")
    private String copyright;

    @Excel(name = "Record Number")
    @Schema(description = "Record Number", example = "")
    private String recordNumber;

    @Excel(name = "Delete Flag")
    @Schema(description = "Delete Flag", example = "")
    private Boolean delFlag;

    @Excel(name = "Status")
    @Schema(description = "Status", example = "")
    private Integer status;

    @Excel(name = "Created By")
    @Schema(description = "Created By", example = "")
    private String createBy;

    @Excel(name = "Creator ID")
    @Schema(description = "Creator ID", example = "")
    private Long creatorId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Created Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Created Time", example = "")
    private Date createTime;

    @Excel(name = "Updated By")
    @Schema(description = "Updated By", example = "")
    private String updateBy;

    @Excel(name = "Updater ID")
    @Schema(description = "Updater ID", example = "")
    private Long updaterId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Updated Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Updated Time", example = "")
    private Date updateTime;

    @Excel(name = "Remark")
    @Schema(description = "Remark", example = "")
    private String remarks;

}
