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

package tech.qiantong.qdata.module.system.service;

import tech.qiantong.qdata.common.core.domain.TreeSelect;
import tech.qiantong.qdata.common.core.domain.entity.SysDept;

import java.util.List;

/**
 * Department management service layer
 *
 * @author qdata
 */
public interface ISysDeptService
{
    /**
     * Query department management data
     *
     * @param dept department information
     * @return department information collection
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * Query department tree structure information
     *
     * @param dept department information
     * @return department tree information collection
     */
    public List<TreeSelect> selectDeptTreeList(SysDept dept);

    /**
     * Build tree structure required by frontend
     *
     * @param depts department list
     * @return tree structure list
     */
    public List<SysDept> buildDeptTree(List<SysDept> depts);

    /**
     * Build dropdown tree structure required by frontend
     *
     * @param depts department list
     * @return dropdown tree structure list
     */
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts);

    /**
     * Query department tree information by role ID
     *
     * @param roleId role ID
     * @return selected department list
     */
    public List<Long> selectDeptListByRoleId(Long roleId);

    /**
     * Query department information by ID
     *
     * @param deptId department ID
     * @return department information
     */
    public SysDept selectDeptById(Long deptId);

    /**
     * Query all child departments by ID (normal status)
     *
     * @param deptId department ID
     * @return number of child departments
     */
    public int selectNormalChildrenDeptById(Long deptId);

    /**
     * Check if department has child nodes
     *
     * @param deptId department ID
     * @return result
     */
    public boolean hasChildByDeptId(Long deptId);

    /**
     * Check if department has users
     *
     * @param deptId department ID
     * @return result true exists, false does not exist
     */
    public boolean checkDeptExistUser(Long deptId);

    /**
     * Check if department name is unique
     *
     * @param dept department information
     * @return result
     */
    public boolean checkDeptNameUnique(SysDept dept);

    /**
     * Check if department has data scope permission
     *
     * @param deptId department ID
     */
    public void checkDeptDataScope(Long deptId);

    /**
     * Insert and save department information
     *
     * @param dept department information
     * @return result
     */
    public int insertDept(SysDept dept);

    /**
     * Update and save department information
     *
     * @param dept department information
     * @return result
     */
    public int updateDept(SysDept dept);

    /**
     * Delete department management information
     *
     * @param deptId department ID
     * @return result
     */
    public int deleteDeptById(Long deptId);
}
