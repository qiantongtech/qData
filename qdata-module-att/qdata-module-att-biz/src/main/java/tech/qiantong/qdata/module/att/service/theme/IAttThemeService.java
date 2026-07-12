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

package tech.qiantong.qdata.module.att.service.theme;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemeRespVO;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemeSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.theme.AttThemeDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Theme Service Interface
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttThemeService extends IService<AttThemeDO> {

    /**
     * Get Theme paginated list
     *
     *  pageReqVO Page request
     *  Theme paginated list
     */
    PageResult<AttThemeDO> getAttThemePage(AttThemePageReqVO pageReqVO);

    /**
     * Create Theme
     *
     *  createReqVO Theme info
     *  Theme ID
     */
    Long createAttTheme(AttThemeSaveReqVO createReqVO);

    /**
     * Update Theme
     *
     *  updateReqVO Theme info
     */
    int updateAttTheme(AttThemeSaveReqVO updateReqVO);

    /**
     * Delete Theme
     *
     *  idList Theme ID list
     */
    int removeAttTheme(Collection<Long> idList);

    /**
     * Get Theme details
     *
     *  id Theme ID
     *  Theme
     */
    AttThemeDO getAttThemeById(Long id);

    /**
     * Get all Theme list
     *
     *  Theme list
     */
    List<AttThemeDO> getAttThemeList();

    /**
     * Get all Theme Map
     *
     *  Theme Map
     */
    Map<Long, AttThemeDO> getAttThemeMap();


    /**
     * Import Theme data
     *
     *  importExcelList Theme data list
     * @param isUpdateSupport Whether update is supported; if already exists, update the data
     *  operName Operator
     *  Result
     */
    String importAttTheme(List<AttThemeRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<AttThemeDO> getAttThemeListByReqVO(AttThemePageReqVO attTheme);
}
