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

package tech.qiantong.qdata.module.da.service.datasource;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceProjectRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * Datasource - Project Relation Service Interface
 *
 * @author qdata
 * @date 2025-03-13
 */
public interface IDaDatasourceProjectRelService extends IService<DaDatasourceProjectRelDO> {

    /**
     * Get datasource-project relation page list
     *
     * @param pageReqVO page request
     * @return datasource-project relation page list
     */
    PageResult<DaDatasourceProjectRelDO> getDaDatasourceProjectRelPage(DaDatasourceProjectRelPageReqVO pageReqVO);

    /**
     * Create datasource-project relation
     *
     * @param createReqVO datasource-project relation info
     * @return datasource-project relation ID
     */
    Long createDaDatasourceProjectRel(DaDatasourceProjectRelSaveReqVO createReqVO);

    /**
     * Update datasource-project relation
     *
     * @param updateReqVO datasource-project relation info
     */
    int updateDaDatasourceProjectRel(DaDatasourceProjectRelSaveReqVO updateReqVO);

    /**
     * Delete datasource-project relation
     *
     * @param idList datasource-project relation ID list
     */
    int removeDaDatasourceProjectRel(Collection<Long> idList);

    /**
     * Get datasource-project relation details
     *
     * @param id datasource-project relation ID
     * @return datasource-project relation
     */
    DaDatasourceProjectRelDO getDaDatasourceProjectRelById(Long id);

    /**
     * Get all datasource-project relation list
     *
     * @return datasource-project relation list
     */
    List<DaDatasourceProjectRelDO> getDaDatasourceProjectRelList();

    /**
     * Get all datasource-project relation list
     *
     * @return datasource-project relation list
     */
    List<DaDatasourceProjectRelDO> getDaDatasourceProjectRelList(DaDatasourceProjectRelDO daDatasourceProjectRelDO);

    /**
     * Get all datasource-project relation list joined with datasource table and project table
     *
     * @return datasource-project relation list
     */
    List<DaDatasourceProjectRelDO> getJoinProjectAndDatasource(DaDatasourceProjectRelDO daDatasourceProjectRelDO);

    /**
     * Get all datasource-project relation Map
     *
     * @return datasource-project relation Map
     */
    Map<Long, DaDatasourceProjectRelDO> getDaDatasourceProjectRelMap();


    /**
     * Import datasource-project relation data
     *
     * @param importExcelList datasource-project relation data list
     * @param isUpdateSupport whether to support update; if exists, update the data
     * @param operName operator user
     * @return result
     */
    String importDaDatasourceProjectRel(List<DaDatasourceProjectRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
