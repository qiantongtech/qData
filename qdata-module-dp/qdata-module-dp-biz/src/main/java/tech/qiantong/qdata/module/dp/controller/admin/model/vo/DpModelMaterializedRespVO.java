package tech.qiantong.qdata.module.dp.controller.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 物化模型记录 Response VO 对象 DP_MODEL_MATERIALIZED
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "物化模型记录 Response VO")
@Data
public class DpModelMaterializedRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "模型编码")
    @Schema(description = "模型编码", example = "")
    private String modelName;

    @Excel(name = "模型名称")
    @Schema(description = "模型名称", example = "")
    private String modelAlias;

    @Excel(name = "模型表id")
    @Schema(description = "模型表id", example = "")
    private Long modelId;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private String status;

    @Excel(name = "执行日志信息")
    @Schema(description = "执行日志信息", example = "")
    private String message;

    @Excel(name = "执行sql备份")
    @Schema(description = "执行sql备份", example = "")
    private String sqlCommand;

    @Excel(name = "数据源id")
    @Schema(description = "数据源id", example = "")
    private String datasourceId;

    @Excel(name = "数据源类型")
    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Excel(name = "数据源名称")
    @Schema(description = "数据源名称", example = "")
    private String datasourceName;

    @Excel(name = "资产表id")
    @Schema(description = "资产表id", example = "")
    private String assetId;

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

}
