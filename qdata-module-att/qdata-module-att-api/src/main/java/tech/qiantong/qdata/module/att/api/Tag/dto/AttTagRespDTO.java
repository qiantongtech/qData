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
 * Tag Management DTO ATT_TAG
 *
 * @author qdata
 * @date 2025-07-11
 */
@Data
public class AttTagRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Code */
    private String code;

    /** Name */
    private String name;

    /** Description */
    private String description;

    /** Category Code */
    private String catCode;

    /** Asset Count */
    private Long aeestCount;

    /** Status */
    private String status;

    /** Extended Info Alias */
    private String allas;

    /** Near Synonyms */
    private String nearSynonyms;

    /** Synonyms */
    private String synonyms;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
