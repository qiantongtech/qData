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

package tech.qiantong.qdata.module.att.api.project.dto;

import lombok.Data;

/**
 * 项目 DTO 对象 ATT_PROJECT
 *
 * @author shu
 * @date 2025-01-20
 */
@Data
public class AttProjectRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 项目名称 */
    private String name;

    /** 项目管理员id */
    private Long managerId;

    /** 项目描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    /** 是否分配到数据研发 */
    private Boolean dppAssigned;

    /** 项目编码 */
    private String code;

    /** 项目管理员 */
    private String nickName;
    /** 项目管理员手机号 */
    private String managerPhone;


}
