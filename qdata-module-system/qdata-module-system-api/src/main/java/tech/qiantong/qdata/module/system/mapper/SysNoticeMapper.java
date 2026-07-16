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

import tech.qiantong.qdata.module.system.domain.SysNotice;

import java.util.List;

/**
 * Notice/Announcement data layer
 *
 * @author qdata
 */
public interface SysNoticeMapper
{
    /**
     * Query notice information by ID
     *
     * @param noticeId notice ID
     * @return notice information
     */
    public SysNotice selectNoticeById(Long noticeId);

    /**
     * Query notice list
     *
     * @param notice notice information
     * @return notice collection
     */
    public List<SysNotice> selectNoticeList(SysNotice notice);

    /**
     * Insert notice
     *
     * @param notice notice information
     * @return result
     */
    public int insertNotice(SysNotice notice);

    /**
     * Update notice
     *
     * @param notice notice information
     * @return result
     */
    public int updateNotice(SysNotice notice);

    /**
     * Delete notice by ID
     *
     * @param noticeId notice ID
     * @return result
     */
    public int deleteNoticeById(Long noticeId);

    /**
     * Batch delete notice information
     *
     * @param noticeIds notice IDs to delete
     * @return result
     */
    public int deleteNoticeByIds(Long[] noticeIds);
}
