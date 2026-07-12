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

import tech.qiantong.qdata.module.system.domain.SysOperLog;

import java.util.List;

/**
 * Operation log data layer
 *
 * @author qdata
 */
public interface SysOperLogMapper
{
    /**
     * Insert operation log
     *
     * @param operLog operation log object
     */
    public void insertOperlog(SysOperLog operLog);

    /**
     * Query system operation log collection
     *
     * @param operLog operation log object
     * @return operation log collection
     */
    public List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * Batch delete system operation logs
     *
     * @param operIds operation log IDs to delete
     * @return result
     */
    public int deleteOperLogByIds(Long[] operIds);

    /**
     * Query operation log detail
     *
     * @param operId operation ID
     * @return operation log object
     */
    public SysOperLog selectOperLogById(Long operId);

    /**
     * Clear operation logs
     */
    public void cleanOperLog();
}
