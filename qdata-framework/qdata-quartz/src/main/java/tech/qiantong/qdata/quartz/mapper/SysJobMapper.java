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

import tech.qiantong.qdata.quartz.domain.SysJob;

import java.util.List;

/**
 * Scheduling task information data layer
 *
 * @author qdata
 */
public interface SysJobMapper
{
    /**
     * Query the scheduling task log collection
     *
     * @param job scheduling information
     * @return operation log collection
     */
    public List<SysJob> selectJobList(SysJob job);

    /**
     * Query all scheduled tasks
     *
     * @return Scheduled task list
     */
    public List<SysJob> selectJobAll();

    /**
     * Query scheduling task information by scheduling ID
     *
     * @param jobId scheduling ID
     * @return role object information
     */
    public SysJob selectJobById(Long jobId);

    /**
     * Delete scheduled task information by scheduling ID
     *
     * @param jobId scheduling ID
     * @return result
     */
    public int deleteJobById(Long jobId);

    /**
     * Delete scheduled task information in batches
     *
     * @param ids data ID to be deleted
     * @return result
     */
    public int deleteJobByIds(Long[] ids);

    /**
     * Modify scheduled task information
     *
     * @param job scheduling task information
     * @return result
     */
    public int updateJob(SysJob job);

    /**
     * Add scheduling task information
     *
     * @param job scheduling task information
     * @return result
     */
    public int insertJob(SysJob job);
}
