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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dg.service.standard;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.standard.DgDataElemDO;

import java.util.List;

/**
 * Data Element Service Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDgDataElemService extends IService<DgDataElemDO> {

    /**
     * Get data element paginated list
     *
     * @param pageReqVO Pagination request
     * @return Data element paginated list
     */
    PageResult<DgDataElemDO> getDgDataElemPage(DgDataElemPageReqVO pageReqVO);

    List<DgDataElemDO> getDgDataElemList(DgDataElemPageReqVO pageReqVO);

    /**
     * Create data element
     *
     * @param createReqVO Data element information
     * @return Data element ID
     */
    Long createDgDataElem(DgDataElemSaveReqVO createReqVO);

    /**
     * Update data element
     *
     * @param updateReqVO Data element information
     */
    int updateDgDataElem(DgDataElemSaveReqVO updateReqVO);

    /**
     * Delete data element
     *
     * @param idList Data element IDs
     */
    int removeDgDataElem(List<Long> idList);

    /**
     * Get data element details
     *
     * @param id Data element ID
     * @return Data element
     */
    DgDataElemDO getDgDataElemById(Long id);

    /**
     * Update data element status
     *
     * @param id
     * @param status
     * @return
     */
    Boolean updateStatus(Long id, Long status);

}
