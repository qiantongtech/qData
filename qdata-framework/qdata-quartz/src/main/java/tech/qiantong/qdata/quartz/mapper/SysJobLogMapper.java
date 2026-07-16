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

package tech.qiantong.qdata.quartz.mapper;

import tech.qiantong.qdata.quartz.domain.SysJobLog;

import java.util.List;

/**
 * Scheduling task log information data layer
 *
 * @author qdata
 */
public interface SysJobLogMapper
{
    /**
     * Get the scheduled task of the quartz scheduler log
     *
     * @param jobLog scheduling log information
     * @return Scheduling task log collection
     */
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog);

    /**
     * Query all scheduled task logs
     *
     * @return Scheduling task log list
     */
    public List<SysJobLog> selectJobLogAll();

    /**
     * Query scheduling information by scheduling task log ID
     *
     * @param jobLogId Scheduling task log ID
     * @return Scheduling task log object information
     */
    public SysJobLog selectJobLogById(Long jobLogId);

    /**
     * Added task log
     *
     * @param jobLog scheduling log information
     * @return result
     */
    public int insertJobLog(SysJobLog jobLog);

    /**
     * Delete scheduling log information in batches
     *
     * @param logIds Data ID to be deleted
     * @return result
     */
    public int deleteJobLogByIds(Long[] logIds);

    /**
     * Delete task log
     *
     * @param jobId scheduling log ID
     * @return result
     */
    public int deleteJobLogById(Long jobId);

    /**
     * Clear task log
     */
    public void cleanJobLog();
}
