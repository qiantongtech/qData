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

package tech.qiantong.qdata.module.dp.dal.dataobject.document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Standard Document Registration DO - DP_DOCUMENT
 *
 * @author qdata
 * @date 2025-08-21
 */
@Data
@TableName(value = "DP_DOCUMENT")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DP_DOCUMENT_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpDocumentDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Code */
    private String code;

    /** Name */
    private String name;

    /** Category code */
    private String catCode;

    @TableField(exist = false)
    private String catName;

    /** Document standard type field */
    private String type;

    /** Document status */
    private String status;

    /** Publishing organization name, for example, "Standardization Administration of China" */
    private String issuingAgency;

    /** Version */
    private String version;

    /** Publication date */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date releaseDate;

    /** Effective date */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date implementationDate;

    /** Abolition date */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date abolitionDate;

    /** Document URL */

    private String fileUrl;
    private String fileName;

    /** Whether the record is active */
    private Boolean validFlag;

    /** Deletion flag */
    @TableLogic
    private Boolean delFlag;

    /** Description */
    private String description;


}
