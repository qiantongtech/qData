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

package tech.qiantong.qdata.module.att.api.project.dto;

import lombok.Data;

/**
 * Project DTO ATT_PROJECT
 *
 * @author shu
 * @date 2025-01-20
 */
@Data
public class AttProjectReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Project Name */
    private String name;

    /** Project Code */
    private String code;

    /** Datasource ID */
    private Long datasourceId;

    /** Project Manager ID */
    private Long managerId;

    /** Project Description */
    private String description;

    /** Valid Flag */
    private Boolean validFlag;

    /** Deletion Flag */
    private Boolean delFlag;


}
