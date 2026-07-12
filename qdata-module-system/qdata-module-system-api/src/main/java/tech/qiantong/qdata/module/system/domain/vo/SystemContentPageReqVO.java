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
 * System Config Request VO object system_content
 *
 * @author qdata
 * @date 2024-12-31
 */
@Schema(description = "System Config Request VO")
@Data
public class SystemContentPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Integer id;
    @Schema(description = "System Name", example = "")
    private String sysName;

    @Schema(description = "Logo", example = "")
    private String logo;

    @Schema(description = "Carousel Image", example = "")
    private String carouselImage;

    @Schema(description = "Contact Number", example = "")
    private String contactNumber;

    @Schema(description = "Email", example = "")
    private String email;

    @Schema(description = "Copyright", example = "")
    private String copyright;

    @Schema(description = "Record Number", example = "")
    private String recordNumber;


    @Schema(description = "Status", example = "")
    private Integer status;

    @Schema(description = "Remark", example = "")
    private String remark;


}
