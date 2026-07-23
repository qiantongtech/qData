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

package tech.qiantong.qdata.module.dp.service.dataElem;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemRuleRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Element Rule Relation Information Service Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpDataElemRuleRelService extends IService<DpDataElemRuleRelDO> {

    /**
     * Get data element rule relation information paginated list
     *
     * @param pageReqVO Pagination request
     * @return Paginated list of data element rule relation information
     */
    PageResult<DpDataElemRuleRelDO> getDpDataElemRuleRelPage(DpDataElemRuleRelPageReqVO pageReqVO);

    /**
     * Create data element rule relation information
     *
     * @param createReqVO Data element rule relation information
     * @return Data element rule relation information ID
     */
    Long createDpDataElemRuleRel(DpDataElemRuleRelSaveReqVO createReqVO);

    /**
     * Update data element rule relation information
     *
     * @param updateReqVO Data element rule relation information
     */
    int updateDpDataElemRuleRel(DpDataElemRuleRelSaveReqVO updateReqVO);

    /**
     * Delete data element rule relation information
     *
     * @param idList Data element rule relation information ID list
     */
    int removeDpDataElemRuleRel(Collection<Long> idList);

    /**
     * Get data element rule relation information details
     *
     * @param id Data element rule relation information ID
     * @return Data element rule relation information
     */
    DpDataElemRuleRelDO getDpDataElemRuleRelById(Long id);

    /**
     * Get all data element rule relation information list
     *
     * @return Data element rule relation information list
     */
    List<DpDataElemRuleRelDO> getDpDataElemRuleRelList();

    /**
     * Get all data element rule relation information Map
     *
     * @return Data element rule relation information Map
     */
    Map<Long, DpDataElemRuleRelDO> getDpDataElemRuleRelMap();


    /**
     * Import data element rule relation information data
     *
     * @param importExcelList Data element rule relation information data list
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName        Operator
     * @return Result
     */
    String importDpDataElemRuleRel(List<DpDataElemRuleRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
