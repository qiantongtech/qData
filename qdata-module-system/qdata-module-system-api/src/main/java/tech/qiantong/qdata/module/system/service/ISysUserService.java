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

import tech.qiantong.qdata.common.core.domain.entity.SysUser;

import java.util.List;

/**
 * User service layer
 *
 * @author qdata
 */
public interface ISysUserService
{
    /**
     * Query user list by conditions with pagination
     *
     * @param user user information
     * @return user information collection
     */
    public List<SysUser> selectUserList(SysUser user);

    /**
     * Query allocated user role list by conditions with pagination
     *
     * @param user user information
     * @return user information collection
     */
    public List<SysUser> selectAllocatedList(SysUser user);

    /**
     * Query unallocated user role list by conditions with pagination
     *
     * @param user user information
     * @return user information collection
     */
    public List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * Query user by username
     *
     * @param userName username
     * @return user object information
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * Query user by user ID
     *
     * @param userId user ID
     * @return user object information
     */
    public SysUser selectUserById(Long userId);

    /**
     * Query user by user ID and project ID
     *
     * @param userId user ID
     * @param projectId project ID
     * @return user object information
     */
    public SysUser getByUserIdAndProjectId(Long userId,Long projectId);

    /**
     * Query user role group by username
     *
     * @param userName username
     * @return result
     */
    public String selectUserRoleGroup(String userName);

    /**
     * Query user post group by username
     *
     * @param userName username
     * @return result
     */
    public String selectUserPostGroup(String userName);

    /**
     * Check if username is unique
     *
     * @param user user information
     * @return result
     */
    public boolean checkUserNameUnique(SysUser user);

    /**
     * Check if phone number is unique
     *
     * @param user user information
     * @return result
     */
    public boolean checkPhoneUnique(SysUser user);

    /**
     * Check if email is unique
     *
     * @param user user information
     * @return result
     */
    public boolean checkEmailUnique(SysUser user);

    /**
     * Check if user is allowed to operate
     *
     * @param user user information
     */
    public void checkUserAllowed(SysUser user);

    /**
     * Check if user has data scope permission
     *
     * @param userId user ID
     */
    public void checkUserDataScope(Long userId);

    /**
     * Insert user information
     *
     * @param user user information
     * @return result
     */
    public int insertUser(SysUser user);

    /**
     * Register user information
     *
     * @param user user information
     * @return result
     */
    public boolean registerUser(SysUser user);

    /**
     * Update user information
     *
     * @param user user information
     * @return result
     */
    public int updateUser(SysUser user);

    /**
     * Authorize roles to user
     *
     * @param userId user ID
     * @param roleIds role IDs
     */
    public void insertUserAuth(Long userId, Long[] roleIds);

    /**
     * Update user status
     *
     * @param user user information
     * @return result
     */
    public int updateUserStatus(SysUser user);

    /**
     * Update user basic profile
     *
     * @param user user information
     * @return result
     */
    public int updateUserProfile(SysUser user);

    /**
     * Update user avatar
     *
     * @param userName username
     * @param avatar avatar URL
     * @return result
     */
    public boolean updateUserAvatar(String userName, String avatar);

    /**
     * Reset user password
     *
     * @param user user information
     * @return result
     */
    public int resetPwd(SysUser user);

    /**
     * Reset user password
     *
     * @param userName username
     * @param password password
     * @return result
     */
    public int resetUserPwd(String userName, String password);

    /**
     * Delete user by user ID
     *
     * @param userId user ID
     * @return result
     */
    public int deleteUserById(Long userId);

    /**
     * Batch delete user information
     *
     * @param userIds user IDs to delete
     * @return result
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * Import user data
     *
     * @param userList user data list
     * @param isUpdateSupport whether to update existing data if already present
     * @param operName operator name
     * @return result
     */
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);

    /**
     * Query user by login name or phone number
     * @param str login name or phone number
     * @return
     */
    SysUser findUserByNameOrPhone(String str);

    List<SysUser> selectDeptUserTreeList();
}
