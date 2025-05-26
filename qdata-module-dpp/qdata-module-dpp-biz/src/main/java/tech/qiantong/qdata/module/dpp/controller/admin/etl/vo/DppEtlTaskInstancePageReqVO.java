package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 数据集成任务实例 Request VO 对象 DPP_ETL_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成任务实例 Request VO")
@Data
public class DppEtlTaskInstancePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "任务类型", example = "1：离线任务 2：实时任务 3：数据开发任务 4：作业任务")
    private String taskType;

    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "任务实例名称", example = "")
    private String name;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "任务编码", example = "")
    private String taskCode;

    @Schema(description = "任务版本", example = "")
    private Integer taskVersion;

    @Schema(description = "状态历史(json列表)", example = "")
    private String statusHistory;

    @Schema(description = "责任人", example = "")
    private String personCharge;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "调度时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scheduleTime;

    @Schema(description = "开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(description = "运行次数", example = "")
    private Integer runTimes;

    @Schema(description = "运行类型", example = "")
    private String commandType;

    @Schema(description = "最大重试次数", example = "")
    private Long maxTryTimes;

    @Schema(description = "失败策略", example = "")
    private String failureStrategy;

    @Schema(description = "是否是子任务", example = "")
    private String subTaskFlag;

    @Schema(description = "状态", example = "")
    private String status;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;


}
