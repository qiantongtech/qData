package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据集成任务-日志 创建/修改 Request VO DPP_ETL_TASK_LOG
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成任务-日志 Response VO")
@Data
public class DppEtlTaskLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "任务类型", example = "")
    @Size(max = 256, message = "任务类型长度不能超过256个字符")
    private String type;

    @Schema(description = "任务名称", example = "")
    @Size(max = 256, message = "任务名称长度不能超过256个字符")
    private String name;

    @Schema(description = "任务编码", example = "")
    @Size(max = 256, message = "任务编码长度不能超过256个字符")
    private String code;

    @Schema(description = "任务版本", example = "")
    private Integer version;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    @Size(max = 256, message = "项目编码长度不能超过256个字符")
    private String projectCode;

    @Schema(description = "责任人", example = "")
    @Size(max = 256, message = "责任人长度不能超过256个字符")
    private String personCharge;

    @Schema(description = "节点坐标信息", example = "")
    private String locations;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "超时时间", example = "")
    private Long timeout;

    @Schema(description = "抽取量", example = "")
    private Long extractionCount;

    @Schema(description = "写入量", example = "")
    private Long writeCount;

    @Schema(description = "任务状态", example = "")
    @Size(max = 256, message = "任务状态长度不能超过256个字符")
    private String status;

    @Schema(description = "执行策略", example = "")
    private String executionType;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
