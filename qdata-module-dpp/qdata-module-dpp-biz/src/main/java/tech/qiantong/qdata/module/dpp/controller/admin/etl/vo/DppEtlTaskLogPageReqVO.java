package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据集成任务-日志 Request VO 对象 DPP_ETL_TASK_LOG
 *
 * @author qdata
 * @date 2025-02-13
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "数据集成任务-日志 Request VO")
@Data
public class DppEtlTaskLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "任务类型", example = "")
    private String type;

    @Schema(description = "任务名称", example = "")
    private String name;

    @Schema(description = "任务编码", example = "")
    private String code;

    @Schema(description = "任务版本", example = "")
    private Integer version;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "责任人", example = "")
    private String personCharge;

    @Schema(description = "节点坐标信息", example = "")
    private String locations;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "超时时间", example = "")
    private Long timeout;

    @Schema(description = "抽取量", example = "")
    private Long extractionCount;

    @Schema(description = "写入量", example = "")
    private Long writeCount;

    @Schema(description = "任务状态", example = "")
    private String status;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;




}
