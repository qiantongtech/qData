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

package tech.qiantong.qdata.module.system.api.updater.dto;

import lombok.*;

/**
 * 版本跟踪 DTO object SYSTEM_VERSION_TRACK
 *
 * @author qdata
 * @date 2026-08-12
 */
@Data
public class SystemVersionTrackReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 项目名称 */
    private String name;

    /** 项目版本号 */
    private String currVersion;

    /** 描述 */
    private String description;

    /** 作者 */
    private String author;

    /** 删除标志 */
    private Boolean delFlag;


}
