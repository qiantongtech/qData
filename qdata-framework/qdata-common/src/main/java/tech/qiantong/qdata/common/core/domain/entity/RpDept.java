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

package tech.qiantong.qdata.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Resource Portal Department DO Object RP_DEPT
 *
 * @author qdata
 * @date 2025-04-18
 */
@Data
public class RpDept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** Department id */
    private Long deptId;

    /** Parent department id */
    private Long parentId;

    /** Ancestor list */
    private String ancestors;

    /** Department name */
    private String deptName;
    private String deptCode;
    private String deptType;

    /** Display order */
    private String orderNum;

    /** Person in charge */
    private String leader;

    /** Contact number */
    private String phone;

    /** Email */
    private String email;

    /** Department status (0 normal 1 disabled) */
    private String status;

    /** Is it valid */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;

    private List<RpDept> children = new ArrayList<RpDept>();

}
