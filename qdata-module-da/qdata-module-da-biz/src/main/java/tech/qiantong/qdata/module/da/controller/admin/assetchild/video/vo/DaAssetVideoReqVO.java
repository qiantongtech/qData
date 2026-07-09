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

package tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Asset-Video Request VO DA_ASSET_VIDEO
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-视频数据 Request VO")
@Data
public class DaAssetVideoReqVO {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "IP", example = "")
    private String ip;

    @Schema(description = "端口号", example = "")
    private Long port;

    @Schema(description = "协议", example = "")
    private String protocol;

    @Schema(description = "平台", example = "")
    private String platform;

    @Schema(description = "配置JSON", example = "")
    private String config;




}
