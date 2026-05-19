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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.da.controller.admin.asset.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.database.core.FileInfo;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoRespVO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 数据资产 Response VO 对象 DA_ASSET
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据资产 Response VO")
@Data
public class DaAssetRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "资产名称")
    @Schema(description = "资产名称", example = "")
    private String name;

    @Excel(name = "资产类型")
    @Schema(description = "资产类型", example = "")
    private String type;

    @Excel(name = "类目编码")
    @Schema(description = "类目编码", example = "")
    private String catCode;

    private String catName;

    /**
     * 表类型;1:明细表 2:汇总表 3:维度表 4:应用表
     */
    @Schema(description = "表类型 1:明细表 2:汇总表 3:维度表 4:应用表")
    private String tableType;
    /**
     * 数仓分层id
     */
    @Schema(description = "数仓分层id ")
    private Long dataLayerId;
    /**
     * 业务分类id;只有表类型为非应用表是才有值
     */
    @Schema(description = "业务分类id 只有表类型为非应用表是才有值")
    private Long businessCategoryId;
    /**
     * 业务分类层级编码
     */
    @Schema(description = "业务分类层级编码 ")
    private String businessCategoryCode;
    /**
     * 数据分域id;只有表类型为非应用表是才有值
     */
    @Schema(description = "数据分域id 只有表类型为非应用表是才有值")
    private Long dataDomainId;
    /**
     * 所属主题id（主题规划）;只有表类型为应用表是才有值
     */
    @Schema(description = "所属主题id（主题规划） 只有表类型为应用表是才有值")
    private Long themeDomainId;
    /**
     * 所属主题层级编码
     */
    @Schema(description = "所属主题层级编码 ")
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

    @TableField(exist = false)
    private List<DaAssetThemeRelRespVO> daAssetThemeRelList;

    @Excel(name = "是申请过来的资产还是项目自己生成的资产0：申请，1：自创")
    @Schema(description = "是申请过来的资产还是项目自己生成的资产0：申请，1：自创", example = "")
    private String sourceType;


    @Excel(name = "数据连接id")
    @Schema(description = "数据连接id", example = "")
    private Long datasourceId;

    @TableField(exist = false)
    private String datasourceName;

    @TableField(exist = false)
    private String datasourceIp;

    @TableField(exist = false)
    private String datasourceType;

    @Excel(name = "表名称")
    @Schema(description = "表名称", example = "")
    private String tableName;

    @Excel(name = "表描述")
    @Schema(description = "表描述", example = "")
    private String tableComment;

    @Excel(name = "数据量")
    @Schema(description = "数据量", example = "")
    private Long dataCount;

    @Excel(name = "字段量")
    @Schema(description = "字段量", example = "")
    private Long fieldCount;

    /** 来源;1:数据发现；2:数据模型； */
    @Schema(description = "来源", example = "")
    private String source;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private String status;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    //api
    @TableField(exist = false)
    private DaAssetApiRespVO daAssetApi;
    //api
    @TableField(exist = false)
    private List<DaAssetApiParamRespVO> daAssetApiParamList;

    /**
     * 矢量
     */
    @TableField(exist = false)
    private DaAssetGeoRespVO daAssetGeo;

    /**
     * 地理空间服务
     */
    @TableField(exist = false)
    private DaAssetGisRespVO daAssetGis;

    /**
     * 视频数据
     */
    @TableField(exist = false)
    private DaAssetVideoRespVO daAssetVideo;

    /**
     * 文件数据
     */
    @TableField(exist = false)
    private DaAssetFilesSaveReqVO daAssetFiles;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "创建类型", example = "")
    private String createType;

    private List<String> tagIds;
    private List<String> tagNames;

    private String tags;

    private FileInfo fileInfo;


    @Schema(description = "数仓分层名称")
    private String dataLayerName;

    @Schema(description = "数仓分层英文缩写")
    private String dataLayerEngName;

    @Schema(description = "业务名称")
    private String businessCategoryName;

    @Schema(description = "业务英文缩写")
    private String businessCategoryEngName;

    @Schema(description = "数据分域名称")
    private String dataDomainName;

    @Schema(description = "数据分域英文缩写")
    private String dataDomainEngName;

    @Schema(description = "所属主题名称")
    private String themeDomainName;

    @Schema(description = "所属主题英文缩写")
    private String themeDomainEngName;

    @Schema(name = "创建人联系电话")
    private String createUserPhoneNumber;


    @Schema(name = "修改人联系电话")
    private String updateUserPhoneNumber;
}
