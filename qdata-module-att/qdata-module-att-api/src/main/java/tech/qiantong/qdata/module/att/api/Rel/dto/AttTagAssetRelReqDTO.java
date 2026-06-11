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

package tech.qiantong.qdata.module.att.api.Rel.dto;

import lombok.Data;

/**
 * 标签与资产关联关系 DTO 对象 ATT_TAG_ASSET_REL
 *
 * @author qdata
 * @date 2025-07-11
 */
@Data
public class AttTagAssetRelReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long ID;

    /** 标签管理id */
    private String tagId;

    /** 资产id */
    private String assetId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
