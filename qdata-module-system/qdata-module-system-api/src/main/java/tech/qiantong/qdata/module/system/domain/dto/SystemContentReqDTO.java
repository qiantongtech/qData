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

package tech.qiantong.qdata.module.system.domain.dto;

import lombok.Data;

/**
 * System Config DTO object system_content
 *
 * @author qdata
 * @date 2024-12-31
 */
@Data
public class SystemContentReqDTO {

    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** System Name */
    private String sysName;

    /** Logo */
    private String logo;

    /** Carousel Image */
    private String carouselImage;

    /** Contact Number */
    private String contactNumber;

    /** Email */
    private String email;

    /** Copyright */
    private String copyright;

    /** Record Number */
    private String recordNumber;

    /** Delete Flag */
    private Boolean delFlag;

    /** Status */
    private Integer status;

    /** Remark */
    private String remarks;


}
