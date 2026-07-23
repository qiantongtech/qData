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

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectUserRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Project-User Relationship Service Interface
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface IAttProjectUserRelService extends IService<AttProjectUserRelDO> {

    /**
     * Get Project-User Relationship paginated list
     *
     * @param pageReqVO Page request
     * @return Project-User Relationship paginated list
     */
    PageResult<AttProjectUserRelDO> getAttProjectUserRelPage(AttProjectUserRelPageReqVO pageReqVO);

    /**
     * Create Project-User Relationship
     *
     * @param createReqVO Project-User Relationship info
     * @return Project-User Relationship ID
     */
    Long createAttProjectUserRel(AttProjectUserRelSaveReqVO createReqVO);

    /**
     * Update Project-User Relationship
     *
     * @param updateReqVO Project-User Relationship info
     */
    int updateAttProjectUserRel(AttProjectUserRelSaveReqVO updateReqVO);

    /**
     * Update Project-User Relationship
     *
     * @param updateReqVO Project-User Relationship info
     */
    int updateUserListAndRoleList(AttProjectUserRelSaveReqVO updateReqVO);

    /**
     * Delete Project-User Relationship
     *
     * @param idList Project-User Relationship ID list
     */
    int removeAttProjectUserRel(Collection<Long> idList);

    /**
     * Get Project-User Relationship details
     *
     * @param id Project-User Relationship ID
     * @return Project-User Relationship
     */
    AttProjectUserRelDO getAttProjectUserRelById(Long id);

    /**
     * Get all Project-User Relationship list
     *
     * @return Project-User Relationship list
     */
    List<AttProjectUserRelDO> getAttProjectUserRelList();

    /**
     * Get all Project-User Relationship Map
     *
     * @return Project-User Relationship Map
     */
    Map<Long, AttProjectUserRelDO> getAttProjectUserRelMap();


    /**
     * Import Project-User Relationship data
     *
     * @param importExcelList Project-User Relationship data list
     * @param isUpdateSupport Whether to support update, if already exists, then update data
     * @param operName        Operator
     * @return Result
     */
    String importAttProjectUserRel(List<AttProjectUserRelRespVO> importExcelList, boolean isUpdateSupport, String operName);


    /**
     * Create Project-User Relationship with user and role collections from frontend
     *
     * @param attProject Project info
     * @return Project ID
     */
    Boolean createUserListAndRoleList(AttProjectUserRelSaveReqVO attProject);

    /**
     * Get Project-User Relationship details including role info
     *
     * @param id
     * @return
     */
    AttProjectUserRelRespVO getRoleUser(Long id);
}
