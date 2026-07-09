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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditSchedulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditScheduleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditScheduleSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditScheduleDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Asset Audit Schedule Service Interface
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface IDaAssetAuditScheduleService extends IService<DaAssetAuditScheduleDO> {

    /**
     * Get asset audit schedule page list
     *
     * @param pageReqVO page request
     * @return asset audit schedule page list
     */
    PageResult<DaAssetAuditScheduleDO> getDaAssetAuditSchedulePage(DaAssetAuditSchedulePageReqVO pageReqVO);

    /**
     * Create asset audit schedule
     *
     * @param createReqVO asset audit schedule info
     * @return asset audit schedule ID
     */
    Long createDaAssetAuditSchedule(DaAssetAuditScheduleSaveReqVO createReqVO);

    /**
     * Update asset audit schedule
     *
     * @param updateReqVO asset audit schedule info
     */
    int updateDaAssetAuditSchedule(DaAssetAuditScheduleSaveReqVO updateReqVO);

    /**
     * Delete asset audit schedule
     *
     * @param idList asset audit schedule ID list
     */
    int removeDaAssetAuditSchedule(Collection<Long> idList);

    /**
     * Get asset audit schedule details
     *
     * @param id asset audit schedule ID
     * @return asset audit schedule
     */
    DaAssetAuditScheduleDO getDaAssetAuditScheduleById(Long id);

    /**
     * Get all asset audit schedule list
     *
     * @return asset audit schedule list
     */
    List<DaAssetAuditScheduleDO> getDaAssetAuditScheduleList();

    /**
     * Get all asset audit schedule Map
     *
     * @return asset audit schedule Map
     */
    Map<Long, DaAssetAuditScheduleDO> getDaAssetAuditScheduleMap();


    /**
     * Import asset audit schedule data
     *
     * @param importExcelList asset audit schedule data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetAuditSchedule(List<DaAssetAuditScheduleRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
