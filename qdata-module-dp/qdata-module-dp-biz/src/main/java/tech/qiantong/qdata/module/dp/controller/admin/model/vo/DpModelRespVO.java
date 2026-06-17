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

package tech.qiantong.qdata.module.dp.controller.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 逻辑模型 Response VO 对象 DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "逻辑模型 Response VO")
@Data
public class DpModelRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "ID")
    @Schema(description = "ID", example = "")
    private Long id;

    @Excel(name = "模型编码")
    @Schema(description = "模型编码", example = "")
    private String modelName;

    @Excel(name = "模型名称")
    @Schema(description = "模型名称", example = "")
    private String modelComment;

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

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private String status;

    @Excel(name = "创建方式")
    @Schema(description = "创建方式", example = "")
    private String createType;

    @Excel(name = "数据源id")
    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    private Long documentId;

    @Excel(name = "联系人")
    @Schema(description = "联系人", example = "")
    private String contact;

    @Excel(name = "联系电话")
    @Schema(description = "联系电话", example = "")
    private String contactNumber;

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

    @Excel(name = "数据源名称")
    @Schema(description = "数据源名称", example = "")
    private String datasourceName;

    @Excel(name = "数据源类型")
    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Excel(name = "数据源配置(json字符串)")
    @Schema(description = "数据源配置(json字符串)", example = "")
    private String datasourceConfig;

    @Excel(name = "IP")
    @Schema(description = "IP", example = "")
    private String ip;

    @Excel(name = "端口号")
    @Schema(description = "端口号", example = "")
    private Long port;

    /**
     * 名称
     */
    private String documentName;

    /**
     * 名称
     */
    private String documentCode;

    /**
     * 文件标准类型字段，
     */
    private String documentType;

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

    @Schema(description = "发布状态 1:未发布 3:发布成功 4:发布失败")
    private String releaseStatus;

    @Schema(description = "发布发布数据源列表")
    private String releaseDatabaseList;

    @Schema(name = "创建人联系电话")
    private String createUserPhoneNumber;


    @Schema(name = "修改人联系电话")
    private String updateUserPhoneNumber;
}
