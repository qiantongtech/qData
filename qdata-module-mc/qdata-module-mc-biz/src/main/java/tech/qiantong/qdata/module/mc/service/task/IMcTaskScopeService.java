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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskScopeDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Collection range Service interface
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface IMcTaskScopeService extends IService<McTaskScopeDO> {

    /**
     * Get a paging list of collection ranges
     *
     * @param pageReqVO paging request
     * @return collection range paging list
     */
    PageResult<McTaskScopeDO> getMcTaskScopePage(McTaskScopePageReqVO pageReqVO);

    /**
     * Create a collection range
     *
     * @param createReqVO collection range information
     * @return collection range number
     */
    Long createMcTaskScope(McTaskScopeSaveReqVO createReqVO);

    /**
     * Update collection range
     *
     * @param updateReqVO collection range information
     */
    int updateMcTaskScope(McTaskScopeSaveReqVO updateReqVO);

    /**
     * Delete collection range
     *
     * @param idList collection range number
     */
    int removeMcTaskScope(Collection<Long> idList);

    /**
     * Get collection range details
     *
     * @param id collection range number
     * @return collection range
     */
    McTaskScopeDO getMcTaskScopeById(Long id);

    /**
     * Get a list of all collection ranges
     *
     * @return Collection range list
     */
    List<McTaskScopeDO> getMcTaskScopeList();

    List<McTaskScopeDO> getMcTaskScopeListBytaskId(Long taskId);

    /**
     * Get the entire collection range Map
     *
     * @return Collection range Map
     */
    Map<Long, McTaskScopeDO> getMcTaskScopeMap();


    /**
     * Import collection range data
     *
     * @param importExcelList collection range data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    String importMcTaskScope(List<McTaskScopeRespVO> importExcelList, boolean isUpdateSupport, String operName);

    void removeMcTaskScopeBytaskId(Long taskId);
}
