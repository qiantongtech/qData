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

package tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessDomainRelDO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 业务分类 Response VO 对象 DM_BUSINESS_CATEGORY
 *
 * @author qdata
 * @date 2026-04-08
 */
@Schema(description = "业务分类 Response VO")
@Data
public class DmBusinessCategoryRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "层级编码")
    @Schema(description = "层级编码", example = "")
    private String code;

    @Excel(name = "业务分类名称")
    @Schema(description = "业务分类名称", example = "")
    private String name;

    @Excel(name = "关联上级ID")
    @Schema(description = "关联上级ID", example = "")
    private Long parentId;

    @Excel(name = "关联上级名称")
    @Schema(description = "关联上级名称", example = "")
    private String parentName;

    @Excel(name = "排序")
    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Excel(name = "英文缩写名")
    @Schema(description = "英文缩写名", example = "")
    private String engName;

    @Excel(name = "负责人手机号")
    @Schema(description = "负责人手机号", example = "")
    private String ownerPhone;

    @Excel(name = "负责人ID")
    @Schema(description = "负责人ID", example = "")
    private Long ownerId;

    @Excel(name = "负责人ID")
    @Schema(description = "负责人姓名", example = "")
    private String ownerName;

    @Excel(name = "数据域ID")
    @Schema(description = "数据域ID", example = "")
    private Long domainId;

    @Schema(description = "数据域集合", example = "")
    private List<DmBusinessDomainRelDO> domainList;

    @Schema(description = "数据域ID集合", example = "")
    private List<String> domainIds;

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
