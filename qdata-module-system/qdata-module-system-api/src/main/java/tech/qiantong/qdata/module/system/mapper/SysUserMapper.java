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
import tech.qiantong.qdata.common.core.domain.entity.SysUser;

import java.util.List;
import java.util.Set;

/**
 * User table data layer
 *
 * @author qdata
 */
public interface SysUserMapper {
    /**
     * Paginate query user list by conditions
     *
     * @param sysUser User info
     * @return User info collection
     */
    public List<SysUser> selectUserList(SysUser sysUser);

    public List<SysUser> selectUserAllList(SysUser sysUser);

    /**
     * Query user list by dept ID
     *
     * @param sysUser User info
     * @return User info collection
     */
    public List<SysUser> selectUserListByDeptId(SysUser sysUser);

    /**
     * Paginate query allocated user role list by conditions
     *
     * @param user User info
     * @return User info collection
     */
    public List<SysUser> selectAllocatedList(SysUser user);

    /**
     * Get user list excluding users already in current project
     */
    public List<SysUser> selectNoProjectUserList(SysUser user);

    /**
     * Paginate query unallocated user role list by conditions
     *
     * @param user User info
     * @return User info collection
     */
    public List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * Query user by username
     *
     * @param userName Username
     * @return User object info
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * Query user by user ID
     *
     * @param userId User ID
     * @return User object info
     */
    public SysUser selectUserById(Long userId);

    /**
     * Query user by user ID and project ID
     *
     * @param userId    User ID
     * @param projectId Project ID
     * @return User object info
     */
    public SysUser selectUserByUserIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);

    /**
     * Insert user info
     *
     * @param user User info
     * @return Result
     */
    public int insertUser(SysUser user);

    /**
     * Update user info
     *
     * @param user User info
     * @return Result
     */
    public int updateUser(SysUser user);

    /**
     * Update user avatar
     *
     * @param userName Username
     * @param avatar   Avatar URL
     * @return Result
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * Reset user password
     *
     * @param userName Username
     * @param password Password
     * @return Result
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * Delete user by user ID
     *
     * @param userId User ID
     * @return Result
     */
    public int deleteUserById(Long userId);

    /**
     * Batch delete user info
     *
     * @param userIds User IDs to delete
     * @return Result
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * Check if username is unique
     *
     * @param userName Username
     * @return Result
     */
    public SysUser checkUserNameUnique(String userName);

    /**
     * Check if phone number is unique
     *
     * @param phonenumber Phone number
     * @return Result
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * Check if email is unique
     *
     * @param email User email
     * @return Result
     */
    public SysUser checkEmailUnique(String email);

    /**
     * Get user ID list by dept ID set and role ID set
     *
     * @param deptIdSet
     * @param roleIdSet
     * @return
     */
    Set<Long> queryUserIdListByRoleIdListAndDeptIdList(
            @Param("deptIdSet") List<String> deptIdSet,
            @Param("roleIdSet") List<String> roleIdSet);

    /**
     * Query user by login name or phone number
     *
     * @param str Login name or phone number
     */
    List<SysUser> selectUserListByNameOrPhone(String str);
}
