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

package tech.qiantong.qdata.api.ds.api.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>
 * 用途:项目保存修改DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:20
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DsProjectUpdateReqDTO {

    /**
     * 项目编码（必填）
     */
    private Long projectCode;

    /**
     * 项目名称（必填）
     */
    private String projectName;

    /**
     * 项目描述
     */
    private String description;
}
