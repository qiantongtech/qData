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

package tech.qiantong.qdata.module.da.dal.dataobject.asset;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;

import java.util.List;

/**
 * 数据资产 DO 对象 DA_ASSET
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
@TableName(value = "DA_ASSET")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 资产名称 */
    private String name;

    @Schema(description = "资产类型", example = "")
    private String type;

    /** 类目编码 */
    private String catCode;

    @TableField(exist = false)
    private String catName;

    /**
     * 表类型;1:明细表 2:汇总表 3:维度表 4:应用表
     */
    @Schema(name = "表类型 1:明细表 2:汇总表 3:维度表 4:应用表")
    private String tableType;
    /**
     * 数仓分层id
     */
    @Schema(name = "数仓分层id ")
    private Long dataLayerId;
    /**
     * 业务分类id;只有表类型为非应用表是才有值
     */
    @Schema(name = "业务分类id 只有表类型为非应用表是才有值")
    private Long businessCategoryId;
    /**
     * 业务分类层级编码
     */
    @Schema(name = "业务分类层级编码 ")
    private String businessCategoryCode;
    /**
     * 数据分域id;只有表类型为非应用表是才有值
     */
    @Schema(name = "数据分域id 只有表类型为非应用表是才有值")
    private Long dataDomainId;
    /**
     * 所属主题id（主题规划）;只有表类型为应用表是才有值
     */
    @Schema(name = "所属主题id（主题规划） 只有表类型为应用表是才有值")
    private Long themeDomainId;
    /**
     * 所属主题层级编码
     */
    @Schema(name = "所属主题层级编码 ")
    private String themeDomainCode;
    /**
     * 表名大小写;1：大写 2：小写
     */
    @Schema(description = "表名大小写 1：大写 2：小写")
    private String tableCase;

    /**
     * 元数据表id
     */
    @Schema(description = "元数据表id")
    private Long tableId;

    /**是申请过来的资产还是项目自己生成的资产0：申请，1：自创 */
    @TableField(exist = false)
    private String sourceType;

    @TableField(exist = false)
    private List<DaAssetThemeRelRespVO> daAssetThemeRelList;

    /** 数据连接id */
    private Long datasourceId;

    @TableField(exist = false)
    private String datasourceName;

    @TableField(exist = false)
    private String datasourceIp;

    @TableField(exist = false)
    private String datasourceType;

    /** 表名称 */
    private String tableName;

    /** 表描述 */
    private String tableComment;

    /** 数据量 */
    private Long dataCount;

    /** 字段量 */
    private Long fieldCount;

    /** 来源;1:数据发现；2:数据模型； */
    private String source;

    /** 状态 */
    private String status;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    //api
    @TableField(exist = false)
    private DaAssetApiRespVO daAssetApi;
//    //api
//    @TableField(exist = false)
//    private List<DaAssetApiParamRespVO> daAssetApiParamList;
//
//    /**
//     * 矢量
//     */
//    @TableField(exist = false)
//    private DaAssetGeoRespVO daAssetGeo;
//
//    /**
//     * 地理空间服务
//     */
//    @TableField(exist = false)
//    private DaAssetGisRespVO daAssetGis;
//
//    /**
//     * 视频数据
//     */
//    @TableField(exist = false)
//    private DaAssetVideoRespVO daAssetVideo;

    @Schema(description = "项目id", example = "")
    @TableField(exist = false)
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    @TableField(exist = false)
    private String projectCode;
    /** 创建类型 */
    private String createType;

    @TableField(exist = false)
    private String tags;

    @Schema(name = "数仓分层名称")
    @TableField(exist = false)
    private String dataLayerName;

    @Schema(name = "数仓分层英文缩写")
    @TableField(exist = false)
    private String dataLayerEngName;

    @Schema(name = "业务名称")
    @TableField(exist = false)
    private String businessCategoryName;

    @Schema(name = "业务英文缩写")
    @TableField(exist = false)
    private String businessCategoryEngName;

    @Schema(name = "数据分域名称")
    @TableField(exist = false)
    private String dataDomainName;

    @Schema(name = "数据分域英文缩写")
    @TableField(exist = false)
    private String dataDomainEngName;

    @Schema(name = "所属主题名称")
    @TableField(exist = false)
    private String themeDomainName;

    @Schema(name = "所属主题英文缩写")
    @TableField(exist = false)
    private String themeDomainEngName;

    @Schema(name = "创建人联系电话")
    @TableField(exist = false)
    private String createUserPhoneNumber;


    @Schema(name = "修改人联系电话")
    @TableField(exist = false)
    private String updateUserPhoneNumber;
}
