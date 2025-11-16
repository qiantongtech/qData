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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.ds.dal.dataobject.apiLog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.time.LocalDateTime;

/**
 * API服务调用日志 DO 对象 DS_API_LOG
 *
 * @author lhs
 * @date 2025-02-12
 */
@Data
@TableName(value = "DS_API_LOG")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
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

    /** 类目ID */
    private Long catId;

    /** 类目编码 */
    private String catCode;

    /** 类目名称 */
    @TableField(exist = false)
    private String catName;

    /** 调用API服务Id */
    private Long apiId;

    @TableField(exist = false)
    private String apiName;

    @TableField(exist = false)
    private String reqMethod;

    /** 调用者id */
    private String callerId;

    /** 调用者 */
    private String callerBy;

    /** 调用者ip */
    private String callerIp;

    /** 调用url */
    private String callerUrl;

    /** 调用参数 */
    private String callerParams;

    /** 调用开始时间 */
    private LocalDateTime callerStartDate;

    /** 调用结束时间 */
    private LocalDateTime callerEndDate;

    /** 调用数据量 */
    private int callerSize;

    /** 调用耗时(毫秒) */
    private Long callerTime;

    /** 信息记录 */
    private String msg;

    /** 状态 */
    private Integer status;

    /** 是否有效 */
    private Boolean validFlag;

    /**
     * 返回参数
     */
    private String fieldParameters;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;



}
