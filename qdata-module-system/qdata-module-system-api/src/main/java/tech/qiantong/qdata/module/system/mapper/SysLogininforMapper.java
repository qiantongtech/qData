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

import tech.qiantong.qdata.module.system.domain.SysLogininfor;

import java.util.List;

/**
 * System access log information data layer
 *
 * @author qdata
 */
public interface SysLogininforMapper
{
    /**
     * Insert system login log
     *
     * @param logininfor access log object
     */
    public void insertLogininfor(SysLogininfor logininfor);

    /**
     * Query system login log collection
     *
     * @param logininfor access log object
     * @return login record collection
     */
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    /**
     * Batch delete system login logs
     *
     * @param infoIds login log IDs to delete
     * @return result
     */
    public int deleteLogininforByIds(Long[] infoIds);

    /**
     * Clear system login logs
     *
     * @return result
     */
    public int cleanLogininfor();
}
