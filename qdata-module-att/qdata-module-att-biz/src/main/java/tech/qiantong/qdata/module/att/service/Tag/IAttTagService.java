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

package tech.qiantong.qdata.module.att.service.Tag;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Tag.AttTagDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Tag Management Service Interface
 *
 * @author qdata
 * @date 2025-07-11
 */
public interface IAttTagService extends IService<AttTagDO> {

    /**
     * Get tag management paginated list
     *
     * @param pageReqVO page request
     * @return tag management paginated list
     */
    PageResult<AttTagDO> getAttTagPage(AttTagPageReqVO pageReqVO);

    /**
     * Create Tag Management
     *
     * @param createReqVO tag management info
     * @return tag management ID
     */
    Long createAttTag(AttTagSaveReqVO createReqVO);

    /**
     * Update Tag Management
     *
     * @param updateReqVO tag management info
     */
    int updateAttTag(AttTagSaveReqVO updateReqVO);

    /**
     * Delete Tag Management
     *
     * @param idList tag management ID list
     */
    int removeAttTag(Collection<Long> idList);

    /**
     * Get tag management details
     *
     * @param id tag management ID
     * @return tag management
     */
    AttTagRespVO getAttTagById(Long id);

    /**
     * Get all tag management list
     *
     * @return tag management list
     */
    List<AttTagDO> getAttTagList();

    /**
     * Get all tag management Map
     *
     * @return tag management Map
     */
    Map<Long, AttTagDO> getAttTagMap();


    /**
     * Import tag management data
     *
     * @param importExcelList tag management data list
     * @param isUpdateSupport whether to support update, if already exists, then update the data
     * @param operName operator name
     * @return result
     */
    String importAttTag(List<AttTagRespVO> importExcelList, boolean isUpdateSupport, String operName);

    Long getCountByCatCode(String code);


    /**
     * Batch update old CAT_CODE to new CAT_CODE
     *
     * @param oldCatCode old category code
     * @param newCatCode new category code
     * @return affected row count
     */
    int updateCatCode(String oldCatCode, String newCatCode);
}
