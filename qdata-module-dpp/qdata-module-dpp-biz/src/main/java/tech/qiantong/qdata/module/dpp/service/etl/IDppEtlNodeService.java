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
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlNodeRespDTO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Integration Node Service Interface
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlNodeService extends IService<DppEtlNodeDO> {

    /**
     * Get data integration node pagination list
     *
     * @param pageReqVO Pagination request
     * @return Data integration node pagination list
     */
    PageResult<DppEtlNodeDO> getDppEtlNodePage(DppEtlNodePageReqVO pageReqVO);

    List<DppEtlNodeRespVO> getDppEtlNodeRespList(DppEtlNodePageReqVO pageReqVO);

    /**
     * Get node list by task ID
     *
     * @param taskId
     * @return
     */
    List<DppEtlNodeRespVO> listNodeByTaskId(Long taskId);

    DppEtlNodeRespVO getDppEtlNodeRespVOByReqVO(DppEtlNodePageReqVO reqVOPre);

    /**
     * Create data integration node
     *
     * @param createReqVO Data integration node info
     * @return Data integration node ID
     */
    Long createDppEtlNode(DppEtlNodeSaveReqVO createReqVO);

    List<DppEtlNodeDO> createDppEtlNodeBatch(List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList);

    /**
     * Update data integration node
     *
     * @param updateReqVO Data integration node info
     */
    int updateDppEtlNode(DppEtlNodeSaveReqVO updateReqVO);

    /**
     * Delete data integration node
     *
     * @param idList Data integration node ID list
     */
    int removeDppEtlNode(Collection<Long> idList);

    /**
     * Get data integration node detail
     *
     * @param id Data integration node ID
     * @return Data integration node
     */
    DppEtlNodeDO getDppEtlNodeById(Long id);

    /**
     * Get all data integration node list
     *
     * @return Data integration node list
     */
    List<DppEtlNodeDO> getDppEtlNodeList();

    /**
     * Get all data integration node Map
     *
     * @return Data integration node Map
     */
    Map<Long, DppEtlNodeDO> getDppEtlNodeMap();


    /**
     * Import data integration node data
     *
     * @param importExcelList Data integration node data list
     * @param isUpdateSupport Whether to support update. If already exists, update the data
     * @param operName        Operator
     * @return Result
     */
    String importDppEtlNode(List<DppEtlNodeRespVO> importExcelList, boolean isUpdateSupport, String operName);

    void removeOldDppEtlNode(List<String> code);

    /**
     * Get node ID by node code
     *
     * @param nodeCode
     * @return
     */
    Long getNodeIdByNodeCode(String nodeCode);

    /**
     * Get node info by node code
     *
     * @param nodeCode
     * @return
     */
    DppEtlNodeRespDTO getNodeByNodeCode(String nodeCode);
}
