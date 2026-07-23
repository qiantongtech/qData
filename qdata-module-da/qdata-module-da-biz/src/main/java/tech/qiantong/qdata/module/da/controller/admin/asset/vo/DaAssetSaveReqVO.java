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

package tech.qiantong.qdata.module.da.controller.admin.asset.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.database.core.FileInfo;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoSaveReqVO;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Data Asset Save/Create/Modify Request VO DA_ASSET
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据资产 Response VO")
@Data
public class DaAssetSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产名称", example = "")
    @Size(max = 256, message = "资产名称长度不能超过256个字符")
    private String name;

    @Schema(description = "资产类型", example = "")
    private String type;

    @Schema(description = "类目编码", example = "")
    @Size(max = 256, message = "类目编码长度不能超过256个字符")
    private String catCode;

    /**
     * Table type; 1: Detail table 2: Summary table 3: Dimension table 4: Application table
     */
    @Schema(description = "表类型 1:明细表 2:汇总表 3:维度表 4:应用表")
    private String tableType;
    /**
     * Data warehouse layer ID
     */
    @Schema(description = "数仓分层id ")
    private Long dataLayerId;
    /**
     * Business category ID; only has value when table type is not application table
     */
    @Schema(description = "业务分类id 只有表类型为非应用表是才有值")
    private Long businessCategoryId;
    /**
     * Business category hierarchy code
     */
    @Schema(description = "业务分类层级编码 ")
    private String businessCategoryCode;
    /**
     * Data domain ID; only has value when table type is not application table
     */
    @Schema(description = "数据分域id 只有表类型为非应用表是才有值")
    private Long dataDomainId;
    /**
     * Theme domain ID (theme planning); only has value when table type is application table
     */
    @Schema(description = "所属主题id（主题规划） 只有表类型为应用表是才有值")
    private Long themeDomainId;
    /**
     * Theme domain hierarchy code
     */
    @Schema(description = "所属主题层级编码 ")
    private String themeDomainCode;
    /**
     * Table name casing; 1: Uppercase 2: Lowercase
     */
    @Schema(description = "表名大小写 1：大写 2：小写")
    private String tableCase;

    /**
     * Metadata table ID
     */
    @Schema(description = "元数据表id")
    private Long tableId;

    @Schema(description = "是申请过来的资产还是项目自己生成的资产0：申请，1：自创", example = "")
    private String sourceType;

    private List<String> themeIdList;

    @Schema(description = "数据连接id", example = "")
    @Size(max = 256, message = "数据连接id长度不能超过256个字符")
    private String datasourceId;

    @Schema(description = "表名称", example = "")
    @Size(max = 256, message = "表名称长度不能超过256个字符")
    private String tableName;

    @Schema(description = "表描述", example = "")
    @Size(max = 256, message = "表描述长度不能超过256个字符")
    private String tableComment;

    @Schema(description = "数据量", example = "")
    private Long dataCount;

    @Schema(description = "字段量", example = "")
    private Long fieldCount;

    /** Source; 1: Data Discovery; 2: Data Model; */
    @Schema(description = "来源", example = "")
    private String source;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    // API
    @TableField(exist = false)
    private DaAssetApiSaveReqVO daAssetApi;
    // API
    @TableField(exist = false)
    private List<DaAssetApiParamSaveReqVO> daAssetApiParamList;

    /**
     * Vector
     */
    @TableField(exist = false)
    private DaAssetGeoSaveReqVO daAssetGeo;

    /**
     * Geographic spatial service
     */
    @TableField(exist = false)
    private DaAssetGisSaveReqVO daAssetGis;

    /**
     * Video data
     */
    @TableField(exist = false)
    private DaAssetVideoSaveReqVO daAssetVideo;

    private FileInfo fileInfo;

    /**
     * File data
     */
    @TableField(exist = false)
    private DaAssetFilesSaveReqVO daAssetFiles;


    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "创建类型", example = "")
    private String createType;
}
