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

package tech.qiantong.qdata.module.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    /**
     * User ID, required
     */
    private String id;
    /**
     * Direct Leader ID
     */
    private String parentId;

    /**
     * User Name, required
     */
    private String name;
    /**
     * User Avatar URL, required
     */
    private String avatarUrl;
    /**
     * Department ID List, optional
     */
    private List<String> deptIdList;
    /**
     * User Status: 0 = Disabled, 1 = Enabled
     */
    private Integer status;
    private String token;

    /**
     * Phone Number
     */
    private String phone;
    /**
     * Department Name
     */
    private String deptName;


}
