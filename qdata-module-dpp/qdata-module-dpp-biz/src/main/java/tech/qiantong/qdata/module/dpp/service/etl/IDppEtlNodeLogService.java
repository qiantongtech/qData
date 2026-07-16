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
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Integration Node Log Service Interface
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlNodeLogService extends IService<DppEtlNodeLogDO> {

    /**
     * Get data integration node log pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data integration node log pagination list
     */
    PageResult<DppEtlNodeLogDO> getDppEtlNodeLogPage(DppEtlNodeLogPageReqVO pageReqVO);

    DppEtlNodeLogDO getDppEtlNodeLogRespVOByReqVO(DppEtlNodeLogPageReqVO reqVO);

    /**
     * Create data integration node log
     *
     * @param createReqVO Data integration node log info
     * @return Data integration node log ID
     */
    Long createDppEtlNodeLog(DppEtlNodeLogSaveReqVO createReqVO);

    DppEtlNodeLogDO createDppEtlNodeLogNew(DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO);

    List<DppEtlNodeLogDO> createDppEtlNodeLogBatch(List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS);

    /**
     * Update data integration node log
     *
     * @param updateReqVO Data integration node log info
     */
    int updateDppEtlNodeLog(DppEtlNodeLogSaveReqVO updateReqVO);

    /**
     * Delete data integration node log
     *
     * @param idList Data integration node log ID list
     */
    int removeDppEtlNodeLog(Collection<Long> idList);

    /**
     * Get data integration node log detail
     *
     * @param id Data integration node log ID
     * @return Data integration node log
     */
    DppEtlNodeLogDO getDppEtlNodeLogById(Long id);

    /**
     * Get all data integration node log list
     *
     * @return Data integration node log list
     */
    List<DppEtlNodeLogDO> getDppEtlNodeLogList();

    /**
     * Get all data integration node log Map
     *
     * @return Data integration node log Map
     */
    Map<Long, DppEtlNodeLogDO> getDppEtlNodeLogMap();


    /**
     * Import data integration node log data
     *
     * @param importExcelList Data integration node log data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName        Operator
     * @return Result
     */
    String importDppEtlNodeLog(List<DppEtlNodeLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Get node info by code and version
     *
     * @param nodeCode
     * @param version
     * @return
     */
    DppEtlNodeLogDO getByNodeCodeAndVersion(String nodeCode, Integer version);

    /**
     * Get max version number by node code
     *
     * @param nodeCode
     * @return
     */
    Integer getMaxVersionByNodeCode(String nodeCode);


    /**
     * Get node info by task code and version
     *
     * @param taskCode
     * @param version
     * @return
     */
    List<DppEtlNodeLogDO> listByTaskCode(String taskCode, Integer version);
}
