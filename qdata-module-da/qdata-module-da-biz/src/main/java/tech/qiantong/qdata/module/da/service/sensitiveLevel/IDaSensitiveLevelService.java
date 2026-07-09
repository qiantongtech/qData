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

package tech.qiantong.qdata.module.da.service.sensitiveLevel;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.sensitiveLevel.DaSensitiveLevelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Sensitive Level Service Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDaSensitiveLevelService extends IService<DaSensitiveLevelDO> {

    /**
     * Get sensitive level page list
     *
     * @param pageReqVO page request
     * @return sensitive level page list
     */
    PageResult<DaSensitiveLevelDO> getDaSensitiveLevelPage(DaSensitiveLevelPageReqVO pageReqVO);

    /**
     * Create sensitive level
     *
     * @param createReqVO sensitive level info
     * @return sensitive level ID
     */
    Long createDaSensitiveLevel(DaSensitiveLevelSaveReqVO createReqVO);

    /**
     * Update sensitive level
     *
     * @param updateReqVO sensitive level info
     */
    int updateDaSensitiveLevel(DaSensitiveLevelSaveReqVO updateReqVO);

    /**
     * Delete sensitive level
     *
     * @param idList sensitive level ID list
     */
    int removeDaSensitiveLevel(Collection<Long> idList);

    /**
     * Get sensitive level details
     *
     * @param id sensitive level ID
     * @return sensitive level
     */
    DaSensitiveLevelDO getDaSensitiveLevelById(Long id);

    /**
     * Get all sensitive level list
     *
     * @return sensitive level list
     */
    List<DaSensitiveLevelDO> getDaSensitiveLevelList();

    /**
     * Get all sensitive level Map
     *
     * @return sensitive level Map
     */
    Map<Long, DaSensitiveLevelDO> getDaSensitiveLevelMap();


    /**
     * Import sensitive level data
     *
     * @param importExcelList sensitive level data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaSensitiveLevel(List<DaSensitiveLevelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * Modify status
     * @param id primary key
     * @param status status value
     * @return
     */
    Boolean updateStatus(Long id, Long status);
}
