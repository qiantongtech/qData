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

package tech.qiantong.qdata.module.dg.service.dataLevel;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataLevel.DgDataLevelDO;
/**
 * Data Level Service Interface
 *
 * @author qdata
 * @date 2026-04-03
 */
public interface IDgDataLevelService extends IService<DgDataLevelDO> {

    /**
     * Get data level paginated list
     *
     * @param pageReqVO Pagination request
     * @return Data level paginated list
     */
    PageResult<DgDataLevelDO> getDgDataLevelPage(DgDataLevelPageReqVO pageReqVO);

    /**
     * Create data level
     *
     * @param createReqVO Data level information
     * @return Data level ID
     */
    Long createDgDataLevel(DgDataLevelSaveReqVO createReqVO);

    /**
     * Update data level
     *
     * @param updateReqVO Data level information
     */
    int updateDgDataLevel(DgDataLevelSaveReqVO updateReqVO);

    /**
     * Delete data level
     *
     * @param idList Data level IDs
     */
    int removeDgDataLevel(Collection<Long> idList);

    /**
     * Get data level details
     *
     * @param id Data level ID
     * @return Data level
     */
    DgDataLevelDO getDgDataLevelById(Long id);

    /**
     * Get all data level list
     *
     * @return Data level list
     */
    List<DgDataLevelDO> getDgDataLevelList();

    /**
     * Get all data level Map
     *
     * @return Data level Map
     */
    Map<Long, DgDataLevelDO> getDgDataLevelMap();


    /**
     * Import data level data
     *
     * @param importExcelList Data level data list
     * @param isUpdateSupport Whether to update support, if exists then update data
     * @param operName Operator user
     * @return Result
     */
    String importDgDataLevel(List<DgDataLevelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<DgDataLevelDO> getDgDataLevelListAll(DgDataLevelPageReqVO dgDataLevel);
}
