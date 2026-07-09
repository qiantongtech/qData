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

package tech.qiantong.qdata.module.da.service.assetchild.audit;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditAlertDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Asset - Quality Alert Service Interface
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface IDaAssetAuditAlertService extends IService<DaAssetAuditAlertDO> {

    /**
     * Get data asset quality alert page list
     *
     * @param pageReqVO page request
     * @return data asset quality alert page list
     */
    PageResult<DaAssetAuditAlertDO> getDaAssetAuditAlertPage(DaAssetAuditAlertPageReqVO pageReqVO);

    /**
     * Create data asset quality alert
     *
     * @param createReqVO data asset quality alert info
     * @return data asset quality alert ID
     */
    Long createDaAssetAuditAlert(DaAssetAuditAlertSaveReqVO createReqVO);

    /**
     * Update data asset quality alert
     *
     * @param updateReqVO data asset quality alert info
     */
    int updateDaAssetAuditAlert(DaAssetAuditAlertSaveReqVO updateReqVO);

    /**
     * Delete data asset quality alert
     *
     * @param idList data asset quality alert ID list
     */
    int removeDaAssetAuditAlert(Collection<Long> idList);

    /**
     * Get data asset quality alert details
     *
     * @param id data asset quality alert ID
     * @return data asset quality alert
     */
    DaAssetAuditAlertDO getDaAssetAuditAlertById(Long id);

    /**
     * Get all data asset quality alert list
     *
     * @return data asset quality alert list
     */
    List<DaAssetAuditAlertDO> getDaAssetAuditAlertList();

    /**
     * Get all data asset quality alert Map
     *
     * @return data asset quality alert Map
     */
    Map<Long, DaAssetAuditAlertDO> getDaAssetAuditAlertMap();


    /**
     * Import data asset quality alert data
     *
     * @param importExcelList data asset quality alert data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetAuditAlert(List<DaAssetAuditAlertRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
