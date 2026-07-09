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
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryColumnDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Discovery Column Service Interface
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface IDaDiscoveryColumnService extends IService<DaDiscoveryColumnDO> {

    /**
     * Get data discovery column page list
     *
     * @param pageReqVO page request
     * @return data discovery column page list
     */
    PageResult<DaDiscoveryColumnDO> getDaDiscoveryColumnPage(DaDiscoveryColumnPageReqVO pageReqVO);

    /**
     * Get all data discovery column list
     *
     * @return data discovery column list
     */
    List<DaDiscoveryColumnDO> getDaDiscoveryColumnList(DaDiscoveryColumnPageReqVO pageReqVO);

    /**
     * Create data discovery column
     *
     * @param createReqVO data discovery column info
     * @return data discovery column ID
     */
    Long createDaDiscoveryColumn(DaDiscoveryColumnSaveReqVO createReqVO);
    Long createDaDiscoveryColumn(DaDiscoveryColumnDO createReqVO);

    /**
     * Update data discovery column
     *
     * @param updateReqVO data discovery column info
     */
    int updateDaDiscoveryColumn(DaDiscoveryColumnSaveReqVO updateReqVO);
    int updateDaDiscoveryColumn(DaDiscoveryColumnDO updateReqVO);

    /**
     * Delete data discovery column
     *
     * @param idList data discovery column ID list
     */
    int removeDaDiscoveryColumn(Collection<Long> idList);

    /**
     * Get data discovery column details
     *
     * @param id data discovery column ID
     * @return data discovery column
     */
    DaDiscoveryColumnDO getDaDiscoveryColumnById(Long id);

    /**
     * Get all data discovery column list
     *
     * @return data discovery column list
     */
    List<DaDiscoveryColumnDO> getDaDiscoveryColumnList();

    /**
     * Get all data discovery column Map
     *
     * @return data discovery column Map
     */
    Map<Long, DaDiscoveryColumnDO> getDaDiscoveryColumnMap();


    /**
     * Import data discovery column data
     *
     * @param importExcelList data discovery column data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaDiscoveryColumn(List<DaDiscoveryColumnRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
