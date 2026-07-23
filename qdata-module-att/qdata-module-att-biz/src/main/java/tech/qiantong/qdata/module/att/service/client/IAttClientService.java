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
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * App Management Service Interface
 *
 * @author qdata
 * @date 2025-02-18
 */
public interface IAttClientService extends IService<AttClientDO> {

    /**
     * Get App Management paginated list
     *
     *  pageReqVO Page request
     *  App Management paginated list
     */
    PageResult<AttClientDO> getAttClientPage(AttClientPageReqVO pageReqVO);

    /**
     * Create App Management
     *
     *  createReqVO App Management info
     *  App Management ID
     */
    Long createAttClient(AttClientSaveReqVO createReqVO);

    /**
     * Update App Management
     *
     *  updateReqVO App Management info
     */
    int updateAttClient(AttClientSaveReqVO updateReqVO);

    /**
     * Delete App Management
     *
     *  idList App Management ID list
     */
    int removeAttClient(Collection<Long> idList);

    /**
     * Get App Management details
     *
     *  id App Management ID
     *  App Management
     */
    AttClientDO getAttClientById(Long id);

    /**
     * Get all App Management list
     *
     *  App Management list
     */
    List<AttClientDO> getAttClientList();

    /**
     * Get all App Management Map
     *
     * @return App Management Map
     */
    Map<Long, AttClientDO> getAttClientMap();


    /**
     * Import App Management data
     *
     * @param importExcelList App Management data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importAttClient(List<AttClientRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
