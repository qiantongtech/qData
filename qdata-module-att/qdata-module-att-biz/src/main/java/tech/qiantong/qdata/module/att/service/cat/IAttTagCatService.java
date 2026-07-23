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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTagCatDO;

import java.util.List;
import java.util.Map;

/**
 * Tag Category Management Service Interface
 *
 * @author qdata
 * @date 2025-07-11
 */
public interface IAttTagCatService extends IService<AttTagCatDO> {

    /**
     * Get tag category management paginated list
     *
     * @param pageReqVO Page request
     * @return Tag category management paginated list
     */
    PageResult<AttTagCatDO> getAttTagCatPage(AttTagCatPageReqVO pageReqVO);

    /**
     * Create tag category management
     *
     * @param createReqVO Tag category management info
     * @return Tag category management ID
     */
    Long createAttTagCat(AttTagCatSaveReqVO createReqVO);

    /**
     * Update tag category management
     *
     * @param updateReqVO Tag category management info
     */
    int updateAttTagCat(AttTagCatSaveReqVO updateReqVO);

    /**
     * Delete tag category management
     *
     * @param idList Tag category management ID
     */
//    int removeAttTagCat(Collection<Long> idList);

    /**
     * Get tag category management details
     *
     * @param id Tag category management ID
     * @return Tag category management
     */
    AttTagCatDO getAttTagCatById(Long id);

    /**
     * Get all tag category management list
     *
     * @return Tag category management list
     */
    List<AttTagCatDO> getAttTagCatList();

    /**
     * Get all tag category management Map
     *
     * @return Tag category management Map
     */
    Map<Long, AttTagCatDO> getAttTagCatMap();


    /**
     * Import tag category management data
     *
     * @param importExcelList Tag category management data list
     * @param isUpdateSupport Whether to support updates; if already exists, update the data
     * @param operName Operating user
     * @return Result
     */
    String importAttTagCat(List<AttTagCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Get all data tag category management list
     *
     * @return Data tag category management list
     */
    List<AttTagCatDO> getAttTagCatLIst(AttTagCatPageReqVO attTagCat);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

    /**
     * Change all codes under specified pid
     *
     * @param pid
     */
    void changeCodeByPid(Long pid, String parentCode);

    Integer removeAttTagCat(Long id);

}
