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

package tech.qiantong.qdata.module.dp.controller.admin.document.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <P>
 * Purpose: Standard Search Response VO
 * </p>
 *
 * @author: FXB
 * @create: 2025-08-22 10:08
 **/
@Data
public class DpDocumentSearchRespVO implements Serializable {

    private static final long serialVersionUID = -4634002019134354679L;

    /**
     * Data type 1: Standard, 2: Logical Model, 3: Data Element, 4: Code Table
     */
    private String dataType;

    /**
     * ID
     */
    private Long id;

    /**
     * Code
     */
    private String code;

    /**
     * Standard Name
     */
    private String name;

    /**
     * Category Name
     */
    private String catName;

    /**
     * Document standard type; 1-National Standard, 2-Industry Standard, 3-Local Standard, 4-Group Standard. Dictionary: dp_document_type
     */
    private String type;

    /**
     * Document status (Standard Status), Dictionary: dp_document_status
     */
    private String status;

    /**
     * Release Date
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    /**
     * Implementation Date
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date implementationDate;

    /**
     * File URL
     */
    private String fileUrl;

    /**
     * File Name
     */
    private String fileName;
}
