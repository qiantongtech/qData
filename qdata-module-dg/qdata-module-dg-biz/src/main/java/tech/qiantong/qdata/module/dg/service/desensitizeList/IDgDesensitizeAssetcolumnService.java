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

package tech.qiantong.qdata.module.dg.service.desensitizeList;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeList.DgDesensitizeAssetcolumnDO;
/**
 * Desensitize List Relationship Service Interface
 *
 * @author qdata
 * @date 2026-04-12
 */
public interface IDgDesensitizeAssetcolumnService extends IService<DgDesensitizeAssetcolumnDO> {

    /**
     * Get Desensitize List Relationship Page
     *
     * @param pageReqVO Page request
     * @return Desensitize List Relationship Page
     */
    PageResult<DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnPage(DgDesensitizeAssetcolumnPageReqVO pageReqVO);

    /**
     * Create Desensitize List Relationship
     *
     * @param createReqVO Desensitize List Relationship info
     * @return Desensitize List Relationship ID
     */
    Long createDgDesensitizeAssetcolumn(DgDesensitizeAssetcolumnSaveReqVO createReqVO);

    /**
     * Update Desensitize List Relationship
     *
     * @param updateReqVO Desensitize List Relationship info
     */
    int updateDgDesensitizeAssetcolumn(DgDesensitizeAssetcolumnSaveReqVO updateReqVO);

    /**
     * Delete Desensitize List Relationship
     *
     * @param idList Desensitize List Relationship IDs
     */
    int removeDgDesensitizeAssetcolumn(Collection<Long> idList);

    /**
     * Get Desensitize List Relationship Detail
     *
     * @param id Desensitize List Relationship ID
     * @return Desensitize List Relationship
     */
    DgDesensitizeAssetcolumnDO getDgDesensitizeAssetcolumnById(Long id);

    DgDesensitizeAssetcolumnDO getDgDesensitizeAssetcolumnByAid(Long assetcolumnId);

    /**
     * Get All Desensitize List Relationship List
     *
     * @return Desensitize List Relationship list
     */
    List<DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnList();

    /**
     * Get All Desensitize List Relationship Map
     *
     * @return Desensitize List Relationship Map
     */
    Map<Long, DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnMap();


    /**
     * Import Desensitize List Relationship Data
     *
     * @param importExcelList Desensitize List Relationship data list
     * @param isUpdateSupport Whether to support update, if already exists, update the data
     * @param operName Operator name
     * @return Result
     */
    String importDgDesensitizeAssetcolumn(List<DgDesensitizeAssetcolumnRespVO> importExcelList, boolean isUpdateSupport, String operName);

    PageResult<DgDesensitizeAssetcolumnDO> getDgDesensitizePagebyRuleId(DgDesensitizeAssetcolumnPageReqVO dgDesensitizeAssetcolumn);

    DgDesensitizeAssetcolumnDO getByassetcolumnId(Long assetcolumnId);

    int deleteByassetcolumnId(Long assetcolumnId);
}
