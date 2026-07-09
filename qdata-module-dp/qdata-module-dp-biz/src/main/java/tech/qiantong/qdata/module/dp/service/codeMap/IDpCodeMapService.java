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

package tech.qiantong.qdata.module.dp.service.codeMap;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.codeMap.DpCodeMapDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Element Code Map Service Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpCodeMapService extends IService<DpCodeMapDO> {

    /**
     * Get data element code map paginated list
     *
     * @param pageReqVO Pagination request
     * @return Paginated list of data element code maps
     */
    PageResult<DpCodeMapDO> getDpCodeMapPage(DpCodeMapPageReqVO pageReqVO);

    /**
     * Create data element code map
     *
     * @param createReqVO Data element code map information
     * @return Data element code map ID
     */
    Long createDpCodeMap(DpCodeMapSaveReqVO createReqVO);

    /**
     * Update data element code map
     *
     * @param updateReqVO Data element code map information
     */
    int updateDpCodeMap(DpCodeMapSaveReqVO updateReqVO);

    /**
     * Delete data element code map
     *
     * @param idList Data element code map ID list
     */
    int removeDpCodeMap(Collection<Long> idList);

    /**
     * Get data element code map details
     *
     * @param id Data element code map ID
     * @return Data element code map
     */
    DpCodeMapDO getDpCodeMapById(Long id);

    /**
     * Get all data element code map list
     *
     * @return Data element code map list
     */
    List<DpCodeMapDO> getDpCodeMapList();

    /**
     * Get all data element code map Map
     *
     * @return Data element code map Map
     */
    Map<Long, DpCodeMapDO> getDpCodeMapMap();


    /**
     * Import data element code map data
     *
     * @param importExcelList Data element code map data list
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName Operator
     * @return Result
     */
    String importDpCodeMap(List<DpCodeMapRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
