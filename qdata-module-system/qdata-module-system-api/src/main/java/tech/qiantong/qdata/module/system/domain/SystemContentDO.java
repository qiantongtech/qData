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

package tech.qiantong.qdata.module.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * System configuration DO object system_content
 *
 * @author qdata
 * @date 2024-12-31
 */
@Data
@TableName(value = "system_content")
// Auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Not needed for MySQL, etc.
// @KeySequence("system_content_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SystemContentDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** System name */
    private String sysName;

    /** logo */
    private String loginLogo;

    private String logo;

    /** Carousel images */
    private String carouselImage;

    /** Contact number */
    private String contactNumber;

    /** Email address */
    private String email;

    /** Copyright holder */
    private String copyright;

    /** Record number (ICP filing) */
    private String recordNumber;

    /** Deletion flag */
    @TableLogic
    private Boolean delFlag;

    /** Status */
    private Integer status;

    /** Remarks */
    private String remark;


}
