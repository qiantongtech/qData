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

package tech.qiantong.qdata.module.dp.controller.admin.document.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * <P>
 * 用途:标准检索响应VO
 * </p>
 *
 * @author: FXB
 * @create: 2025-08-22 10:08
 **/
@Data
public class DpDocumentSearchReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "检索内容", example = "")
    private String search;
}
