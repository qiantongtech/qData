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

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogStatisticsVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.CheckErrorDataReqDTO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEvaluateLogDO;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Evaluation Rule Result Service Interface
 *
 * @author qdata
 * @date 2025-07-21
 */
public interface IDppEvaluateLogService extends IService<DppEvaluateLogDO> {

    /**
     * Get evaluation rule result pagination list
     *
     * @param pageReqVO Pagination request
     * @return Evaluation rule result pagination list
     */
    PageResult<DppEvaluateLogDO> getDppEvaluateLogPage(DppEvaluateLogPageReqVO pageReqVO);

    /**
     * Create evaluation rule result
     *
     * @param createReqVO Evaluation rule result info
     * @return Evaluation rule result ID
     */
    Long createDppEvaluateLog(DppEvaluateLogSaveReqVO createReqVO);

    /**
     * Update evaluation rule result
     *
     * @param updateReqVO Evaluation rule result info
     */
    int updateDppEvaluateLog(DppEvaluateLogSaveReqVO updateReqVO);

    /**
     * Delete evaluation rule result
     *
     * @param idList Evaluation rule result ID list
     */
    int removeDppEvaluateLog(Collection<Long> idList);

    /**
     * Get evaluation rule result detail
     *
     * @param id Evaluation rule result ID
     * @return Evaluation rule result
     */
    DppEvaluateLogDO getDppEvaluateLogById(Long id);

    /**
     * Get all evaluation rule result list
     *
     * @return Evaluation rule result list
     */
    List<DppEvaluateLogDO> getDppEvaluateLogList();

    /**
     * Get all evaluation rule result Map
     *
     * @return Evaluation rule result Map
     */
    Map<Long, DppEvaluateLogDO> getDppEvaluateLogMap();

    Map<String, Object> sumTotalAndProblemTotalByTaskLogId(String taskLogId);


    /**
     * Import evaluation rule result data
     *
     * @param importExcelList Evaluation rule result data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importDppEvaluateLog(List<DppEvaluateLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<DppEvaluateLogStatisticsVO> statisticsEvaluateOne(Long id);

    JSONObject statisticsEvaluateTow(Long id , Date deDate , Date oldDate , int type);

    List<DppEvaluateLogRespVO> statisticsEvaluateTable(Long id);

    JSONObject pageErrorData(CheckErrorDataReqDTO checkErrorDataReqDTO);

    boolean updateErrorData(CheckErrorDataReqDTO checkErrorDataReqDTO);
}
