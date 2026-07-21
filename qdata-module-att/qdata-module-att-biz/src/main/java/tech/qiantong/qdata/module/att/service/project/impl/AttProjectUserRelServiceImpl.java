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

package tech.qiantong.qdata.module.att.service.project.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.qiantong.qdata.common.core.domain.entity.SysRole;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectUserRelDO;
import tech.qiantong.qdata.module.att.dal.mapper.project.AttProjectUserRelMapper;
import tech.qiantong.qdata.module.att.service.project.IAttProjectUserRelService;
import tech.qiantong.qdata.module.system.domain.SysUserRole;
import tech.qiantong.qdata.module.system.mapper.SysRoleMapper;
import tech.qiantong.qdata.module.system.mapper.SysUserMapper;
import tech.qiantong.qdata.module.system.mapper.SysUserRoleMapper;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Project-User Relationship Service business layer processing
 *
 * @author qdata
 * @date 2025-02-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttProjectUserRelServiceImpl extends ServiceImpl<AttProjectUserRelMapper, AttProjectUserRelDO> implements IAttProjectUserRelService {
    @Resource
    private AttProjectUserRelMapper attProjectUserRelMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public PageResult<AttProjectUserRelDO> getAttProjectUserRelPage(AttProjectUserRelPageReqVO pageReqVO) {
        return attProjectUserRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttProjectUserRel(AttProjectUserRelSaveReqVO createReqVO) {
        AttProjectUserRelDO dictType = BeanUtils.toBean(createReqVO, AttProjectUserRelDO.class);
        attProjectUserRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttProjectUserRel(AttProjectUserRelSaveReqVO updateReqVO) {
        // Validate

        // Update project-user relationship
        AttProjectUserRelDO updateObj = BeanUtils.toBean(updateReqVO, AttProjectUserRelDO.class);
        return attProjectUserRelMapper.updateById(updateObj);
    }

    @Override
    public int updateUserListAndRoleList(AttProjectUserRelSaveReqVO updateReqVO) {
        List<Long> adminRoleIds = getProjectAdminRoleIds(updateReqVO.getProjectId());
        boolean currentIsAdmin = sysUserRoleMapper.getUserRoleByRoleId(updateReqVO.getUserId()).stream()
                .anyMatch(rel -> adminRoleIds.contains(rel.getRoleId()));
        boolean remainsAdmin = updateReqVO.getRoleIdList() != null && updateReqVO.getRoleIdList().stream()
                .anyMatch(adminRoleIds::contains);
        if (currentIsAdmin && !remainsAdmin && getProjectAdminUserIds(updateReqVO.getProjectId()).size() <= 1) {
            throw new ServiceException("att.error.project.admin.required",
                    "The project must retain at least one project administrator");
        }

        // Update project-user relationship
        SysRole sysRole = new SysRole();
        sysRole.setProjectId(updateReqVO.getProjectId());
        List<SysRole> sysRoleList = sysRoleMapper.selectRoleList(sysRole);

        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        for (SysRole role : sysRoleList) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(updateReqVO.getUserId());
            sysUserRole.setRoleId(role.getRoleId());
            sysUserRoleList.add(sysUserRole);
        }
        sysUserRoleMapper.deleteUserRoleList(sysUserRoleList);

        List<SysUserRole> userRoleList = new ArrayList<>();
        for (Long roleId : updateReqVO.getRoleIdList()) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(updateReqVO.getUserId());
            sysUserRole.setRoleId(roleId);
            userRoleList.add(sysUserRole);
        }
        if (!userRoleList.isEmpty()){
            sysUserRoleMapper.batchUserRole(userRoleList);
        }

        AttProjectUserRelDO updateObj = BeanUtils.toBean(updateReqVO, AttProjectUserRelDO.class);
        return attProjectUserRelMapper.updateById(updateObj);
    }

    @Override
    public int removeAttProjectUserRel(Collection<Long> idList) {
        QueryWrapper<AttProjectUserRelDO> projectWrapper = new QueryWrapper<>();
        projectWrapper.in(!CollectionUtils.isEmpty(idList), "id", idList);
        List<AttProjectUserRelDO> attProjectUserRelDOList = attProjectUserRelMapper.selectList(projectWrapper);
        if (attProjectUserRelDOList.isEmpty()) {
            return 0;
        }
        List<Long> userId = attProjectUserRelDOList.stream().map(AttProjectUserRelDO::getUserId).collect(Collectors.toList());
        Long projectId = attProjectUserRelDOList.get(0).getProjectId();
        Set<Long> adminUserIds = getProjectAdminUserIds(projectId);
        long deletingAdminCount = userId.stream().distinct().filter(adminUserIds::contains).count();
        if (!adminUserIds.isEmpty() && deletingAdminCount >= adminUserIds.size()) {
            throw new ServiceException("att.error.project.admin.required",
                    "The project must retain at least one project administrator");
        }
        List<SysUserRole> byUserIdList = sysUserRoleMapper.getByUserIdList(userId);
        SysRole sysRole = new SysRole();
        sysRole.setProjectId(projectId);
        List<SysRole> sysRoleList = sysRoleMapper.selectRoleList(sysRole);
        List<Long> roleIdList = sysRoleList.stream().map(SysRole::getRoleId).collect(Collectors.toList());
        List<SysUserRole> userRoleList = new ArrayList<>();
        for (SysUserRole sysUserRole : byUserIdList) {
            if (roleIdList.contains(sysUserRole.getRoleId())) {
                userRoleList.add(sysUserRole);
            }
        }
        if (!userRoleList.isEmpty()){
            sysUserRoleMapper.deleteUserRoleList(userRoleList);
        }
        // Batch delete project-user relationship
        return attProjectUserRelMapper.deleteBatchIds(idList);
    }

    private List<Long> getProjectAdminRoleIds(Long projectId) {
        SysRole query = new SysRole();
        query.setProjectId(projectId);
        return sysRoleMapper.selectRoleList(query).stream()
                .filter(role -> "gly".equals(role.getRoleKey()))
                .map(SysRole::getRoleId)
                .collect(Collectors.toList());
    }

    private Set<Long> getProjectAdminUserIds(Long projectId) {
        List<Long> memberUserIds = attProjectUserRelMapper.selectList(
                new QueryWrapper<AttProjectUserRelDO>().eq("PROJECT_ID", projectId)
        ).stream().map(AttProjectUserRelDO::getUserId).distinct().collect(Collectors.toList());
        List<Long> adminRoleIds = getProjectAdminRoleIds(projectId);
        if (memberUserIds.isEmpty() || adminRoleIds.isEmpty()) {
            return Collections.emptySet();
        }
        return sysUserRoleMapper.getByUserIdList(memberUserIds).stream()
                .filter(rel -> adminRoleIds.contains(rel.getRoleId()))
                .map(SysUserRole::getUserId)
                .collect(Collectors.toSet());
    }

    @Override
    public AttProjectUserRelDO getAttProjectUserRelById(Long id) {
        return attProjectUserRelMapper.selectById(id);
    }

    @Override
    public List<AttProjectUserRelDO> getAttProjectUserRelList() {
        return attProjectUserRelMapper.selectList();
    }

    @Override
    public Map<Long, AttProjectUserRelDO> getAttProjectUserRelMap() {
        List<AttProjectUserRelDO> attProjectUserRelList = attProjectUserRelMapper.selectList();
        return attProjectUserRelList.stream()
                .collect(Collectors.toMap(
                        AttProjectUserRelDO::getId,
                        attProjectUserRelDO -> attProjectUserRelDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import project-user relationship data
     *
     * @param importExcelList project-user relationship data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operator
     * @return Result
     */
    @Override
    public String importAttProjectUserRel(List<AttProjectUserRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttProjectUserRelRespVO respVO : importExcelList) {
            try {
                AttProjectUserRelDO attProjectUserRelDO = BeanUtils.toBean(respVO, AttProjectUserRelDO.class);
                Long attProjectUserRelId = respVO.getId();
                if (isUpdateSupport) {
                    if (attProjectUserRelId != null) {
                        AttProjectUserRelDO existingAttProjectUserRel = attProjectUserRelMapper.selectById(attProjectUserRelId);
                        if (existingAttProjectUserRel != null) {
                            attProjectUserRelMapper.updateById(attProjectUserRelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                    "Data update successful, ID {0} {1} record.", attProjectUserRelId, MessageUtils.messageWithFallback("att.entity.project.user.relation", "Project-user relation")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", attProjectUserRelId, MessageUtils.messageWithFallback("att.entity.project.user.relation", "Project-user relation")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<AttProjectUserRelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attProjectUserRelId);
                    AttProjectUserRelDO existingAttProjectUserRel = attProjectUserRelMapper.selectOne(queryWrapper);
                    if (existingAttProjectUserRel == null) {
                        attProjectUserRelMapper.insert(attProjectUserRelDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", attProjectUserRelId, MessageUtils.messageWithFallback("att.entity.project.user.relation", "Project-user relation")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", attProjectUserRelId, MessageUtils.messageWithFallback("att.entity.project.user.relation", "Project-user relation")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("att.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("att.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("att.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("att.import.result.success",
                    "Congratulations! All data imported successfully! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }


    /**
     * Create project-user and role list from frontend
     *
     * @param attProject Project info with user and role ID lists
     */
    @Override
    public Boolean createUserListAndRoleList(AttProjectUserRelSaveReqVO attProject) {
        List<AttProjectUserRelDO> attProjectUserRelDOList = new ArrayList<>();
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        for (Long userId : attProject.getUserIdList()) {
            SysUser user = sysUserMapper.selectUserById(userId);
            if (user == null || !"0".equals(user.getStatus())) {
                throw new ServiceException("att.error.project.user.disabled",
                        "The system user is disabled and cannot be added as a project member");
            }
            AttProjectUserRelDO attProjectUserRelDO = new AttProjectUserRelDO();
            attProjectUserRelDO.setUserId(userId);
            attProjectUserRelDO.setProjectId(attProject.getProjectId());
            attProjectUserRelDOList.add(attProjectUserRelDO);
            for (Long roleId : attProject.getRoleIdList()) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(userId);
                sysUserRoleList.add(sysUserRole);
            }
        }
        Boolean aBoolean = attProjectUserRelMapper.insertBatch(attProjectUserRelDOList);
        int i = sysUserRoleMapper.batchUserRole(sysUserRoleList);
        return aBoolean && i != -1;
    }

    /**
     * Get project-user relationship details including role info
     *
     * @param id
     * @return
     */
    @Override
    public AttProjectUserRelRespVO getRoleUser(Long id) {
        AttProjectUserRelDO attProjectUserRelDO = attProjectUserRelMapper.selectById(id);
        SysUser sysUser = sysUserMapper.selectUserById(attProjectUserRelDO.getUserId());
        attProjectUserRelDO.setUserName(sysUser.getUserName());
        attProjectUserRelDO.setNickName(sysUser.getNickName());
        attProjectUserRelDO.setPhoneNumber(sysUser.getPhonenumber());
        List<SysUserRole> userRoleList = sysUserRoleMapper.getUserRoleByRoleId(attProjectUserRelDO.getUserId());
        List<Long> roleIdList = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        SysRole sysRole = new SysRole();
        sysRole.setProjectId(attProjectUserRelDO.getProjectId());
        List<SysRole> sysRoleList = sysRoleMapper.selectRoleList(sysRole);
        Set<Long> roleSet = new HashSet<>();
        for (SysRole role : sysRoleList) {
            if (roleIdList.contains(role.getRoleId())) {
                roleSet.add(role.getRoleId());
            }
        }
        AttProjectUserRelRespVO attProjectUserRelRespVO = BeanUtils.toBean(attProjectUserRelDO, AttProjectUserRelRespVO.class);
        attProjectUserRelRespVO.setRoleIdList(roleSet.stream().collect(Collectors.toList()));
        return attProjectUserRelRespVO;
    }
}
