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
     * 用户id 不能为空
     */
    private String id;
    /**
     * 直属领导id
     */
    private String parentId;

    /**
     * 用户姓名 不能为空
     */
    private String name;
    /**
     * 用户头像 不能为空
     */
    private String avatarUrl;
    /**
     * 用户所属部门id 可以为空
     */
    private List<String> deptIdList;
    /**
     * 用户状态 0禁用 1启用
     */
    private Integer status;
    private String token;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 部门名称
     */
    private String deptName;


}
