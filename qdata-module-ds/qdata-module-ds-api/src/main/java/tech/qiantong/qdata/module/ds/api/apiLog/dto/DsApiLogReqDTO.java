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

package tech.qiantong.qdata.module.ds.api.apiLog.dto;

import lombok.Data;

import java.util.Date;

/**
 * API服务调用日志 DTO 对象 DS_API_LOG
 *
 * @author lhs
 * @date 2025-02-12
 */
@Data
public class DsApiLogReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long ID;

    /** 调用API服务Id */
    private String apiId;

    /** 调用者id */
    private String callerId;

    /** 调用者 */
    private Long callerBy;

    /** 调用者ip */
    private String callerIp;

    /** 调用url */
    private String callerUrl;

    /** 调用参数 */
    private String callerParams;

    /** 调用开始时间 */
    private Date callerStartDate;

    /** 调用结束时间 */
    private Date callerEndDate;

    /** 调用数据量 */
    private Long callerSize;

    /** 调用耗时(毫秒) */
    private Long callerTime;

    /** 信息记录 */
    private String MSG;

    /** 状态 */
    private String STATUS;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
