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

package tech.qiantong.qdata.module.system.service.updater;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackRespVO;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackSaveReqVO;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackPageReqVO;
import tech.qiantong.qdata.module.system.dal.dataobject.updater.SystemVersionTrackDO;
/**
 * 版本跟踪Service interface
 *
 * @author qdata
 * @date 2026-08-12
 */
public interface ISystemVersionTrackService extends IService<SystemVersionTrackDO> {

    /**
     * Get 版本跟踪 paginated list
     *
     * @param pageReqVO paging request
     * @return 版本跟踪paginated list
     */
    PageResult<SystemVersionTrackDO> getSystemVersionTrackPage(SystemVersionTrackPageReqVO pageReqVO);

    /**
     * Create 版本跟踪
     *
     * @param createReqVO 版本跟踪 information
     * @return 版本跟踪 number
     */
    Long createSystemVersionTrack(SystemVersionTrackSaveReqVO createReqVO);

    /**
     * Update 版本跟踪
     *
     * @param updateReqVO 版本跟踪 information
     */
    int updateSystemVersionTrack(SystemVersionTrackSaveReqVO updateReqVO);

    /**
     * Delete 版本跟踪
     *
     * @param idList 版本跟踪number
     */
    int removeSystemVersionTrack(Collection<Long> idList);

    /**
     * Get 版本跟踪 details
     *
     * @param id 版本跟踪 number
     * @return 版本跟踪
     */
    SystemVersionTrackDO getSystemVersionTrackById(Long id);

    /**
     * Get the list of all 版本跟踪
     *
     * @return 版本跟踪 list
     */
    List<SystemVersionTrackDO> getSystemVersionTrackList();

    /**
     * Get all 版本跟踪 Map
     *
     * @return 版本跟踪 Map
     */
    Map<Long, SystemVersionTrackDO> getSystemVersionTrackMap();


    /**
     * Import 版本跟踪 data
     *
     * @param importExcelList 版本跟踪data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    String importSystemVersionTrack(List<SystemVersionTrackRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
