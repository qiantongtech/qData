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

package tech.qiantong.qdata.module.att.service.project;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectSaveReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttSysUserReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Project Service Interface
 *
 * @author shu
 * @date 2025-01-20
 */
public interface IAttProjectService extends IService<AttProjectDO> {

    /**
     * Get Project paginated list
     *
     * @param pageReqVO Page request
     * @return Project paginated list
     */
    PageResult<AttProjectDO> getAttProjectPage(AttProjectPageReqVO pageReqVO);

    /**
     * Create Project
     *
     * @param createReqVO Project info
     * @return Project ID
     */
    Long createAttProject(AttProjectSaveReqVO createReqVO);

    /**
     * Update Project
     *
     * @param updateReqVO Project info
     */
    int updateAttProject(AttProjectSaveReqVO updateReqVO);

    /**
     * Delete Project
     *
     * @param idList Project ID list
     */
    int removeAttProject(Collection<Long> idList);

    /**
     * Get Project details
     *
     * @param id Project ID
     * @return Project
     */
    AttProjectDO getAttProjectById(Long id);

    /**
     * Get all Project list
     *
     * @return Project list
     */
    List<AttProjectDO> getAttProjectList();

    /**
     * Get all Project Map
     *
     * @return Project Map
     */
    Map<Long, AttProjectDO> getAttProjectMap();


    /**
     * Import Project data
     *
     * @param importExcelList Project data list
     * @param isUpdateSupport Whether update is supported; if already exists, update the data
     * @param operName        Operator
     * @return Result
     */
    String importAttProject(List<AttProjectRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Check if the current user has user add and project admin permissions
     *
     * @param userId User ID
     * @return
     */
    JSONObject addUserAndProjectIsOk(Long userId, Long id);

    /**
     * Query the project list belonging to the current user
     *
     * @param userId User ID
     * @return
     */
    List<AttProjectDO> getCurrentUserList(Long userId);

    /**
     * Get user list excluding users already in the current project
     */
    List<SysUser> selectNoProjectUserList(AttSysUserReqVO user);

    Boolean editProjectStatus(Long id,Long status);
}
