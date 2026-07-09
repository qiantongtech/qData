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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplySaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateApplyDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Asset Operation Application Service Interface
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface IDaAssetOperateApplyService extends IService<DaAssetOperateApplyDO> {

    /**
     * Get data asset operation application page list
     *
     * @param pageReqVO page request
     * @return data asset operation application page list
     */
    PageResult<DaAssetOperateApplyDO> getDaAssetOperateApplyPage(DaAssetOperateApplyPageReqVO pageReqVO);

    /**
     * Create data asset operation application
     *
     * @param createReqVO data asset operation application info
     * @return data asset operation application ID
     */
    Long createDaAssetOperateApply(DaAssetOperateApplySaveReqVO createReqVO);

    /**
     * Update data asset operation application
     *
     * @param updateReqVO data asset operation application info
     */
    int updateDaAssetOperateApply(DaAssetOperateApplySaveReqVO updateReqVO);

    /**
     * Delete data asset operation application
     *
     * @param idList data asset operation application ID list
     */
    int removeDaAssetOperateApply(Collection<Long> idList);

    /**
     * Get data asset operation application details
     *
     * @param id data asset operation application ID
     * @return data asset operation application
     */
    DaAssetOperateApplyDO getDaAssetOperateApplyById(Long id);

    /**
     * Get all data asset operation application list
     *
     * @return data asset operation application list
     */
    List<DaAssetOperateApplyDO> getDaAssetOperateApplyList();

    /**
     * Get all data asset operation application Map
     *
     * @return data asset operation application Map
     */
    Map<Long, DaAssetOperateApplyDO> getDaAssetOperateApplyMap();


    /**
     * Import data asset operation application data
     *
     * @param importExcelList data asset operation application data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetOperateApply(List<DaAssetOperateApplyRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
