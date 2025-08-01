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
