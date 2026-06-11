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

package tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 脱敏白名单与用户关联关系 Request VO 对象 DG_DESENSITIZE_USER_REL
 *
 * @author qdata
 * @date 2026-04-09
 */
@Schema(description = "脱敏白名单与用户关联关系 Request VO")
@Data
public class DgDesensitizeUserRelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "脱敏白名单ID", example = "")
    private Long desensitizeId;

    @Schema(description = "用户ID", example = "")
    private Long userId;

    @Schema(description = "白名单名称", example = "")
    private String desensitizeName;

    @Schema(description = "用户名称", example = "")
    private String userName;



    @Schema(description = "生效分类;1：用户 2：角色 3：部门", example = "")
    private String effectiveCategory;


}
