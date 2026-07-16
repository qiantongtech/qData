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

package tech.qiantong.qdata.module.dp.api.codeMap.dto;

import lombok.Data;

/**
 * Data Element Code Map DTO - DP_CODE_MAP
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpCodeMapRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Data Element ID */
    private String dataElemId;

    /** Original Value */
    private String originalValue;

    /** Code Name */
    private String codeName;

    /** Code Value */
    private String codeValue;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
