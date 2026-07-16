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

package tech.qiantong.qdata.module.system.mapper;

import tech.qiantong.qdata.module.system.domain.SysRoleDept;

import java.util.List;

/**
 * Role-department association table data layer
 *
 * @author qdata
 */
public interface SysRoleDeptMapper
{
    /**
     * Delete role-department association by role ID
     *
     * @param roleId role ID
     * @return result
     */
    public int deleteRoleDeptByRoleId(Long roleId);

    /**
     * Batch delete role-department association information
     *
     * @param ids data IDs to delete
     * @return result
     */
    public int deleteRoleDept(Long[] ids);

    /**
     * Query department usage count
     *
     * @param deptId department ID
     * @return result
     */
    public int selectCountRoleDeptByDeptId(Long deptId);

    /**
     * Batch insert role-department information
     *
     * @param roleDeptList role-department list
     * @return result
     */
    public int batchRoleDept(List<SysRoleDept> roleDeptList);
}
