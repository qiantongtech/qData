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

package tech.qiantong.qdata.module.da.service.discovery;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTablePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTableRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTableSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTableDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Discovery Database Info Service Interface
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface IDaDiscoveryTableService extends IService<DaDiscoveryTableDO> {

    /**
     * Get data discovery database info page list
     *
     * @param pageReqVO page request
     * @return data discovery database info page list
     */
    PageResult<DaDiscoveryTableDO> getDaDiscoveryTablePage(DaDiscoveryTablePageReqVO pageReqVO);

    /**
     * Get all data discovery database info list
     *
     * @return data discovery database info list
     */
    List<DaDiscoveryTableDO> getDaDiscoveryTableList(DaDiscoveryTablePageReqVO discoveryTablePageReqVO);

    /**
     * Create data discovery database info
     *
     * @param createReqVO data discovery database info
     * @return data discovery database info ID
     */
    Long createDaDiscoveryTable(DaDiscoveryTableSaveReqVO createReqVO);
    Long createDaDiscoveryTable(DaDiscoveryTableDO createReqVO);

    /**
     * Update data discovery database info
     *
     * @param updateReqVO data discovery database info
     */
    int updateDaDiscoveryTable(DaDiscoveryTableSaveReqVO updateReqVO);
    int updateDaDiscoveryTable(DaDiscoveryTableDO updateReqVO);

    /**
     * Delete data discovery database info
     *
     * @param idList data discovery database info ID list
     */
    int removeDaDiscoveryTable(Collection<Long> idList);

    /**
     * Get data discovery database info details
     *
     * @param id data discovery database info ID
     * @return data discovery database info
     */
    DaDiscoveryTableDO getDaDiscoveryTableById(Long id);

    /**
     * Get all data discovery database info list
     *
     * @return data discovery database info list
     */
    List<DaDiscoveryTableDO> getDaDiscoveryTableList();

    /**
     * Get all data discovery database info Map
     *
     * @return data discovery database info Map
     */
    Map<Long, DaDiscoveryTableDO> getDaDiscoveryTableMap();


    /**
     * Import data discovery database info data
     *
     * @param importExcelList data discovery database info data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaDiscoveryTable(List<DaDiscoveryTableRespVO> importExcelList, boolean isUpdateSupport, String operName);

    Integer commitOrRevokeDiscoveryInfo(DaDiscoveryTableSaveReqVO daDiscoveryTable);


    Integer updateByTaskIdListAndTableNameStatus(DaDiscoveryTableSaveReqVO daDiscoveryTable);
}
