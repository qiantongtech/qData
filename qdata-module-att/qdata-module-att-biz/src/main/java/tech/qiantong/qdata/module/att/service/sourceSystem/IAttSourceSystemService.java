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

package tech.qiantong.qdata.module.att.service.sourceSystem;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemRespVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem.AttSourceSystemDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Source System Service Interface
 *
 * @author qdata
 * @date 2026-04-03
 */
public interface IAttSourceSystemService extends IService<AttSourceSystemDO> {

    /**
     * Get Source System paginated list
     *
     * @param pageReqVO Page request
     * @return Source System paginated list
     */
    PageResult<AttSourceSystemDO> getAttSourceSystemPage(AttSourceSystemPageReqVO pageReqVO);

    /**
     * Create Source System
     *
     * @param createReqVO Source System info
     * @return Source System ID
     */
    Long createAttSourceSystem(AttSourceSystemSaveReqVO createReqVO);

    /**
     * Update Source System
     *
     * @param updateReqVO Source System info
     */
    int updateAttSourceSystem(AttSourceSystemSaveReqVO updateReqVO);

    /**
     * Delete Source System
     *
     * @param idList Source System ID list
     */
    int removeAttSourceSystem(Collection<Long> idList);

    /**
     * Get Source System details
     *
     * @param id Source System ID
     * @return Source System
     */
    AttSourceSystemDO getAttSourceSystemById(Long id);

    /**
     * Get all Source System list
     *
     * @return Source System list
     */
    List<AttSourceSystemDO> getAttSourceSystemList();

    /**
     * Get all Source System list (with status)
     *
     * @return Source System list
     */
    public List<AttSourceSystemDO> getAttSourceSystemListByValidFlag(Boolean validFlag);

    /**
     * Get all Source System Map
     *
     * @return Source System Map
     */
    Map<Long, AttSourceSystemDO> getAttSourceSystemMap();


    /**
     * Import Source System data
     *
     * @param importExcelList Source System data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operator
     * @return Result
     */
    String importAttSourceSystem(List<AttSourceSystemRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
