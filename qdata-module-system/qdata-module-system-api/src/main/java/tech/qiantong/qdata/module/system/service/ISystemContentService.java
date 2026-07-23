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

package tech.qiantong.qdata.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.system.domain.SystemContentDO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentPageReqVO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentRespVO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentSaveReqVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * System configuration service interface
 *
 * @author qdata
 * @date 2024-12-31
 */
public interface ISystemContentService extends IService<SystemContentDO> {

    /**
     * Get system configuration paginated list
     *
     * @param pageReqVO pagination request
     * @return system configuration paginated list
     */
    PageResult<SystemContentDO> getSystemContentPage(SystemContentPageReqVO pageReqVO);

    /**
     * Create system configuration
     *
     * @param createReqVO system configuration information
     * @return system configuration ID
     */
    Long createSystemContent(SystemContentSaveReqVO createReqVO);

    /**
     * Update system configuration
     *
     * @param updateReqVO system configuration information
     */
    int updateSystemContent(SystemContentSaveReqVO updateReqVO);

    /**
     * Delete system configuration
     *
     * @param idList system configuration IDs
     */
    int removeSystemContent(Collection<Long> idList);

    /**
     * Get system configuration details
     *
     * @param id system configuration ID
     * @return system configuration
     */
    SystemContentDO getSystemContentById(Long id);

    /**
     * Get all system configuration list
     *
     * @return system configuration list
     */
    List<SystemContentDO> getSystemContentList();

    /**
     * Get all system configuration as Map
     *
     * @return system configuration Map
     */
    Map<Long, SystemContentDO> getSystemContentMap();


    /**
     * Import system configuration data
     *
     * @param importExcelList system configuration data list
     * @param isUpdateSupport whether to update existing data if already present
     * @param operName operator name
     * @return result
     */
    String importSystemContent(List<SystemContentRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
