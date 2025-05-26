package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据集成任务 Request VO 对象 DPP_ETL_TASK
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成任务 Request VO")
@Data
public class DppEtlTaskPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "任务类型", example = "1：离线任务 2：实时任务 3：数据开发任务 4：作业任务")
    private String type;

    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Schema(description = "任务名称", example = "")
    private String name;

    @Schema(description = "任务的执行策略", example = "")
    private String executionType;

    @Schema(description = "任务编码", example = "")
    private String code;

    @Schema(description = "任务版本", example = "")
    private Long version;

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

    /** 类目编码 */
    private String catCode;
}
