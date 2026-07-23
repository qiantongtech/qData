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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Integration Task Node Relation Service Interface
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlTaskNodeRelService extends IService<DppEtlTaskNodeRelDO> {

    /**
     * Get data integration task node relation pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data integration task node relation pagination list
     */
    PageResult<DppEtlTaskNodeRelDO> getDppEtlTaskNodeRelPage(DppEtlTaskNodeRelPageReqVO pageReqVO);
    List<DppEtlTaskNodeRelRespVO> getDppEtlTaskNodeRelRespVOList(DppEtlTaskNodeRelPageReqVO pageReqVO);

    /**
     * Create data integration task node relation
     *
     * @param createReqVO Data integration task node relation info
     * @return Data integration task node relation ID
     */
    Long createDppEtlTaskNodeRel(DppEtlTaskNodeRelSaveReqVO createReqVO);

    void createDppEtlTaskNodeRelBatch(List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS);

    /**
     * Update data integration task node relation
     *
     * @param updateReqVO Data integration task node relation info
     */
    int updateDppEtlTaskNodeRel(DppEtlTaskNodeRelSaveReqVO updateReqVO);

    /**
     * Delete data integration task node relation
     *
     * @param idList Data integration task node relation ID list
     */
    int removeDppEtlTaskNodeRel(Collection<Long> idList);

    /**
     * Get data integration task node relation detail
     *
     * @param id Data integration task node relation ID
     * @return Data integration task node relation
     */
    DppEtlTaskNodeRelDO getDppEtlTaskNodeRelById(Long id);

    /**
     * Get all data integration task node relation list
     *
     * @return Data integration task node relation list
     */
    List<DppEtlTaskNodeRelDO> getDppEtlTaskNodeRelList();

    /**
     * Get all data integration task node relation Map
     *
     * @return Data integration task node relation Map
     */
    Map<Long, DppEtlTaskNodeRelDO> getDppEtlTaskNodeRelMap();


    /**
     * Import data integration task node relation data
     *
     * @param importExcelList Data integration task node relation data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importDppEtlTaskNodeRel(List<DppEtlTaskNodeRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<DppEtlTaskNodeRelRespVO> removeOldDppEtlTaskNodeRel(String code);
}
