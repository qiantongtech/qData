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

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * 脱敏白名单 Response VO 对象 DG_DESENSITIZE_WHITELIST
 *
 * @author qdata
 * @date 2026-04-09
 */
@Schema(description = "脱敏白名单 Response VO")
@Data
public class DgDesensitizeWhitelistRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "白名单名称")
    @Schema(description = "白名单名称", example = "")
    private String name;

    @Excel(name = "数据分类")
    @Schema(description = "数据分类", example = "")
    private Long dataCategoryId;

    @Excel(name = "数据分类名称")
    @Schema(description = "数据分类名称", example = "")
    private String dataCategoryName;

    @Excel(name = "生效分类;1：用户 2：角色 3：部门")
    @Schema(description = "生效分类;1：用户 2：角色 3：部门", example = "")
    private String effectiveCategory;

    @Schema(description = "分类详情;1：用户 2：角色 3：部门", example = "")
    private List<DgDesensitizeUserRelDO> userList;

    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "开始时间", example = "")
    private Date startTime;

    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "结束时间", example = "")
    private Date endTime;

    @Excel(name = "排序")
    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Excel(name = "是否有效;0：无效，1：有效")
    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志;1：已删除，0：未删除")
    @Schema(description = "删除标志;1：已删除，0：未删除", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
