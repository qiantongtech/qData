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

import tech.qiantong.qdata.module.system.domain.SysConfig;

import java.util.List;

/**
 * Configuration service layer
 *
 * @author qdata
 */
public interface ISysConfigService
{
    /**
     * Query configuration information by ID
     *
     * @param configId configuration ID
     * @return configuration information
     */
    public SysConfig selectConfigById(Long configId);

    /**
     * Query configuration information by key name
     *
     * @param configKey configuration key name
     * @return configuration key value
     */
    public String selectConfigByKey(String configKey);

    /**
     * Get captcha switch status
     *
     * @return true if enabled, false if disabled
     */
    public boolean selectCaptchaEnabled();

    /**
     * Query configuration list
     *
     * @param config configuration information
     * @return configuration collection
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * Insert configuration
     *
     * @param config configuration information
     * @return result
     */
    public int insertConfig(SysConfig config);

    /**
     * Update configuration
     *
     * @param config configuration information
     * @return result
     */
    public int updateConfig(SysConfig config);

    /**
     * Batch delete configuration information
     *
     * @param configIds configuration IDs to delete
     */
    public void deleteConfigByIds(Long[] configIds);

    /**
     * Load configuration cache data
     */
    public void loadingConfigCache();

    /**
     * Clear configuration cache data
     */
    public void clearConfigCache();

    /**
     * Reset configuration cache data
     */
    public void resetConfigCache();

    /**
     * Check if configuration key name is unique
     *
     * @param config configuration information
     * @return result
     */
    public boolean checkConfigKeyUnique(SysConfig config);
}
