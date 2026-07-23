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

import tech.qiantong.qdata.module.system.domain.SysUserPost;

import java.util.List;

/**
 * User-post association table data layer
 *
 * @author qdata
 */
public interface SysUserPostMapper
{
    /**
     * Delete user-post association by user ID
     *
     * @param userId user ID
     * @return result
     */
    public int deleteUserPostByUserId(Long userId);

    /**
     * Count post usage by post ID
     *
     * @param postId post ID
     * @return result
     */
    public int countUserPostById(Long postId);

    /**
     * Batch delete user-post associations
     *
     * @param ids data IDs to delete
     * @return result
     */
    public int deleteUserPost(Long[] ids);

    /**
     * Batch insert user-post information
     *
     * @param userPostList user-post list
     * @return result
     */
    public int batchUserPost(List<SysUserPost> userPostList);
}
