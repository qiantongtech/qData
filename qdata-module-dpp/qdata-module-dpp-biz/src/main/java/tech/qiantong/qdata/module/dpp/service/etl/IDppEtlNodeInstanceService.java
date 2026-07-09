/*
 * Copyright (c) 2025-present Jiangsu Qiantong Technology Co., Ltd.
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

package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskInstance;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Integration Node Instance Service Interface
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlNodeInstanceService extends IService<DppEtlNodeInstanceDO> {

    /**
     * Get data integration node instance pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data integration node instance pagination list
     */
    PageResult<DppEtlNodeInstanceDO> getDppEtlNodeInstancePage(DppEtlNodeInstancePageReqVO pageReqVO);

    /**
     * Create data integration node instance
     *
     * @param createReqVO Data integration node instance info
     * @return Data integration node instance ID
     */
    Long createDppEtlNodeInstance(DppEtlNodeInstanceSaveReqVO createReqVO);

    /**
     * Update data integration node instance
     *
     * @param updateReqVO Data integration node instance info
     */
    int updateDppEtlNodeInstance(DppEtlNodeInstanceSaveReqVO updateReqVO);

    /**
     * Delete data integration node instance
     *
     * @param idList Data integration node instance ID list
     */
    int removeDppEtlNodeInstance(Collection<Long> idList);

    /**
     * Get data integration node instance detail
     *
     * @param id Data integration node instance ID
     * @return Data integration node instance
     */
    DppEtlNodeInstanceDO getDppEtlNodeInstanceById(Long id);

    /**
     * Get all data integration node instance list
     *
     * @return Data integration node instance list
     */
    List<DppEtlNodeInstanceDO> getDppEtlNodeInstanceList();

    /**
     * Get all data integration node instance Map
     *
     * @return Data integration node instance Map
     */
    Map<Long, DppEtlNodeInstanceDO> getDppEtlNodeInstanceMap();


    /**
     * Import data integration node instance data
     *
     * @param importExcelList Data integration node instance data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName        Operator
     * @return Result
     */
    String importDppEtlNodeInstance(List<DppEtlNodeInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Create task node instance
     *
     * @param taskInstance
     * @return
     */
    Boolean createNodeInstance(TaskInstance taskInstance);

    /**
     * Update task node instance
     *
     * @param taskInstance
     * @return
     */
    Boolean updateNodeInstance(TaskInstance taskInstance);

    /**
     * Get data by dsId
     *
     * @param dsId
     * @return
     */
    DppEtlNodeInstanceDO getByDsId(Long dsId);

    /**
     * Process node instance log
     *
     * @param taskInstanceId
     * @param processInstanceId
     * @param logStr
     */
    void taskInstanceLogInsert(String taskInstanceId, String processInstanceId, String logStr);

    String getLogByNodeInstanceId(Long nodeInstanceId);
}
