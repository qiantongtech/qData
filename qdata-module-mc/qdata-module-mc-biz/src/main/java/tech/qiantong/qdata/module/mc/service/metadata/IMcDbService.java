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
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbSaveReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McMetaSearchRespDTO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Database Service interface
 *
 * @author qdata
 * @date 2026-02-11
 */
public interface IMcDbService extends IService<McDbDO> {

    /**
     * Get the database paging list
     *
     * @param pageReqVO paging request
     * @return database paging list
     */
    PageResult<McDbDO> getMcDbPage(McDbPageReqVO pageReqVO);

    /**
     * Create database
     *
     * @param createReqVO database information
     * @return database number
     */
    Long createMcDb(McDbSaveReqVO createReqVO);

    /**
     * Update database
     *
     * @param updateReqVO database information
     */
    int updateMcDb(McDbSaveReqVO updateReqVO);

    /**
     * Delete database
     *
     * @param idList database number
     */
    int removeMcDb(Collection<Long> idList);

    /**
     * Get database details
     *
     * @param id database number
     * @return database
     */
    McDbRespVO getMcDbById(Long id);

    /**
     * Get a list of all databases
     *
     * @return database list
     */
    List<McDbDO> getMcDbList(McDbPageReqVO mcDb);

    /**
     * Get all database maps
     *
     * @return database map
     */
    Map<Long, McDbDO> getMcDbMap();


    Integer toggle(Long id, String status);

    Integer editPortalVisible(Long id, String portalVisible);
    List<McDbRespVO> getMcDbByTaskId(Long taskId);

    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> list);

    PageResult<McMetaSearchRespDTO> selectMetaSearchPage(McMetaSearchRespDTO mdMetaSearchRespDTO);
}
