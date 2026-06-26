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

package tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qdata.common.annotation.Excel;
import java.util.Date;
import java.io.Serializable;

/**
 * 脱敏清单关联关系 Response VO 对象 DG_DESENSITIZE_ASSETCOLUMN
 *
 * @author qdata
 * @date 2026-04-12
 */
@Schema(description = "脱敏清单关联关系 Response VO")
@Data
public class DgDesensitizeAssetcolumnRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "资产ID")
    @Schema(description = "资产ID", example = "")
    private Long assetId;

    @Excel(name = "资产名称")
    @Schema(description = "资产名称", example = "")
    private String assetName;

    @Excel(name = "资产描述")
    @Schema(description = "资产描述", example = "")
    private String assetDescription;

    private String assetTableName;
    private String assetTableComment;


    @Excel(name = "资产字段ID")
    @Schema(description = "资产字段ID", example = "")
    private Long assetcolumnId;

    @Excel(name = "资产字段名称")
    @Schema(description = "资产字段名称", example = "")
    private String assetcolumnName;
    @Excel(name = "资产字段描述")
    @Schema(description = "资产字段描述", example = "")
    private String assetcolumnComment;

    @Excel(name = "数据分类ID")
    @Schema(description = "数据分类ID", example = "")
    private Long dataCategoryId;

    @Excel(name = "数据分类名称")
    @Schema(description = "数据分类名称", example = "")
    private String dataCategoryName;
    private String dataLevelName;
    private String desensitizeRuleName;

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
