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

package tech.qiantong.qdata.module.dpp.api.etl.dto;

import lombok.Data;

import java.util.Date;

/**
 * 数据质量日志 DTO 对象 DPP_QUALITY_LOG
 *
 * @author qdata
 * @date 2025-07-19
 */
@Data
public class DppQualityLogReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 名称 */
    private String name;

    /** 状态 */
    private String successFlag;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 任务id */
    private String qualityId;

    /** 评分 */
    private Long score;

    /** 问题数据 */
    private Long problemData;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
