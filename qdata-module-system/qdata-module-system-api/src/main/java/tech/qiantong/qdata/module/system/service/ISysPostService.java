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

import tech.qiantong.qdata.module.system.domain.SysPost;

import java.util.List;

/**
 * Post information service layer
 *
 * @author qdata
 */
public interface ISysPostService
{
    /**
     * Query post information collection
     *
     * @param post post information
     * @return post list
     */
    public List<SysPost> selectPostList(SysPost post);

    /**
     * Query all posts
     *
     * @return post list
     */
    public List<SysPost> selectPostAll();

    /**
     * Query post information by post ID
     *
     * @param postId post ID
     * @return post object information
     */
    public SysPost selectPostById(Long postId);

    /**
     * Get post selection list by user ID
     *
     * @param userId user ID
     * @return selected post ID list
     */
    public List<Long> selectPostListByUserId(Long userId);

    /**
     * Check post name uniqueness
     *
     * @param post post information
     * @return result
     */
    public boolean checkPostNameUnique(SysPost post);

    /**
     * Check post code uniqueness
     *
     * @param post post information
     * @return result
     */
    public boolean checkPostCodeUnique(SysPost post);

    /**
     * Query post usage count by post ID
     *
     * @param postId post ID
     * @return result
     */
    public int countUserPostById(Long postId);

    /**
     * Delete post information
     *
     * @param postId post ID
     * @return result
     */
    public int deletePostById(Long postId);

    /**
     * Batch delete post information
     *
     * @param postIds post IDs to delete
     * @return result
     */
    public int deletePostByIds(Long[] postIds);

    /**
     * Insert and save post information
     *
     * @param post post information
     * @return result
     */
    public int insertPost(SysPost post);

    /**
     * Update and save post information
     *
     * @param post post information
     * @return result
     */
    public int updatePost(SysPost post);
}
