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
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
