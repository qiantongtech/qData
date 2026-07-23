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

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.domain.entity.SysDept;

import java.util.List;

/**
 * Department Management Data Layer
 *
 * @author qdata
 */
public interface SysDeptMapper
{
    /**
     * Query department management data
     *
     * @param dept department information
     * @return department information collection
     */
    public List<SysDept> selectDeptList(SysDept dept);

    public List<SysDept> selectDeptListAll(SysDept dept);

    /**
     * Query department data by parent ID
     *
     * @param dept department information
     * @return department information collection
     */
    public List<SysDept> selectDeptListByParentId(SysDept dept);

    /**
     * Query department tree information by role ID
     *
     * @param roleId role ID
     * @param deptCheckStrictly whether department tree selection items are associated
     * @return selected department list
     */
    public List<Long> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

    /**
     * Query department information by department ID
     *
     * @param deptId department ID
     * @return department information
     */
    public SysDept selectDeptById(Long deptId);

    /**
     * Query all child departments by ID
     *
     * @param deptId department ID
     * @return department list
     */
    public List<SysDept> selectChildrenDeptById(Long deptId);

    /**
     * Query all child departments by ID (normal status)
     *
     * @param deptId department ID
     * @return child department count
     */
    public int selectNormalChildrenDeptById(Long deptId);

    /**
     * Check if child nodes exist
     *
     * @param deptId department ID
     * @return result
     */
    public int hasChildByDeptId(Long deptId);

    /**
     * Check if the department has users
     *
     * @param deptId department ID
     * @return result
     */
    public int checkDeptExistUser(Long deptId);

    /**
     * Validate whether department name is unique
     *
     * @param deptName department name
     * @param parentId parent department ID
     * @return result
     */
    public SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

    /**
     * Insert department information
     *
     * @param dept department information
     * @return result
     */
    public int insertDept(SysDept dept);

    /**
     * Update department information
     *
     * @param dept department information
     * @return result
     */
    public int updateDept(SysDept dept);

    /**
     * Update department normal status
     *
     * @param deptIds department ID array
     */
    public void updateDeptStatusNormal(Long[] deptIds);

    /**
     * Update child element relationships
     *
     * @param depts child elements
     * @return result
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * Delete department management information
     *
     * @param deptId department ID
     * @return result
     */
    public int deleteDeptById(Long deptId);
}
