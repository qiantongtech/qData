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
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Discovery Task Log Service Interface
 *
 * @author qdata
 * @date 2025-02-17
 */
public interface IDaDiscoveryTaskLogService extends IService<DaDiscoveryTaskLogDO> {

    /**
     * Get data discovery task log page list
     *
     * @param pageReqVO page request
     * @return data discovery task log page list
     */
    PageResult<DaDiscoveryTaskLogDO> getDaDiscoveryTaskLogPage(DaDiscoveryTaskLogPageReqVO pageReqVO);

    /**
     * Create data discovery task log
     *
     * @param createReqVO data discovery task log info
     * @return data discovery task log ID
     */
    Long createDaDiscoveryTaskLog(DaDiscoveryTaskLogSaveReqVO createReqVO);

    /**
     * Update data discovery task log
     *
     * @param updateReqVO data discovery task log info
     */
    int updateDaDiscoveryTaskLog(DaDiscoveryTaskLogSaveReqVO updateReqVO);

    /**
     * Delete data discovery task log
     *
     * @param idList data discovery task log ID list
     */
    int removeDaDiscoveryTaskLog(Collection<Long> idList);

    /**
     * Get data discovery task log details
     *
     * @param id data discovery task log ID
     * @return data discovery task log
     */
    DaDiscoveryTaskLogDO getDaDiscoveryTaskLogById(Long id);

    /**
     * Get all data discovery task log list
     *
     * @return data discovery task log list
     */
    List<DaDiscoveryTaskLogDO> getDaDiscoveryTaskLogList();

    /**
     * Get all data discovery task log Map
     *
     * @return data discovery task log Map
     */
    Map<Long, DaDiscoveryTaskLogDO> getDaDiscoveryTaskLogMap();


    /**
     * Import data discovery task log data
     *
     * @param importExcelList data discovery task log data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaDiscoveryTaskLog(List<DaDiscoveryTaskLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    String getLogInfo(Long id);
}
