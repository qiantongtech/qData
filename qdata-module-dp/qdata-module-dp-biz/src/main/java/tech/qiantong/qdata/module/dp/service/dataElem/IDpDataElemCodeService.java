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
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodePageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodeRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodeSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemCodeDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Element Code Service Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpDataElemCodeService extends IService<DpDataElemCodeDO> {

    /**
     * Get data element code paginated list
     *
     * @param pageReqVO Pagination request
     * @return Paginated list of data element codes
     */
    PageResult<DpDataElemCodeDO> getDpDataElemCodePage(DpDataElemCodePageReqVO pageReqVO);

    /**
     * Create data element code
     *
     * @param createReqVO Data element code information
     * @return Data element code ID
     */
    Long createDpDataElemCode(DpDataElemCodeSaveReqVO createReqVO);

    /**
     * Update data element code
     *
     * @param updateReqVO Data element code information
     */
    int updateDpDataElemCode(DpDataElemCodeSaveReqVO updateReqVO);

    /**
     * Delete data element code
     *
     * @param idList Data element code ID list
     */
    int removeDpDataElemCode(Collection<Long> idList);

    /**
     * Get data element code details
     *
     * @param id Data element code ID
     * @return Data element code
     */
    DpDataElemCodeDO getDpDataElemCodeById(Long id);

    /**
     * Get all data element code list
     *
     * @return Data element code list
     */
    List<DpDataElemCodeDO> getDpDataElemCodeList();

    /**
     * Get all data element code Map
     *
     * @return Data element code Map
     */
    Map<Long, DpDataElemCodeDO> getDpDataElemCodeMap();


    /**
     * Import data element code data
     *
     * @param importExcelList Data element code data list
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName        Operator
     * @return Result
     */
    String importDpDataElemCode(List<DpDataElemCodeRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Validate source code value
     *
     * @param dataElemId
     * @param codeValue
     * @param id
     * @return
     */
    Integer validateCodeValue(String dataElemId, String codeValue, String id);
}
