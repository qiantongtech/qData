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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.common.core.domain;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <P>
 * Purpose: number structure data
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-26 10:23
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TreeData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Node ID
     */
    private Long id;

    /**
     * Parent node ID (nullable)
     */
    private Long parentId;

    /**
     * Node name
     */
    private String name;

    /**
     * Node type
     */
    private String type;

    /**
     * Other extended data
     */
    private JSONObject otherData;

    /**
     * Child node
     */
    private List<TreeData> children;
}
