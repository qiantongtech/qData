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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttQualityCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Quality Category Service Interface
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface IAttQualityCatService extends IService<AttQualityCatDO> {

    /**
     * Get Data Quality Category paginated list
     *
     * @param pageReqVO Page request
     * @return Data Quality Category paginated list
     */
    PageResult<AttQualityCatDO> getAttQualityCatPage(AttQualityCatPageReqVO pageReqVO);

    /**
     * Create Data Quality Category
     *
     * @param createReqVO Data Quality Category info
     * @return Data Quality Category ID
     */
    Long createAttQualityCat(AttQualityCatSaveReqVO createReqVO);

    /**
     * Update Data Quality Category
     *
     * @param updateReqVO Data Quality Category info
     */
    int updateAttQualityCat(AttQualityCatSaveReqVO updateReqVO);

    /**
     * Delete Data Quality Category
     *
     * @param idList Data Quality Category ID list
     */
    int removeAttQualityCat(Collection<Long> idList);

    /**
     * Get Data Quality Category details
     *
     * @param id Data Quality Category ID
     * @return Data Quality Category
     */
    AttQualityCatDO getAttQualityCatById(Long id);

    /**
     * Get all Data Quality Category list
     *
     * @return Data Quality Category list
     */
    List<AttQualityCatDO> getAttQualityCatList(AttQualityCatPageReqVO attQualityCat);

    /**
     * Get all Data Quality Category Map
     *
     * @return Data Quality Category Map
     */
    Map<Long, AttQualityCatDO> getAttQualityCatMap();


    /**
     * Import Data Quality Category data
     *
     * @param importExcelList Data Quality Category data list
     * @param isUpdateSupport Whether to update existing data if already present
     * @param operName Operator
     * @return Result
     */
    String importAttQualityCat(List<AttQualityCatRespVO> importExcelList, boolean isUpdateSupport, String operName);    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

}
