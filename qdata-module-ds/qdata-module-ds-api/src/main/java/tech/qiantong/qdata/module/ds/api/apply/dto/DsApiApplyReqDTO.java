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

package tech.qiantong.qdata.module.ds.api.apply.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * API service application DTO DS_API_APPLY
 *
 * @author qdata
 * @date 2025-04-22
 */
@Data
public class DsApiApplyReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    private List<Long> idList;

    /** API id */
    private String apiId;

    /** Applicant */
    private String applyBy;

    /** Applicant mobile number */
    private String applyByPhone;

    /** Applicant department */
    private String applyByDeptName;

    /** Applicant department collection */
    private List<Long> applyByDeptIdList;

    /** Application time */
    private Date applyTime;

    /** Application reason */
    private String applyReason;

    /** Approver */
    private String approverBy;

    /** Approval time */
    private Date approvalTime;

    /** Approval reason */
    private String approvalReason;

    /** Validity period type */
    private String validType;

    /** Validity start time */
    private Date validStartTime;

    /** Validity end time */
    private Date validEndTime;

    /** Process status (0: pending approval, 1: pending approval, 2: approved, 3: rejected, 4: withdrawn, 5: approval error) */
    private String status;

    /** Whether the record is active */
    private Boolean validFlag;

    /** Deletion flag */
    private Boolean delFlag;

    private String apiName;

    private String apiUrl;

    /** Process business instance ID */
    private String processInstanceId;


    private Long creatorId;

    /**
     * Creator
     */
    private String createBy;

    /**
     * Creation time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /** Region ID */
    private String rpAreaDictId;


    /** Region ID */
    private List<Long> rpAreaDictIdList;
}
