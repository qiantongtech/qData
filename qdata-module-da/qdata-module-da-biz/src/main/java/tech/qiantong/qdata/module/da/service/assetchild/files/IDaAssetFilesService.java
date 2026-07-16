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

package tech.qiantong.qdata.module.da.service.assetchild.files;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.files.DaAssetFilesDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Asset - File Service Interface
 *
 * @author qdata
 * @date 2025-06-26
 */
public interface IDaAssetFilesService extends IService<DaAssetFilesDO> {

    /**
     * Get data asset file service page list
     *
     * @param pageReqVO page request
     * @return data asset file service page list
     */
    PageResult<DaAssetFilesDO> getDaAssetFilesPage(DaAssetFilesPageReqVO pageReqVO);

    /**
     * Create data asset file service
     *
     * @param createReqVO data asset file service info
     * @return data asset file service ID
     */
    Long createDaAssetFiles(DaAssetFilesSaveReqVO createReqVO);

    /**
     * Update data asset file service
     *
     * @param updateReqVO data asset file service info
     */
    int updateDaAssetFiles(DaAssetFilesSaveReqVO updateReqVO);

    /**
     * Delete data asset file service
     *
     * @param idList data asset file service ID list
     */
    int removeDaAssetFiles(Collection<Long> idList);

    /**
     * Get data asset file service details
     *
     * @param id data asset file service ID
     * @return data asset file service
     */
    DaAssetFilesDO getDaAssetFilesById(Long id);

    /**
     * Get all data asset file service list
     *
     * @return data asset file service list
     */
    List<DaAssetFilesDO> getDaAssetFilesList();

    /**
     * Get all data asset file service Map
     *
     * @return data asset file service Map
     */
    Map<Long, DaAssetFilesDO> getDaAssetFilesMap();


    /**
     * Import data asset file service data
     *
     * @param importExcelList data asset file service data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaAssetFiles(List<DaAssetFilesRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
