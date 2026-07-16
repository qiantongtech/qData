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

package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttAssetCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Asset Category Management Service Interface
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttAssetCatService extends IService<AttAssetCatDO> {

    /**
     * Get data asset category management paginated list
     *
     * @param pageReqVO Page request
     * @return Data asset category management paginated list
     */
    PageResult<AttAssetCatDO> getAttAssetCatPage(AttAssetCatPageReqVO pageReqVO);

    /**
     * Create data asset category management
     *
     * @param createReqVO Data asset category management info
     * @return Data asset category management ID
     */
    Long createAttAssetCat(AttAssetCatSaveReqVO createReqVO);

    /**
     * Update data asset category management
     *
     * @param updateReqVO Data asset category management info
     */
    int updateAttAssetCat(AttAssetCatSaveReqVO updateReqVO);

    /**
     * Delete data asset category management
     *
     * @param idList Data asset category management ID list
     */
    int removeAttAssetCat(Collection<Long> idList);


    /**
     * Get data asset category management details
     *
     * @param id Data asset category management ID
     * @return Data asset category management
     */
    AttAssetCatDO getAttAssetCatById(Long id);

    /**
     * Get all data asset category management list
     *
     * @return Data asset category management list
     */
    List<AttAssetCatDO> getAttAssetCatList();

    /**
     * Get all data asset category management list
     *
     * @return Data asset category management list
     */
    List<AttAssetCatDO> getAttAssetCatList(AttAssetCatPageReqVO reqVO);

    /**
     * Get all data asset category management Map
     *
     * @return Data asset category management Map
     */
    Map<Long, AttAssetCatDO> getAttAssetCatMap();


    /**
     * Import data asset category management data
     *
     * @param importExcelList Data asset category management data list
     * @param isUpdateSupport Whether to support update, if already exists, update the data
     * @param operName        Operator
     * @return Result
     */
    String importAttAssetCat(List<AttAssetCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
