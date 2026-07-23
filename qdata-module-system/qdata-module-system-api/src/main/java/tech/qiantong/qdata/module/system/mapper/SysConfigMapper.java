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

package tech.qiantong.qdata.module.system.mapper;

import tech.qiantong.qdata.module.system.domain.SysConfig;

import java.util.List;

/**
 * Parameter Configuration Data Layer
 *
 * @author qdata
 */
public interface SysConfigMapper
{
    /**
     * Query parameter configuration information
     *
     * @param config parameter configuration information
     * @return parameter configuration information
     */
    public SysConfig selectConfig(SysConfig config);

    /**
     * Query configuration by ID
     *
     * @param configId parameter ID
     * @return parameter configuration information
     */
    public SysConfig selectConfigById(Long configId);

    /**
     * Query parameter configuration list
     *
     * @param config parameter configuration information
     * @return parameter configuration collection
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * Query parameter configuration by key name
     *
     * @param configKey parameter key name
     * @return parameter configuration information
     */
    public SysConfig checkConfigKeyUnique(String configKey);

    /**
     * Insert parameter configuration
     *
     * @param config parameter configuration information
     * @return result
     */
    public int insertConfig(SysConfig config);

    /**
     * Update parameter configuration
     *
     * @param config parameter configuration information
     * @return result
     */
    public int updateConfig(SysConfig config);

    /**
     * Delete parameter configuration
     *
     * @param configId parameter ID
     * @return result
     */
    public int deleteConfigById(Long configId);

    /**
     * Batch delete parameter information
     *
     * @param configIds parameter IDs to delete
     * @return result
     */
    public int deleteConfigByIds(Long[] configIds);
}
