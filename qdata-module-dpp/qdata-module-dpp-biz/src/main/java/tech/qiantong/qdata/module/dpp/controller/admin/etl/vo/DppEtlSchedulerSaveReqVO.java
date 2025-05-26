package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 数据集成调度信息 创建/修改 Request VO DPP_ETL_SCHEDULER
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成调度信息 Response VO")
@Data
public class DppEtlSchedulerSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "任务编码", example = "")
    @Size(max = 256, message = "任务编码长度不能超过256个字符")
    private String taskCode;

    @Schema(description = "开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(description = "时区", example = "")
    @Size(max = 256, message = "时区长度不能超过256个字符")
    private String timezoneId;

    @Schema(description = "任务状态", example = "")
    private String status;

    @Schema(description = "cron表达式", example = "")
    @Size(max = 256, message = "cron表达式长度不能超过256个字符")
    private String cronExpression;

    @Schema(description = "失败策略", example = "")
    @Size(max = 256, message = "失败策略长度不能超过256个字符")
    private String failureStrategy;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
