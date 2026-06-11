/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
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
public class DsApiLogRespDTO {

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
