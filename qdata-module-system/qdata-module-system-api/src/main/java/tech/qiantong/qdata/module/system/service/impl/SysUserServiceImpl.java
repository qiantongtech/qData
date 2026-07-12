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

package tech.qiantong.qdata.module.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.qiantong.qdata.common.annotation.DataScope;
import tech.qiantong.qdata.common.constant.UserConstants;
import tech.qiantong.qdata.common.core.domain.entity.SysRole;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.SecurityUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.spring.SpringUtils;
import tech.qiantong.qdata.module.system.domain.SysPost;
import tech.qiantong.qdata.module.system.domain.SysUserPost;
import tech.qiantong.qdata.module.system.domain.SysUserRole;
import tech.qiantong.qdata.module.system.mapper.*;
import tech.qiantong.qdata.module.system.service.ISysConfigService;
import tech.qiantong.qdata.module.system.service.ISysDeptService;
import tech.qiantong.qdata.module.system.service.ISysUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User business layer handling
 *
 * @author qdata
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysDeptService deptService;

    /**
     * Paginated query of user list based on conditions
     *
     * @param user user information
     * @return collection of user information
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * Paginated query of allocated user role list based on conditions
     *
     * @param user user information
     * @return collection of user information
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user)
    {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * Paginated query of unallocated user role list based on conditions
     *
     * @param user user information
     * @return collection of user information
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user)
    {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * Query user by username
     *
     * @param userName username
     * @return user object information
     */
    @Override
    public SysUser selectUserByUserName(String userName)
    {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * Query user by user ID
     *
     * @param userId user ID
     * @return user object information
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    @Override
    public SysUser getByUserIdAndProjectId(Long userId, Long projectId) {
        return userMapper.selectUserByUserIdAndProjectId(userId,projectId);
    }

    /**
     * Query user's role group
     *
     * @param userName username
     * @return result
     */
    @Override
    public String selectUserRoleGroup(String userName)
    {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list))
        {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * Query user's post group
     *
     * @param userName username
     * @return result
     */
    @Override
    public String selectUserPostGroup(String userName)
    {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list))
        {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * Check whether the user name is unique
     *
     * @param user user information
     * @return result
     */
    @Override
    public boolean checkUserNameUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Check whether the phone number is unique
     *
     * @param user user information
     * @return
     */
    @Override
    public boolean checkPhoneUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Check whether the email is unique
     *
     * @param user user information
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Check if the user is allowed to be operated on
     *
     * @param user user information
     */
    @Override
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new ServiceException("Operation on super admin user is not allowed");
        }
    }

    /**
     * Check if the user has data permission
     *
     * @param userId user ID
     */
    @Override
    public void checkUserDataScope(Long userId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users))
            {
                throw new ServiceException("No permission to access user data!");
            }
        }
    }

    /**
     * Insert and save user information
     *
     * @param user user information
     * @return result
     */
    @Override
    @Transactional
    public int insertUser(SysUser user)
    {
        // Insert user information
        int rows = userMapper.insertUser(user);
        // Insert user-post association
        insertUserPost(user);
        // Insert user-role association
        insertUserRole(user);
        return rows;
    }

    /**
     * Register user information
     *
     * @param user user information
     * @return result
     */
    @Override
    public boolean registerUser(SysUser user)
    {
        return userMapper.insertUser(user) > 0;
    }

    /**
     * Update and save user information
     *
     * @param user user information
     * @return result
     */
    @Override
    @Transactional
    public int updateUser(SysUser user)
    {
        Long userId = user.getUserId();
        // Delete user-role association
        userRoleMapper.deleteUserRoleByUserId(userId);
        // Insert user-role management
        insertUserRole(user);
        // Delete user-post association
        userPostMapper.deleteUserPostByUserId(userId);
        // Insert user-post management
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * Authorize user roles
     *
     * @param userId user ID
     * @param roleIds role IDs
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds)
    {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * Update user status
     *
     * @param user user information
     * @return result
     */
    @Override
    public int updateUserStatus(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * Update user basic information
     *
     * @param user user information
     * @return result
     */
    @Override
    public int updateUserProfile(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * Update user avatar
     *
     * @param userName username
     * @param avatar avatar URL
     * @return result
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar)
    {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * Reset user password
     *
     * @param user user information
     * @return result
     */
    @Override
    public int resetPwd(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * Reset user password
     *
     * @param userName username
     * @param password password
     * @return result
     */
    @Override
    public int resetUserPwd(String userName, String password)
    {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * Insert user role information
     *
     * @param user user object
     */
    public void insertUserRole(SysUser user)
    {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * Insert user post information
     *
     * @param user user object
     */
    public void insertUserPost(SysUser user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotEmpty(posts))
        {
            // Insert user-post management
            List<SysUserPost> list = new ArrayList<SysUserPost>(posts.length);
            for (Long postId : posts)
            {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            userPostMapper.batchUserPost(list);
        }
    }

    /**
     * Insert user role information
     *
     * @param userId user ID
     * @param roleIds role IDs
     */
    public void insertUserRole(Long userId, Long[] roleIds)
    {
        if (StringUtils.isNotEmpty(roleIds))
        {
            // Insert user-role management
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * Delete user by user ID
     *
     * @param userId user ID
     * @return result
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId)
    {
        // Delete user-role association
        userRoleMapper.deleteUserRoleByUserId(userId);
        // Delete user-post table
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * Batch delete user information
     *
     * @param userIds user IDs to delete
     * @return result
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds)
    {
        for (Long userId : userIds)
        {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // Delete user-role association
        userRoleMapper.deleteUserRole(userIds);
        // Delete user-post association
        userPostMapper.deleteUserPost(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * Import user data
     *
     * @param userList user data list
     * @param isUpdateSupport whether to support update, update if exists
     * @param operName operator name
     * @return result
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new ServiceException("Import user data cannot be empty!");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList)
        {
            try
            {
                // Verify if this user exists
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u))
                {
                    deptService.checkDeptDataScope(user.getDeptId());
                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    userMapper.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "Account " + user.getUserName() + " imported successfully");
                }
                else if (isUpdateSupport)
                {
                    checkUserAllowed(u);
                    checkUserDataScope(u.getUserId());
                    deptService.checkDeptDataScope(user.getDeptId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    userMapper.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "Account " + user.getUserName() + " updated successfully");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "Account " + user.getUserName() + " already exists");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "Account " + user.getUserName() + " import failed:";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "Sorry, import failed! Total " + failureNum + " records with incorrect format, errors as follows:");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "Congratulations, all data imported successfully! Total " + successNum + " records, data as follows:");
        }
        return successMsg.toString();
    }

    @Override
    public SysUser findUserByNameOrPhone(String str) {
        List<SysUser> userList = userMapper.selectUserListByNameOrPhone(str);
        return userList != null && userList.size() > 0 ? userList.get(0) : null;
    }

    @Override
    public List<SysUser> selectDeptUserTreeList() {
//        SysDept sysDept = new SysDept();
//        // Department tree structure
//        List<TreeSelect> treeSelects = deptService.selectDeptTreeList(sysDept);
//        // Insert users to form tree structure
//        for (TreeSelect treeSelect : treeSelects) {
//            if (treeSelect.getChildren() == null && treeSelect.getId() == 0){
//                // No children - this level may be a department, query users under the department
//                List<SysUser> userList = userMapper.selectUserListByDeptId(new SysUser(treeSelect.getId()));
//                if (userList != null && !userList.isEmpty()){
//                    List<TreeSelect> children = new ArrayList<>();
//                    for (SysUser user : userList) {
//                        TreeSelect userTree = new TreeSelect();
//                        userTree.setId(user.getUserId());
//                        userTree.setLabel(user.getUserName());
//                        children.add(userTree);
//                    }
//                    treeSelect.setChildren(children);
//                }
//            }
//
//        }
        SysUser sysUser = new SysUser();
        sysUser.setStatus("0");
        List<SysUser> sysUsers = userMapper.selectUserList(sysUser);

        return sysUsers;
    }
}
