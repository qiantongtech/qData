package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 在线单设计器 创建/修改 Request VO DPP_ONL_DESFORM
 *
 * @author qdata
 * @date 2025-04-09
 */
@Schema(description = "在线单设计器 Response VO")
@Data
public class DppOnlDesformSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "表单名称", example = "")
    @Size(max = 256, message = "表单名称长度不能超过256个字符")
    private String desformName;

    @Schema(description = "表单编码", example = "")
    @Size(max = 256, message = "表单编码长度不能超过256个字符")
    private String desformCode;

    @Schema(description = "表单JSON", example = "")
//    @Size(max = 256, message = "表单JSON长度不能超过256个字符")
    private String desformJson;

    @Schema(description = "是否存储到指定表中", example = "")
    @Size(max = 256, message = "是否存储到指定表中长度不能超过256个字符")
    private String saveTableFlag;

    @Schema(description = "关联数据源id", example = "")
    private Long datasourceId;

    @Schema(description = "数据库名", example = "")
    @Size(max = 256, message = "数据库名长度不能超过256个字符")
    private String databaseName;

    @Schema(description = "表名称", example = "")
    @Size(max = 256, message = "表名称长度不能超过256个字符")
    private String tableName;

    @Schema(description = "字段", example = "")
    @Size(max = 256, message = "字段长度不能超过256个字符")
    private String columnName;

    @Schema(description = "主键字段", example = "")
    @Size(max = 256, message = "主键字段长度不能超过256个字符")
    private String pkColumnName;

    @Schema(description = "是否程序生成主键的值", example = "")
    @Size(max = 256, message = "是否程序生成主键的值长度不能超过256个字符")
    private String createPkDataFlag;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
