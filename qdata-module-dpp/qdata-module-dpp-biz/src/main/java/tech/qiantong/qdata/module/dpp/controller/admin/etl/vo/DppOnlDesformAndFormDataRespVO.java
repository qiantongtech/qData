package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;

/**
 * 在线单设计器 Response VO 对象 DPP_ONL_DESFORM
 *
 * @author qdata
 * @date 2025-04-09
 */
@Schema(description = "在线单设计器 Response VO")
@Data
public class DppOnlDesformAndFormDataRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "ID")
    @Schema(description = "ID", example = "")
    private Long id;

    @Excel(name = "表单名称")
    @Schema(description = "表单名称", example = "")
    private String desformName;

    @Excel(name = "表单编码")
    @Schema(description = "表单编码", example = "")
    private String desformCode;

    @Excel(name = "表单JSON")
    @Schema(description = "表单JSON", example = "")
    private String desformJson;

    @Excel(name = "表单数据")
    @Schema(description = "表单数据", example = "")
    private String desformData;

    @Excel(name = "是否存储到指定表中")
    @Schema(description = "是否存储到指定表中", example = "")
    private String saveTableFlag;

    @Excel(name = "关联数据源id")
    @Schema(description = "关联数据源id", example = "")
    private Long datasourceId;

    @Excel(name = "数据库名")
    @Schema(description = "数据库名", example = "")
    private String databaseName;

    @Excel(name = "表名称")
    @Schema(description = "表名称", example = "")
    private String tableName;

    @Excel(name = "字段")
    @Schema(description = "字段", example = "")
    private String columnName;

    @Excel(name = "主键字段")
    @Schema(description = "主键字段", example = "")
    private String pkColumnName;


}
