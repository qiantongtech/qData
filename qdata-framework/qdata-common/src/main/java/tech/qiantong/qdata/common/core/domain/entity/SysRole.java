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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.annotation.Excel.ColumnType;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Role table sys_role
 *
 * @author qdata
 */
public class SysRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Role ID */
    @Excel(name = "角色序号", cellType = ColumnType.NUMERIC)
    private Long roleId;

    /** Role name */
    @Excel(name = "角色名称")
    private String roleName;

    /** Role permissions */
    @Excel(name = "角色权限")
    private String roleKey;

    /** Role sorting */
    @Excel(name = "角色排序")
    private Integer roleSort;

    /** Data scope (1: All data permissions; 2: Customized data permissions; 3: Data permissions for this department; 4: Data permissions for this department and below; 5: Only my data permissions) */
    @Excel(name = "数据范围", readConverterExp = "1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private String dataScope;

    /** Whether the menu tree selection items are displayed in association (0: parent and child are not displayed in association with each other 1: parent and child are displayed in association with each other) */
    private boolean menuCheckStrictly;

    /** Whether the department tree selection items are displayed in association (0: parent and child are not displayed in association with each other 1: parent and child are displayed in association with each other) */
    private boolean deptCheckStrictly;

    /** Character status (0 normal 1 disabled) */
    @Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** Delete flag (0 represents existence, 2 represents deletion) */
    private String delFlag;

    /** Whether this role ID exists for the user. It does not exist by default */
    private boolean flag = false;

    /** Menu group */
    private Long[] menuIds;

    /** Department group (data permissions) */
    private Long[] deptIds;

    /** Role menu permissions */
    private Set<String> permissions;

    /** old id */
    private Long oldRoleId;

    public Long getOldRoleId() {
        return oldRoleId;
    }

    public void setOldRoleId(Long oldRoleId) {
        this.oldRoleId = oldRoleId;
    }

    /**
     * Project management id
     */
    private Long projectId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public SysRole()
    {

    }

    public SysRole(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.roleId);
    }

    public static boolean isAdmin(Long roleId)
    {
        return roleId != null && 1L == roleId;
    }

    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    public String getRoleKey()
    {
        return roleKey;
    }

    public void setRoleKey(String roleKey)
    {
        this.roleKey = roleKey;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getRoleSort()
    {
        return roleSort;
    }

    public void setRoleSort(Integer roleSort)
    {
        this.roleSort = roleSort;
    }

    public String getDataScope()
    {
        return dataScope;
    }

    public void setDataScope(String dataScope)
    {
        this.dataScope = dataScope;
    }

    public boolean isMenuCheckStrictly()
    {
        return menuCheckStrictly;
    }

    public void setMenuCheckStrictly(boolean menuCheckStrictly)
    {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    public boolean isDeptCheckStrictly()
    {
        return deptCheckStrictly;
    }

    public void setDeptCheckStrictly(boolean deptCheckStrictly)
    {
        this.deptCheckStrictly = deptCheckStrictly;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    public Long[] getMenuIds()
    {
        return menuIds;
    }

    public void setMenuIds(Long[] menuIds)
    {
        this.menuIds = menuIds;
    }

    public Long[] getDeptIds()
    {
        return deptIds;
    }

    public void setDeptIds(Long[] deptIds)
    {
        this.deptIds = deptIds;
    }

    public Set<String> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(Set<String> permissions)
    {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("roleKey", getRoleKey())
            .append("roleSort", getRoleSort())
            .append("dataScope", getDataScope())
            .append("menuCheckStrictly", isMenuCheckStrictly())
            .append("deptCheckStrictly", isDeptCheckStrictly())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
