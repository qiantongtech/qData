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

package tech.qiantong.qdata.module.da.api.assetchild.video.dto;

import lombok.Data;

/**
 * 数据资产-视频数据 DTO 对象 DA_ASSET_VIDEO
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
public class DaAssetVideoRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产id */
    private Long assetId;

    /** IP */
    private String ip;

    /** 端口号 */
    private Long port;

    /** 协议 */
    private String protocol;

    /** 平台 */
    private String platform;

    /** 配置JSON */
    private String config;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
