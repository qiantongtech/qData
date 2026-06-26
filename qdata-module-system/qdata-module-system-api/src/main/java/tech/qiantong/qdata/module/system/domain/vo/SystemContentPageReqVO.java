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
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 系统配置 Request VO 对象 system_content
 *
 * @author qdata
 * @date 2024-12-31
 */
@Schema(description = "系统配置 Request VO")
@Data
public class SystemContentPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Integer id;
    @Schema(description = "系统名称", example = "")
    private String sysName;

    @Schema(description = "logo", example = "")
    private String logo;

    @Schema(description = "轮播图", example = "")
    private String carouselImage;

    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Schema(description = "电子邮箱", example = "")
    private String email;

    @Schema(description = "版权方", example = "")
    private String copyright;

    @Schema(description = "备案号", example = "")
    private String recordNumber;


    @Schema(description = "状态", example = "")
    private Integer status;

    @Schema(description = "备注", example = "")
    private String remark;


}
