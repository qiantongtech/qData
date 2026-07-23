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

package tech.qiantong.qdata.module.da.service.assetchild.video;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.video.DaAssetVideoDO;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Asset - Video Data Service Interface
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetVideoService extends IService<DaAssetVideoDO> {

    /**
     * Get data asset video data page list
     *
     * @param pageReqVO page request
     * @return data asset video data page list
     */
    PageResult<DaAssetVideoDO> getDaAssetVideoPage(DaAssetVideoPageReqVO pageReqVO);

    DaAssetVideoRespVO getDaAssetVideoByAssetId(Long assetId);

    /**
     * Create data asset video data
     *
     * @param createReqVO data asset video data info
     * @return data asset video data ID
     */
    Long createDaAssetVideo(DaAssetVideoSaveReqVO createReqVO);

    /**
     * Update data asset video data
     *
     * @param updateReqVO data asset video data info
     */
    int updateDaAssetVideo(DaAssetVideoSaveReqVO updateReqVO);

    /**
     * Delete data asset video data
     *
     * @param idList data asset video data ID list
     */
    int removeDaAssetVideo(Collection<Long> idList);

    /**
     * Get data asset video data details
     *
     * @param id data asset video data ID
     * @return data asset video data
     */
    DaAssetVideoDO getDaAssetVideoById(Long id);

    /**
     * Get all data asset video data list
     *
     * @return data asset video data list
     */
    List<DaAssetVideoDO> getDaAssetVideoList();

    /**
     * Get all data asset video data Map
     *
     * @return data asset video data Map
     */
    Map<Long, DaAssetVideoDO> getDaAssetVideoMap();


    /**
     * Import data asset video data
     *
     * @param importExcelList data asset video data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetVideo(List<DaAssetVideoRespVO> importExcelList, boolean isUpdateSupport, String operName);

    void queryServiceForwarding(HttpServletResponse response, DaAssetVideoReqVO daAssetVideoReqVO);
}
