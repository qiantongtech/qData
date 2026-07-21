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

package tech.qiantong.qdata.module.ds.dal.dataobject.apiLog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.time.LocalDateTime;

/**
 * API service call log DO DS_API_LOG
 *
 * @author lhs
 * @date 2025-02-12
 */
@Data
@TableName(value = "DS_API_LOG")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, and H2; optional for databases such as MySQL.
// @KeySequence("DS_API_LOG_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DsApiLogDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Category ID */
    private Long catId;

    /** Category code */
    private String catCode;

    /** Category name */
    @TableField(exist = false)
    private String catName;

    /** Called API service ID */
    private Long apiId;

    @TableField(exist = false)
    private String apiName;

    @TableField(exist = false)
    private String reqMethod;

    /** Caller ID */
    private String callerId;

    /** Caller */
    private String callerBy;

    /** Caller IP */
    private String callerIp;

    /** Called URL */
    private String callerUrl;

    /** Call parameters */
    private String callerParams;

    /** Call start time */
    private LocalDateTime callerStartDate;

    /** Call end time */
    private LocalDateTime callerEndDate;

    /** Called record count */
    private int callerSize;

    /** Call duration in milliseconds */
    private Long callerTime;

    /** Information record */
    private String msg;

    /** Status */
    private Integer status;

    /** Whether the record is active */
    private Boolean validFlag;

    /**
     * Response parameters
     */
    private String fieldParameters;

    /** Deletion flag */
    @TableLogic
    private Boolean delFlag;



}
