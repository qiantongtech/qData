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

package tech.qiantong.qdata.module.da.service.daAssetApply;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplySaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.daAssetApply.DaAssetApplyDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Asset Application Service Interface
 *
 * @author shu
 * @date 2025-03-19
 */
public interface IDaAssetApplyService extends IService<DaAssetApplyDO> {

    /**
     * Get data asset application page list
     *
     * @param pageReqVO page request
     * @return data asset application page list
     */
    PageResult<DaAssetApplyDO> getDaAssetApplyPage(DaAssetApplyPageReqVO pageReqVO);

    /**
     * Create data asset application
     *
     * @param createReqVO data asset application info
     * @return data asset application ID
     */
    Long createDaAssetApply(DaAssetApplySaveReqVO createReqVO);

    /**
     * Update data asset application
     *
     * @param updateReqVO data asset application info
     */
    int updateDaAssetApply(DaAssetApplySaveReqVO updateReqVO);

    /**
     * Delete data asset application
     *
     * @param idList data asset application ID list
     */
    int removeDaAssetApply(Collection<Long> idList);

    /**
     * Get data asset application details
     *
     * @param id data asset application ID
     * @return data asset application
     */
    DaAssetApplyDO getDaAssetApplyById(Long id);

    /**
     * Get all data asset application list
     *
     * @return data asset application list
     */
    List<DaAssetApplyDO> getDaAssetApplyList();

    /**
     * Get all data asset application Map
     *
     * @return data asset application Map
     */
    Map<Long, DaAssetApplyDO> getDaAssetApplyMap();


    /**
     * Import data asset application data
     *
     * @param importExcelList data asset application data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetApply(List<DaAssetApplyRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
