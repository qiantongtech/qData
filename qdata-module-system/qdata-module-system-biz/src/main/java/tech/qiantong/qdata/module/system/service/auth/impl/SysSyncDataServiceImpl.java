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

package tech.qiantong.qdata.module.system.service.auth.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.entity.SysDept;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.utils.SecurityUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.system.dal.dataobject.auth.RelUserAuthProductDO;
import tech.qiantong.qdata.module.system.dal.mapper.auth.RelUserAuthProductMapper;
import tech.qiantong.qdata.module.system.domain.SysUserRole;
import tech.qiantong.qdata.module.system.enums.auth.AuthProductEnums;
import tech.qiantong.qdata.module.system.mapper.SysDeptMapper;
import tech.qiantong.qdata.module.system.mapper.SysUserMapper;
import tech.qiantong.qdata.module.system.mapper.SysUserRoleMapper;
import tech.qiantong.qdata.module.system.rsa.RSAUtil;
import tech.qiantong.qdata.module.system.service.auth.SysSyncDataService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handle data pushed from authentication platform
 */
@Service
public class SysSyncDataServiceImpl implements SysSyncDataService {
    private static final Logger log = LoggerFactory.getLogger(SysSyncDataService.class);
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private RelUserAuthProductMapper relUserAuthProductMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * Handle data pushed from authentication platform
     *
     * @param jsonObject
     * @return
     */
    public AjaxResult syncData(JSONObject jsonObject) {
        try {
            String mdType = jsonObject.getString("mdType");
            JSONArray masterData = jsonObject.getJSONArray("masterData");
            // Department data
            if ("deptdocs".equals(mdType)) {
                deptData(masterData);
            }
            // Personnel data
            else if ("psndocs".equals(mdType)) {
                userData(masterData);
            }
            log.info("=================Sync succeeded=================");
            AjaxResult ajaxResult = new AjaxResult();
            ajaxResult.put("success", true);
            return ajaxResult;
        } catch (Exception e) {
            log.info("Error processing data pushed from authentication platform: {}", e);
            return AjaxResult.error();
        }
    }

    /**
     * Handle user data pushed from authentication platform
     *
     * @param masterData
     */
    private void userData(JSONArray masterData) {
        ArrayList<SysUser> sysUsers = new ArrayList<>();

        // Query data where authentication platform ID is not empty
        SysUser user = new SysUser();
        List<SysUser> sysUserList = sysUserMapper.selectUserAllList(user);
        sysUserList = sysUserList.stream().filter(item -> StringUtils.isNotBlank(item.getAuthId())).collect(Collectors.toList());

        Map<String, SysUser> userMap = new HashMap<>();
        for (SysUser sysUser : sysUserList) {
            userMap.put(sysUser.getAuthId(), sysUser);
        }

        for (int i = 0; i < masterData.size(); i++) {
            JSONObject dataJSONObject = masterData.getJSONObject(i);
            SysUser sysUser = new SysUser();
            String idHubId = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("idHubId"));
            String userName = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("userName"));
            // If admin, skip
            if ("admin".equals(userName)) continue;
            String nickName = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("nickName"));
            String deptId = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("deptId"));
            String sex = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("sex"));
            String phone = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("phone"));
            String orderNum = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("orderNum"));
            String status = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("status"));
            String delFlag = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("delFlag"));
            String postId = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("postId"));
            String authPostId = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("authPostId"));
            String authPostName = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("authPostName"));

            sysUser.setUserName(userName);
            sysUser.setNickName(nickName);
            try {
                // TODO: Multi-department data import fails; framework deptId is set as Long
                sysUser.setDeptId(StringUtils.isBlank(deptId) ? null : Long.valueOf(deptId));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                continue;
            }
            sysUser.setSex(sex);
            sysUser.setPhonenumber(phone);
            sysUser.setStatus(status);
            sysUser.setDelFlag(delFlag);
//            sysUser.setPassword("qdata@123");
            sysUser.setPassword(SecurityUtils.encryptPassword("qdata@123"));
            sysUser.setRoleId(Long.valueOf(3));
            sysUser.setAuthId(idHubId);
            if (userMap.containsKey(sysUser.getAuthId())) {
                SysUser user1 = userMap.get(sysUser.getAuthId());
                sysUser.setUserId(user1.getUserId());
                sysUserMapper.updateUser(sysUser);
            } else {
                sysUserMapper.insertUser(sysUser);
            }
            sysUsers.add(sysUser);
        }

        // Re-query to get all user data
        sysUserList = sysUserMapper.selectUserAllList(user);

        ArrayList<SysUserRole> userRoles = new ArrayList<>();
        // userId is auto-increment, cannot get userId during insert, so loop again to store associations
        for (SysUser sysUser : sysUserList) {
            if (StringUtils.isNotBlank(sysUser.getAuthId()) && !userMap.containsKey(sysUser.getAuthId())) {
                RelUserAuthProductDO productDO = new RelUserAuthProductDO();
                productDO.setUserId(sysUser.getUserId());
                productDO.setAuthId(sysUser.getAuthId());
                productDO.setAuthProductType(AuthProductEnums.ANIVIA.code);
                relUserAuthProductMapper.insert(productDO);

                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(sysUser.getUserId());
                userRole.setRoleId(Long.valueOf(3));
                userRoles.add(userRole);
            }
        }
        sysUserRoleMapper.batchUserRole(userRoles);
    }

    /**
     * Handle department data pushed from authentication platform
     *
     * @param masterData
     */
    private void deptData(JSONArray masterData) {
        ArrayList<SysDept> sysIdHubDepts = new ArrayList<>();
        // Decrypt data
        for (int i = 0; i < masterData.size(); i++) {
            JSONObject dataJSONObject = masterData.getJSONObject(i);
            SysDept sysDept = new SysDept();
            String deptId = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("deptId"));
            String parentId = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("parentId"));
            String ancestors = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("ancestors"));
            String deptName = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("deptName"));
            String orderNum = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("orderNum"));
            String status = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("status"));
            String delFlag = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("delFlag"));
            String simpleDeptName = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("simpleDeptName"));

            sysDept.setDeptId(StringUtils.isBlank(deptId) ? null : Long.valueOf(deptId));
            sysDept.setParentId(StringUtils.isBlank(parentId) ? null : Long.valueOf(parentId));
            sysDept.setAncestors(ancestors);
            sysDept.setDeptName(deptName);
            sysDept.setOrderNum(StringUtils.isBlank(orderNum) ? null : Integer.valueOf(orderNum));
            sysDept.setStatus(status);
            sysDept.setDelFlag(delFlag);
            sysDept.setParentName(simpleDeptName);
            sysIdHubDepts.add(sysDept);
        }
        SysDept dept = new SysDept();
        List<SysDept> sysDeptList = sysDeptMapper.selectDeptListAll(dept);
        // Store sysDeptList DeptId and corresponding SysDept in HashMap
        Map<String, SysDept> deptMap = new HashMap<>();
        for (SysDept sys : sysDeptList) {
            deptMap.put(sys.getDeptId().toString(), sys);
        }

        // Iterate sysIdHubDepts, update or insert based on existence in deptMap
        for (SysDept dep : sysIdHubDepts) {
            if (deptMap.containsKey(dep.getDeptId().toString())) {
                sysDeptMapper.updateDept(dep);
            } else {
                sysDeptMapper.insertDept(dep);
            }
        }
    }
}
