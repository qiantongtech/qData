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
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemCatPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemCatSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.standard.DgDataElemCatDO;

import java.util.Collection;
import java.util.List;

/**
 * Data Element Category Management Service Interface
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IDgDataElemCatService extends IService<DgDataElemCatDO> {

    /**
     * Create data element category management
     *
     * @param createReqVO Data element category management information
     * @return Data element category management ID
     */
    Long createDgDataElemCat(DgDataElemCatSaveReqVO createReqVO);

    /**
     * Update data element category management
     *
     * @param updateReqVO Data element category management information
     */
    int updateDgDataElemCat(DgDataElemCatSaveReqVO updateReqVO);

    /**
     * Delete data element category management
     *
     * @param idList Data element category management IDs
     */
    int removeDgDataElemCat(Collection<Long> idList);

    /**
     * Change all codes under the specified pid
     *
     * @param pid
     */
    void changeCodeByPid(Long pid, String parentCode);

    /**
     * Get data element category management details
     *
     * @param id Data element category management ID
     * @return Data element category management
     */
    DgDataElemCatDO getDgDataElemCatById(Long id);

    /**
     * Get all data element category management list
     *
     * @return Data element category management list
     */
    List<DgDataElemCatDO> getDgDataElemCatList(DgDataElemCatPageReqVO reqVO);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

    /**
     * Batch delete check, query the number of deletable and non-deletable items
     *
     * @param ids
     * @return
     */
    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids);
}
