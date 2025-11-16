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

package tech.qiantong.qdata.module.ds.api.apply.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * API服务-申请 DTO 对象 DS_API_APPLY
 *
 * @author qdata
 * @date 2025-04-22
 */
@Data
public class DsApiApplyRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    private List<Long> idList;

    /** API id */
    private String apiId;

    /** 申请人 */
    private String applyBy;

    /** 申请人手机号码 */
    private String applyByPhone;

    /** 申请人部门 */
    private String applyByDeptName;

    /** 申请人部门集合 */
    private List<Long> applyByDeptIdList;

    /** 申请时间 */
    private Date applyTime;

    /** 申请理由 */
    private String applyReason;

    /** 审批人 */
    private String approverBy;

    /** 审批时间 */
    private Date approvalTime;

    /** 审批理由 */
    private String approvalReason;

    /** 有效期类型 */
    private String validType;

    /** 时效开始时间 */
    private Date validStartTime;

    /** 时效结束时间 */
    private Date validEndTime;

    /** 流程状态，0：审批中，1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    private String apiName;

    private String apiUrl;

    /** 流程业务实例id */
    private String processInstanceId;


    private Long creatorId;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /** 区域id */
    private String rpAreaDictId;

    /** 区域id */
    private List<Long> rpAreaDictIdList;

    /** 区域id */
    private String rpAreaDictName;

    private Object dsApi;
}
