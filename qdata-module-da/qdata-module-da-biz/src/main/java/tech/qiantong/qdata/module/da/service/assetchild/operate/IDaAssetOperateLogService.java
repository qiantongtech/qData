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

package tech.qiantong.qdata.module.da.service.assetchild.operate;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Asset Operation Record Service Interface
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface IDaAssetOperateLogService extends IService<DaAssetOperateLogDO> {

    /**
     * Get data asset operation record page list
     *
     * @param pageReqVO page request
     * @return data asset operation record page list
     */
    PageResult<DaAssetOperateLogDO> getDaAssetOperateLogPage(DaAssetOperateLogPageReqVO pageReqVO);

    /**
     * Create data asset operation record
     *
     * @param createReqVO data asset operation record info
     * @return data asset operation record ID
     */
    Long createDaAssetOperateLog(DaAssetOperateLogSaveReqVO createReqVO);

    /**
     * Update data asset operation record
     *
     * @param updateReqVO data asset operation record info
     */
    int updateDaAssetOperateLog(DaAssetOperateLogSaveReqVO updateReqVO);

    /**
     * Delete data asset operation record
     *
     * @param idList data asset operation record ID list
     */
    int removeDaAssetOperateLog(Collection<Long> idList);

    /**
     * Get data asset operation record details
     *
     * @param id data asset operation record ID
     * @return data asset operation record
     */
    DaAssetOperateLogDO getDaAssetOperateLogById(Long id);

    /**
     * Get all data asset operation record list
     *
     * @return data asset operation record list
     */
    List<DaAssetOperateLogDO> getDaAssetOperateLogList();

    /**
     * Get all data asset operation record Map
     *
     * @return data asset operation record Map
     */
    Map<Long, DaAssetOperateLogDO> getDaAssetOperateLogMap();


    /**
     * Import data asset operation record data
     *
     * @param importExcelList data asset operation record data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetOperateLog(List<DaAssetOperateLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    void rollBack(Long id);

    PageResult<DaAssetOperateLogDO> queryDaAssetOperateLogPage(DaAssetOperateLogPageReqVO daAssetOperateLog);
}
