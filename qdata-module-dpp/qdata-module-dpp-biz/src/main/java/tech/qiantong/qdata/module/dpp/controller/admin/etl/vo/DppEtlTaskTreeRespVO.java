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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.List;

@Schema(description = "数据集成任务 Response VO")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // 添加此注解，null 字段将不被序列化返回
public class DppEtlTaskTreeRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "树形名称")
    private String label;


    @Schema(description = "数量")
    private int dppEtlTaskCount;

    @Schema(description = "树形下级")
    private List<DppEtlTaskTreeRespVO> children;



    @Excel(name = "任务类型")
    @Schema(description = "任务类型", example = "")
    private String type;

    @Excel(name = "任务名称")
    @Schema(description = "任务名称", example = "")
    private String name;

    @Excel(name = "任务编码")
    @Schema(description = "任务编码", example = "")
    private String code;

    @Excel(name = "扩展任务编码")
    @Schema(description = "扩展任务编码", example = "")
    private String extCode;

    @Excel(name = "任务版本")
    @Schema(description = "任务版本", example = "")
    private Long version;

    @Excel(name = "项目id")
    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Excel(name = "项目编码")
    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Excel(name = "责任人")
    @Schema(description = "责任人", example = "")
    private String personCharge;

    @Excel(name = "联系电话")
    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Excel(name = "节点坐标信息")
    @Schema(description = "节点坐标信息", example = "")
    private String locations;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "任务的执行策略", example = "")
    private String executionType;

    @Excel(name = "任务状态")
    @Schema(description = "任务状态", example = "")
    private String status;

    @TableField(exist = false)
    private String releaseState = this.status;

    @Excel(name = "DolphinScheduler的id")
    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

    /** 任务id */
    private Long treeId;

    @Schema(description = "草稿任务配置信息", example = "")
    private String draftJson;
}
