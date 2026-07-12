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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataElemCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Element Category Management Service Interface
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttDataElemCatService extends IService<AttDataElemCatDO> {

    /**
     * Get Data Element Category Management paginated list
     *
     * @param pageReqVO Page request
     * @return Data Element Category Management paginated list
     */
    PageResult<AttDataElemCatDO> getAttDataElemCatPage(AttDataElemCatPageReqVO pageReqVO);

    /**
     * Create Data Element Category Management
     *
     * @param createReqVO Data Element Category Management info
     * @return Data Element Category Management ID
     */
    Long createAttDataElemCat(AttDataElemCatSaveReqVO createReqVO);

    /**
     * Update Data Element Category Management
     *
     * @param updateReqVO Data Element Category Management info
     */
    int updateAttDataElemCat(AttDataElemCatSaveReqVO updateReqVO);

    /**
     * Delete Data Element Category Management
     *
     * @param idList Data Element Category Management ID list
     */
    int removeAttDataElemCat(Collection<Long> idList);

    /**
     * Get Data Element Category Management details
     *
     * @param id Data Element Category Management ID
     * @return Data Element Category Management
     */
    AttDataElemCatDO getAttDataElemCatById(Long id);

    /**
     * Get all Data Element Category Management list
     *
     * @return Data Element Category Management list
     */
    List<AttDataElemCatDO> getAttDataElemCatList();

    /**
     * Get all Data Element Category Management list
     *
     * @return Data Element Category Management list
     */
    List<AttDataElemCatDO> getAttDataElemCatList(AttDataElemCatPageReqVO reqVO);

    /**
     * Get all Data Element Category Management Map
     *
     * @return Data Element Category Management Map
     */
    Map<Long, AttDataElemCatDO> getAttDataElemCatMap();


    /**
     * Import Data Element Category Management data
     *
     * @param importExcelList Data Element Category Management data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importAttDataElemCat(List<AttDataElemCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
