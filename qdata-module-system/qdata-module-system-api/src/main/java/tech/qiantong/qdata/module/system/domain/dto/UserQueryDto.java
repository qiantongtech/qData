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

/**
 * @author zhj
 * @version 1.0
 * @description: 查询用户参数
 * @date 2024/2/22 17:13
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserQueryDto extends PageDto {
    /**
     * 部门id
     */
    private String deptId;
    /**
     * 搜索词
     */
    private String keywords;
    /**
     * 用户状态
     */
    private Integer status;

    private String name;
    /**
     * 部门id集合
     */
    private List<String> deptIdList;
    /**
     * 角色key集合
     */
    private List<String> roleIdList;
}
