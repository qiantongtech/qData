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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTaskCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Integration Task Category Management Service Interface
 *
 * @author qdata
 * @date 2025-03-11
 */
public interface IAttTaskCatService extends IService<AttTaskCatDO> {

    /**
     * Get data integration task category management paginated list
     *
     * @param pageReqVO page request
     * @return data integration task category management paginated list
     */
    PageResult<AttTaskCatDO> getAttTaskCatPage(AttTaskCatPageReqVO pageReqVO);

    /**
     * Create data integration task category management
     *
     * @param createReqVO data integration task category management info
     * @return data integration task category management ID
     */
    Long createAttTaskCat(AttTaskCatSaveReqVO createReqVO);

    /**
     * Update data integration task category management
     *
     * @param updateReqVO data integration task category management info
     */
    int updateAttTaskCat(AttTaskCatSaveReqVO updateReqVO);

    /**
     * Delete data integration task category management
     *
     * @param idList data integration task category management ID list
     */
    int removeAttTaskCat(Collection<Long> idList);

    /**
     * Get data integration task category management details
     *
     * @param id data integration task category management ID
     * @return data integration task category management
     */
    AttTaskCatDO getAttTaskCatById(Long id);

    /**
     * Get all data integration task category management list
     *
     * @return data integration task category management list
     */
    List<AttTaskCatDO> getAttTaskCatList();

    /**
     * Get all data integration task category management list
     *
     * @return data integration task category management list
     */
    List<AttTaskCatDO> getAttTaskCatList(AttTaskCatPageReqVO reqVO);

    /**
     * Get all data integration task category management Map
     *
     * @return data integration task category management Map
     */
    Map<Long, AttTaskCatDO> getAttTaskCatMap();


    /**
     * Import data integration task category management data
     *
     * @param importExcelList data integration task category management data list
     * @param isUpdateSupport whether to support update, if already exists, update the data
     * @param operName operator
     * @return result
     */
    String importAttTaskCat(List<AttTaskCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
