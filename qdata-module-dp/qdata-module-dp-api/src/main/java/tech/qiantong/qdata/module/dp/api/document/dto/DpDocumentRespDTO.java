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

package tech.qiantong.qdata.module.dp.api.document.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Standard Document Registration DTO - DP_DOCUMENT
 *
 * @author qdata
 * @date 2025-08-21
 */
@Data
public class DpDocumentRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Code */
    private String code;

    /** Name */
    private String name;

    /** Category Code */
    private String catCode;
    private String catName;

    /** Document Standard Type */
    private String type;

    /** Document Status */
    private String status;

    /** Issuing Agency, e.g. “Standardization Administration of China” */
    private String issuingAgency;

    /** Version */
    private String version;

    /** Release Date */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date releaseDate;

    /** Implementation Date */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date implementationDate;

    /** Abolition Date */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date abolitionDate;

    /** File URL */

    private String fileUrl;
    private String fileName;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


    /** Description */
    private String description;


}
