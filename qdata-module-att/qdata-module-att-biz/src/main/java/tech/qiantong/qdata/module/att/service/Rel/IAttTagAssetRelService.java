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

package tech.qiantong.qdata.module.att.service.Rel;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Rel.AttTagAssetRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Tag-Asset Relationship Service Interface
 *
 * @author qdata
 * @date 2025-07-11
 */
public interface IAttTagAssetRelService extends IService<AttTagAssetRelDO> {

    /**
     * Get Tag-Asset Relationship paginated list
     *
     * @param pageReqVO Page request
     * @return Tag-Asset Relationship paginated list
     */
    PageResult<AttTagAssetRelDO> getAttTagAssetRelPage(AttTagAssetRelPageReqVO pageReqVO);

    /**
     * Create Tag-Asset Relationship
     *
     * @param createReqVO Tag-Asset Relationship info
     * @return Tag-Asset Relationship ID
     */
    Long createAttTagAssetRel(AttTagAssetRelSaveReqVO createReqVO);

    /**
     * Update Tag-Asset Relationship
     *
     * @param updateReqVO Tag-Asset Relationship info
     */
    int updateAttTagAssetRel(AttTagAssetRelSaveReqVO updateReqVO);

    /**
     * Delete Tag-Asset Relationship
     *
     * @param idList Tag-Asset Relationship IDs
     */
    int removeAttTagAssetRel(Collection<Long> idList);



    /**
     * Get Tag-Asset Relationship details
     *
     * @param id Tag-Asset Relationship ID
     * @return Tag-Asset Relationship
     */
    AttTagAssetRelDO getAttTagAssetRelById(Long id);

    /**
     * Get all Tag-Asset Relationship list
     *
     * @return Tag-Asset Relationship list
     */
    List<AttTagAssetRelDO> getAttTagAssetRelList();

    /**
     * Get all Tag-Asset Relationship Map
     *
     * @return Tag-Asset Relationship Map
     */
    Map<Long, AttTagAssetRelDO> getAttTagAssetRelMap();


    /**
     * Import Tag-Asset Relationship data
     *
     * @param importExcelList Tag-Asset Relationship data list
     * @param isUpdateSupport Whether to support update, if already exists, then update data
     * @param operName Operator
     * @return Result
     */
    String importAttTagAssetRel(List<AttTagAssetRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    int removeAttTagAssetRel(Long id, AttTagAssetRelPageReqVO attTagAssetRel);
}
