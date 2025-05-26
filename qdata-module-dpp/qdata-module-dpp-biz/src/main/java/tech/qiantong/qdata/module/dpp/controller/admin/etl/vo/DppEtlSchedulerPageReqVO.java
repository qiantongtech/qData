package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 数据集成调度信息 Request VO 对象 DPP_ETL_SCHEDULER
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成调度信息 Request VO")
@Data
public class DppEtlSchedulerPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "任务编码", example = "")
    private String taskCode;

    @Schema(description = "开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(description = "时区", example = "")
    private String timezoneId;

    @Schema(description = "任务状态", example = "")
    private String status;

    @Schema(description = "cron表达式", example = "")
    private String cronExpression;

    @Schema(description = "失败策略", example = "")
    private String failureStrategy;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;




}
