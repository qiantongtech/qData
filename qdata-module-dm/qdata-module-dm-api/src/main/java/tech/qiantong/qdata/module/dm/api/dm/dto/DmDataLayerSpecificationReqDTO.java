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

package tech.qiantong.qdata.module.dm.api.dm.dto;

import lombok.*;

/**
 * 数仓分层-规范管理 DTO 对象 DM_DATA_LAYER_SPECIFICATION
 *
 * @author FXB
 * @date 2026-03-24
 */
@Data
public class DmDataLayerSpecificationReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 数仓分层ID */
    private Long dataLayerId;

    /** 表前缀 */
    private String prefixName;

    /** 业务大类英文缩写 */
    private String businessEngName;

    /** 负责人ID */
    private Long ownerUserId;

    /** 状态 */
    private String status;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
