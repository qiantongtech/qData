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

package tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;

/**
 * 脱敏白名单 创建/修改 Request VO DG_DESENSITIZE_WHITELIST
 *
 * @author qdata
 * @date 2026-04-09
 */
@Schema(description = "脱敏白名单 Response VO")
@Data
public class DgDesensitizeWhitelistSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "白名单名称", example = "")
    @Size(max = 256, message = "白名单名称长度不能超过256个字符")
    private String name;

    @Schema(description = "数据分类", example = "")
    private Long dataCategoryId;

    @Schema(description = "生效分类;1：用户 2：角色 3：部门", example = "")
    @Size(max = 256, message = "生效分类;1：用户 2：角色 3：部门长度不能超过256个字符")
    private String effectiveCategory;

    @Schema(description = "用户集合", example = "")
    private List<DgDesensitizeUserRelDO> userList;


    @Schema(description = "开始时间", example = "")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    private Date endTime;

    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;
}
