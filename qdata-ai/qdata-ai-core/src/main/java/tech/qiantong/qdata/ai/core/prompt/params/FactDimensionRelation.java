/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.ai.core.prompt.params;

import lombok.Builder;
import lombok.Data;

/**
 * <P>
 * 用途: 事实维度关联
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-07 09:49
 **/
@Builder
@Data
public class FactDimensionRelation {
    private String factTable;
    private String dimensionTable;
    private String factColumn;
    private String dimensionColumn;
    private String joinType; // INNER, LEFT, etc.
    private String description;
}
