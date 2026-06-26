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

package tech.qiantong.qdata.module.att.api.Tag.dto;

import lombok.Data;

/**
 * 标签管理 DTO 对象 ATT_TAG
 *
 * @author qdata
 * @date 2025-07-11
 */
@Data
public class AttTagRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 类目编码 */
    private String catCode;

    /** 资产数量 */
    private Long aeestCount;

    /** 状态 */
    private String status;

    /** 扩展信息别名 */
    private String allas;

    /** 近义词 */
    private String nearSynonyms;

    /** 同义词 */
    private String synonyms;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
