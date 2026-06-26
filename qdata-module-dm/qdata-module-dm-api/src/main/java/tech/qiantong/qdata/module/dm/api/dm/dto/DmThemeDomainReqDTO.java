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

package tech.qiantong.qdata.module.dm.api.dm.dto;

import lombok.*;

/**
 * 主题域管理 DTO 对象 DM_THEME_DOMAIN
 *
 * @author FXB
 * @date 2026-03-24
 */
@Data
public class DmThemeDomainReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 层级编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 英文缩写 */
    private String engName;

    /** 关联上级ID */
    private Long parentId;

    /** 负责人ID */
    private Long ownerUserId;

    /** 数仓分层ID */
    private Long dataLayerId;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
