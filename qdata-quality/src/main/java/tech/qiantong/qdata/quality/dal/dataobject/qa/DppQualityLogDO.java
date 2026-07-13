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

package tech.qiantong.qdata.quality.dal.dataobject.qa;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Data Quality Log DO object DPP_QUALITY_LOG
 *
 * @author qdata
 * @date 2025-07-19
 */
@Data
@TableName(value = "DPP_QUALITY_LOG")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
// @KeySequence("DPP_QUALITY_LOG_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppQualityLogDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** name */
    private String name;

    /** status */
    private String successFlag;

    /** Start time */
    private Date startTime;

    /** End time */
    private Date endTime;

    /** task id */
    private String qualityId;

    /** Rating */
    private Long score;

    /** Question data */
    private Long problemData;

    /** Is it valid */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;

    private String path;


}
