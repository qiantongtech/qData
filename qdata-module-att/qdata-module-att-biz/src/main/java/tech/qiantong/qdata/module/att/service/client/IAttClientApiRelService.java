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

package tech.qiantong.qdata.module.att.service.client;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientApiRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * App API Service Association Service Interface
 *
 * @author FXB
 * @date 2025-08-21
 */
public interface IAttClientApiRelService extends IService<AttClientApiRelDO> {

    /**
     * Get App API Service Association paginated list
     *
     * @param pageReqVO Page request
     * @return App API Service Association paginated list
     */
    PageResult<AttClientApiRelDO> getAttClientApiRelPage(AttClientApiRelPageReqVO pageReqVO);

    /**
     * Create App API Service Association
     *
     * @param createReqVO App API Service Association info
     * @return App API Service Association ID
     */
    Long createAttClientApiRel(AttClientApiRelSaveReqVO createReqVO);

    /**
     * Update App API Service Association
     *
     * @param updateReqVO App API Service Association info
     */
    int updateAttClientApiRel(AttClientApiRelSaveReqVO updateReqVO);

    /**
     * Delete App API Service Association
     *
     * @param idList App API Service Association ID list
     */
    int removeAttClientApiRel(Collection<Long> idList);

    /**
     * Get App API Service Association details
     *
     * @param id App API Service Association ID
     * @return App API Service Association
     */
    AttClientApiRelDO getAttClientApiRelById(Long id);

    /**
     * Get all App API Service Association list
     *
     * @return App API Service Association list
     */
    List<AttClientApiRelDO> getAttClientApiRelList();

    /**
     * Get all App API Service Association Map
     *
     * @return App API Service Association Map
     */
    Map<Long, AttClientApiRelDO> getAttClientApiRelMap();


    /**
     * Import App API Service Association data
     *
     * @param importExcelList App API Service Association data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operator name
     * @return Result
     */
    String importAttClientApiRel(List<AttClientApiRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
