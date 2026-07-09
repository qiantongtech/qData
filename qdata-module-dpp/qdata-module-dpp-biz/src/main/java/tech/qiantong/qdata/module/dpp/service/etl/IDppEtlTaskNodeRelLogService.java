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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Integration Task Node Relation Log Service Interface
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlTaskNodeRelLogService extends IService<DppEtlTaskNodeRelLogDO> {

    /**
     * Get data integration task node relation log pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data integration task node relation log pagination list
     */
    PageResult<DppEtlTaskNodeRelLogDO> getDppEtlTaskNodeRelLogPage(DppEtlTaskNodeRelLogPageReqVO pageReqVO);
    List<DppEtlTaskNodeRelLogRespVO> getDppEtlTaskNodeRelLogRespVOList(DppEtlTaskNodeRelLogPageReqVO pageReqVO);
    DppEtlTaskNodeRelLogRespVO getDppEtlTaskNodeRelLogById(DppEtlTaskNodeRelLogPageReqVO pageReqVO);

    /**
     * Create data integration task node relation log
     *
     * @param createReqVO Data integration task node relation log info
     * @return Data integration task node relation log ID
     */
    Long createDppEtlTaskNodeRelLog(DppEtlTaskNodeRelLogSaveReqVO createReqVO);

    void createDppEtlTaskNodeRelLogBatch(List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS);

    /**
     * Update data integration task node relation log
     *
     * @param updateReqVO Data integration task node relation log info
     */
    int updateDppEtlTaskNodeRelLog(DppEtlTaskNodeRelLogSaveReqVO updateReqVO);

    /**
     * Delete data integration task node relation log
     *
     * @param idList Data integration task node relation log ID list
     */
    int removeDppEtlTaskNodeRelLog(Collection<Long> idList);

    /**
     * Get data integration task node relation log detail
     *
     * @param id Data integration task node relation log ID
     * @return Data integration task node relation log
     */
    DppEtlTaskNodeRelLogDO getDppEtlTaskNodeRelLogById(Long id);

    /**
     * Get all data integration task node relation log list
     *
     * @return Data integration task node relation log list
     */
    List<DppEtlTaskNodeRelLogDO> getDppEtlTaskNodeRelLogList();

    /**
     * Get all data integration task node relation log Map
     *
     * @return Data integration task node relation log Map
     */
    Map<Long, DppEtlTaskNodeRelLogDO> getDppEtlTaskNodeRelLogMap();


    /**
     * Import data integration task node relation log data
     *
     * @param importExcelList Data integration task node relation log data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importDppEtlTaskNodeRelLog(List<DppEtlTaskNodeRelLogRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
