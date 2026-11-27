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

package tech.qiantong.qdata.module.mc.service.metadata;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Metadata field information Service interface
 *
 * @author qdata
 * @date 2026-02-11
 */
public interface IMcColumnService extends IService<McColumnDO> {

    /**
     * Get a paginated list of metadata field information
     *
     * @param pageReqVO paging request
     * @return metadata field information paginated list
     */
    PageResult<McColumnDO> getMcColumnPage(McColumnPageReqVO pageReqVO);

    /**
     * Create metadata field information
     *
     * @param createReqVO metadata field information
     * @return metadata field information number
     */
    Long createMcColumn(McColumnSaveReqVO createReqVO);

    List<McColumnDO>  createMcColumnList(List<McColumnSaveReqVO> createReqVO);

    /**
     * Update metadata field information
     *
     * @param updateReqVO metadata field information information
     */
    int updateMcColumn(McColumnSaveReqVO updateReqVO);

    /**
     * Delete metadata field information
     *
     * @param idList metadata field information number
     */
    int removeMcColumn(Collection<Long> idList);
    int removeMcColumn(McColumnRespVO mcColumnRespVO);

    /**
     * Get metadata field information details
     *
     * @param id metadata field information number
     * @return metadata field information
     */
    McColumnRespVO getMcColumnById(Long id);

    /**
     * Get a list of all metadata field information
     *
     * @return metadata field information list
     */
    List<McColumnDO> getMcColumnList();
    List<McColumnRespVO> getMcColumnList(McColumnRespVO mcColumnRespVO);

    /**
     * Get all metadata field information Map
     *
     * @return Metadata field information Map
     */
    Map<Long, McColumnDO> getMcColumnMap();


    List<McColumnDO> getMdColumnList(McColumnPageReqVO mdColumn);

    Integer createMdColumn(List<McColumnSaveReqVO> mdColumn);

    Integer saveDraft(List<McColumnSaveReqVO> saveReqVO);

    Integer toggle(Long id, String status);

    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> list);
}
