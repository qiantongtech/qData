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

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import tech.qiantong.qdata.api.ds.api.project.DsProjectCreateReqDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectDeleteRespDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectRespDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectUpdateReqDTO;
import tech.qiantong.qdata.api.ds.api.service.project.IDsProjectService;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.core.domain.entity.SysRole;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.domain.model.LoginUser;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.PageUtils;
import tech.qiantong.qdata.common.utils.SecurityUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.project.IAttProjectApi;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectReqDTO;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectRespDTO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectSaveReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttSysUserReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectDO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectUserRelDO;
import tech.qiantong.qdata.module.att.dal.mapper.project.AttProjectMapper;
import tech.qiantong.qdata.module.att.dal.mapper.project.AttProjectUserRelMapper;
import tech.qiantong.qdata.module.att.service.project.IAttProjectService;
import tech.qiantong.qdata.module.system.domain.SysRoleMenu;
import tech.qiantong.qdata.module.system.domain.SysUserRole;
import tech.qiantong.qdata.module.system.mapper.SysRoleMapper;
import tech.qiantong.qdata.module.system.mapper.SysRoleMenuMapper;
import tech.qiantong.qdata.module.system.mapper.SysUserMapper;
import tech.qiantong.qdata.module.system.mapper.SysUserRoleMapper;
import tech.qiantong.qdata.security.context.PermissionContextHolder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project Service business layer processing
 *
 * @author shu
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IAttProjectServiceImpl extends ServiceImpl<AttProjectMapper, AttProjectDO> implements IAttProjectService, IAttProjectApi {
    @Resource
    private AttProjectMapper attProjectMapper;
    @Resource
    private AttProjectUserRelMapper attProjectUserRelMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Resource
    private IDsProjectService dsProjectService;

    @Override
    public PageResult<AttProjectDO> getAttProjectPage(AttProjectPageReqVO pageReqVO) {
        Page<AttProjectDO> attProjectDOPage = attProjectMapper
                .selectAttProjectListByPage(new Page(pageReqVO.getPageNum(), pageReqVO.getPageSize()), pageReqVO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(attProjectDOPage.getTotal());
        pageResult.setRows(attProjectDOPage.getRecords());
        return pageResult;
    }

    @Override
    public PageResult<AttProjectRespDTO> getAttProjectPage(AttProjectReqDTO pageReqVO) {
        AttProjectPageReqVO attProjectPageReqVO = BeanUtils.toBean(pageReqVO, AttProjectPageReqVO.class);
        PageResult<AttProjectDO> attProjectPage = this.getAttProjectPage(attProjectPageReqVO);
        return BeanUtils.toBean(attProjectPage, AttProjectRespDTO.class);
    }

    @Override
    public Long createAttProject(AttProjectSaveReqVO createReqVO) {
        DsProjectCreateReqDTO dsProjectCreateReqDTO = new DsProjectCreateReqDTO();
        dsProjectCreateReqDTO.setProjectName(createReqVO.getName());
        dsProjectCreateReqDTO.setDescription(createReqVO.getDescription());
        DsProjectRespDTO dsProjectRespDTO = dsProjectService.saveProject(dsProjectCreateReqDTO);
        if (dsProjectRespDTO.getCode() != 0) {
            return -1L;
        }
        AttProjectDO dictType = BeanUtils.toBean(createReqVO, AttProjectDO.class);
        dictType.setCode(dsProjectRespDTO.getData().getCode().toString());
        try {
            // Create project management data
            attProjectMapper.insert(dictType);
            if (dictType.getManagerId() != null) {
                // Create project-user relationship data
                AttProjectUserRelDO attProjectUserRelDO = new AttProjectUserRelDO();
                attProjectUserRelDO.setProjectId(dictType.getId());
                attProjectUserRelDO.setUserId(dictType.getManagerId());
                attProjectUserRelMapper.insert(attProjectUserRelDO);
                // Query built-in role table
                SysRole sysRole = new SysRole();
                sysRole.setProjectId(-1L);
                List<SysRole> roleList = sysRoleMapper.selectRoleList(sysRole);
                if (!roleList.isEmpty()) {
                    List<SysUserRole> userRoleList = new ArrayList<>();
                    List<SysRole> sysRoleList = new ArrayList<>();
                    for (SysRole role : roleList) {
                        SysRole sRole = new SysRole();
                        sRole.setOldRoleId(role.getRoleId());
                        sRole.setProjectId(dictType.getId());
                        sRole.setRoleName(role.getRoleName());
                        sRole.setRoleKey(role.getRoleKey());
                        sRole.setRoleSort(role.getRoleSort());
                        sRole.setDataScope(role.getDataScope());
                        sRole.setMenuCheckStrictly(role.isMenuCheckStrictly());
                        sRole.setDeptCheckStrictly(role.isDeptCheckStrictly());
                        sRole.setStatus(role.getStatus());
                        sysRoleList.add(sRole);
                    }
                    sysRoleMapper.insertRoleList(sysRoleList);
                    List<SysRole> sysRoleGlyList = sysRoleList.stream().filter(sysRole1 -> "gly".equals(sysRole1.getRoleKey())).collect(Collectors.toList());
                    for (SysRole role : sysRoleGlyList) {
                        SysUserRole sysUserRole = new SysUserRole();
                        sysUserRole.setUserId(dictType.getManagerId());
                        sysUserRole.setRoleId(role.getRoleId());
                        userRoleList.add(sysUserRole);
                    }
                    sysUserRoleMapper.batchUserRole(userRoleList);
                    List<Long> roleIdList = roleList.stream().map(SysRole::getRoleId).collect(Collectors.toList());
                    List<SysRoleMenu> roleMenuList = sysRoleMenuMapper.getByRoleIdList(roleIdList);
                    Map<Long, List<SysRoleMenu>> roleIdListMap = roleMenuList.stream().collect(Collectors.groupingBy(SysRoleMenu::getRoleId));
                    List<SysRoleMenu> rMenusList = new ArrayList<>();
                    for (SysRole role : sysRoleList) {
                        if (roleIdListMap.get(role.getOldRoleId()) == null || roleIdListMap.get(role.getOldRoleId()).size() == 0){
                            continue;
                        }
                        for (SysRoleMenu sysRoleMenu : roleIdListMap.get(role.getOldRoleId())) {
                            SysRoleMenu roleMenu = new SysRoleMenu();
                            roleMenu.setRoleId(role.getRoleId());
                            roleMenu.setMenuId(sysRoleMenu.getMenuId());
                            roleMenu.setProjectId(dictType.getId());
                            rMenusList.add(roleMenu);
                        }
                    }
                    sysRoleMenuMapper.batchRoleMenuProjectId(rMenusList);
                }
            }
        }catch (Exception e){
            // If error occurs, delete data from ds
            dsProjectService.deleteProject(dsProjectRespDTO.getData().getCode());
            e.printStackTrace();
            // Manual transaction rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -2L;
        }

        return dictType.getId();
    }

    @Override
    public int updateAttProject(AttProjectSaveReqVO updateReqVO) {
        // Validate
        DsProjectUpdateReqDTO dsProjectUpdateReqDTO = new DsProjectUpdateReqDTO();
        dsProjectUpdateReqDTO.setProjectName(updateReqVO.getName());
        dsProjectUpdateReqDTO.setProjectCode(Long.valueOf(updateReqVO.getCode()));
        dsProjectUpdateReqDTO.setDescription(updateReqVO.getDescription());
        DsProjectRespDTO dsProjectRespDTO = dsProjectService.updateProject(dsProjectUpdateReqDTO);
        if (dsProjectRespDTO.getCode() != 0) {
            return -1;
        }
        // Update project
        AttProjectDO updateObj = BeanUtils.toBean(updateReqVO, AttProjectDO.class);
        int i = -1;
        try {
            i = attProjectMapper.updateById(updateObj);
        }catch (Exception e){
            // If error occurs, delete data from ds
            dsProjectService.deleteProject(dsProjectRespDTO.getData().getCode());
            e.printStackTrace();
            // Manual transaction rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return i;
    }

    @Override
    public int removeAttProject(Collection<Long> idList) {
        QueryWrapper<AttProjectUserRelDO> projectWrapper = new QueryWrapper<>();
        projectWrapper.in(!CollectionUtils.isEmpty(idList), "project_id", idList);
        List<AttProjectUserRelDO> attProjectUserRelDOList = attProjectUserRelMapper.selectList(projectWrapper);
        if (attProjectUserRelDOList.size() > 0) {
            return -1;
        }
        List<AttProjectDO> projectDOList = attProjectMapper.selectList(new QueryWrapper<AttProjectDO>().in(!CollectionUtils.isEmpty(idList), "id", idList));
        int i = attProjectMapper.deleteBatchIds(idList);
        for (AttProjectDO attProjectDO : projectDOList) {
            DsProjectDeleteRespDTO dsProjectDeleteRespDTO = dsProjectService.deleteProject(Long.valueOf(attProjectDO.getCode()));
            if (dsProjectDeleteRespDTO.getCode() != 0) {
                // Manual transaction rollback
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return -2;
            }
        }
        // Batch delete project
        return i;
    }

    @Override
    public AttProjectDO getAttProjectById(Long id) {
        AttProjectDO projectDO = attProjectMapper.selectById(id);

        return projectDO;
    }

    @Override
    public List<AttProjectDO> getAttProjectList() {
        return attProjectMapper.selectList();
    }

    @Override
    public Map<Long, AttProjectDO> getAttProjectMap() {
        List<AttProjectDO> attProjectList = attProjectMapper.selectList();
        return attProjectList.stream()
                .collect(Collectors.toMap(
                        AttProjectDO::getId,
                        attProjectDO -> attProjectDO,
                        // Keep existing values
                        (existing, replacement) -> existing));
    }

    /**
     * Import project data
     *
     * @param importExcelList project data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operator
     * @return Result
     */
    @Override
    public String importAttProject(List<AttProjectRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttProjectRespVO respVO : importExcelList) {
            try {
                AttProjectDO attProjectDO = BeanUtils.toBean(respVO, AttProjectDO.class);
                Long attProjectId = respVO.getId();
                if (isUpdateSupport) {
                    if (attProjectId != null) {
                        AttProjectDO existingAttProject = attProjectMapper.selectById(attProjectId);
                        if (existingAttProject != null) {
                            attProjectMapper.updateById(attProjectDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                    "Data update successful, ID {0} {1} record.", attProjectId, MessageUtils.messageWithFallback("att.entity.project", "Project")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", attProjectId, MessageUtils.messageWithFallback("att.entity.project", "Project")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<AttProjectDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attProjectId);
                    AttProjectDO existingAttProject = attProjectMapper.selectOne(queryWrapper);
                    if (existingAttProject == null) {
                        attProjectMapper.insert(attProjectDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", attProjectId, MessageUtils.messageWithFallback("att.entity.project", "Project")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", attProjectId, MessageUtils.messageWithFallback("att.entity.project", "Project")));
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
     * Check if current user has user-add and project-admin permissions
     *
     * @param userId User ID
     * @return
     */
    @Override
    public JSONObject addUserAndProjectIsOk(Long userId, Long id) {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<AttProjectDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ID", id).eq("MANAGER_ID", userId);
        AttProjectDO attProjectDO = attProjectMapper.selectOne(queryWrapper);
        jsonObject.set("isManagerId", attProjectDO != null);
        if (StringUtils.isEmpty("system:user:add")) {
            jsonObject.set("isUserDaa", false);
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            jsonObject.set("isUserDaa", false);
        }
        PermissionContextHolder.setContext("system:user:add");
        Boolean isOk = loginUser.getPermissions().contains(Constants.ALL_PERMISSION)
                || loginUser.getPermissions().contains(StringUtils.trim("system:user:add"));
        jsonObject.set("isUserDaa", isOk);
        return jsonObject;
    }

    /**
     * Query project list belonging to current user
     *
     * @param userId User ID
     * @return
     */
    @Override
    public List<AttProjectDO> getCurrentUserList(Long userId) {
        if (userId == 1) {
            return attProjectMapper.selectList();
        }
        QueryWrapper<AttProjectUserRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<AttProjectUserRelDO> userRelDOList = attProjectUserRelMapper.selectList(queryWrapper);
        List<Long> projectIds = userRelDOList.stream()
                .map(AttProjectUserRelDO::getProjectId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(projectIds)) {
            return new ArrayList<>();
        }
        QueryWrapper<AttProjectDO> projectWrapper = new QueryWrapper<>();
        projectWrapper.in(!CollectionUtils.isEmpty(projectIds), "id", projectIds).eq("valid_flag", "1");
        return attProjectMapper.selectList(projectWrapper);
    }

    /**
     * Get user list excluding users already in current project
     */
    @Override
    public List<SysUser> selectNoProjectUserList(AttSysUserReqVO user) {
        QueryWrapper<AttProjectUserRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PROJECT_ID", user.getProjectId());
        List<AttProjectUserRelDO> projectUserRelDOList = attProjectUserRelMapper.selectList(queryWrapper);
        List<Long> userIdList = projectUserRelDOList.stream()
                .map(AttProjectUserRelDO::getUserId)
                .collect(Collectors.toList());
        //AttProjectDO projectDO = attProjectMapper.selectById(user.getProjectId());
        //userIdList.add(projectDO.getManagerId());
        userIdList.add(1L);
        SysUser sysUser = new SysUser();
        sysUser.setUserIdList(userIdList);
        sysUser.setStatus("0");
        sysUser.setPhonenumber(user.getPhonenumber());
        sysUser.setUserName(user.getUserName());
        PageUtils.startPage();
        List<SysUser> sysUserList = sysUserMapper.selectNoProjectUserList(sysUser);
        return sysUserList;
    }

    @Override
    public Boolean editProjectStatus(Long id,Long status) {
        return this.update(Wrappers.lambdaUpdate(AttProjectDO.class)
                .eq(AttProjectDO::getId, id)
                .set(AttProjectDO::getValidFlag, status));
    }

    @Override
    public Long getProjectIdByProjectCode(String projectCode) {
        AttProjectDO attProjectDO = baseMapper.selectOne(Wrappers.lambdaQuery(AttProjectDO.class)
                .eq(AttProjectDO::getCode, projectCode));
        if (attProjectDO != null) {
            return attProjectDO.getId();
        }
        return null;
    }
}
