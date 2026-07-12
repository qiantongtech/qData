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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataDevCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Data Development Category Management Service Interface
 *
 * @author qdata
 * @date 2025-03-11
 */
public interface IAttDataDevCatService extends IService<AttDataDevCatDO> {

    /**
     * Get data development category management paginated list
     *
     * @param pageReqVO page request
     * @return data development category management paginated list
     */
    PageResult<AttDataDevCatDO> getAttDataDevCatPage(AttDataDevCatPageReqVO pageReqVO);

    /**
     * Create data development category management
     *
     * @param createReqVO data development category management info
     * @return data development category management ID
     */
    Long createAttDataDevCat(AttDataDevCatSaveReqVO createReqVO);

    /**
     * Update data development category management
     *
     * @param updateReqVO data development category management info
     */
    int updateAttDataDevCat(AttDataDevCatSaveReqVO updateReqVO);

    /**
     * Delete data development category management
     *
     * @param idList data development category management ID list
     */
    int removeAttDataDevCat(Collection<Long> idList);

    /**
     * Get data development category management details
     *
     * @param id data development category management ID
     * @return data development category management
     */
    AttDataDevCatDO getAttDataDevCatById(Long id);

    /**
     * Get all data development category management list
     *
     * @return data development category management list
     */
    List<AttDataDevCatDO> getAttDataDevCatList();

    /**
     * Get all data development category management list
     *
     * @return data development category management list
     */
    List<AttDataDevCatDO> getAttDataDevCatList(AttDataDevCatPageReqVO reqVO);

    /**
     * Get all data development category management Map
     *
     * @return data development category management Map
     */
    Map<Long, AttDataDevCatDO> getAttDataDevCatMap();


    /**
     * Import data development category management data
     *
     * @param importExcelList data development category management data list
     * @param isUpdateSupport whether to support update, if already exists, then update the data
     * @param operName operating user
     * @return result
     */
    String importAttDataDevCat(List<AttDataDevCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
