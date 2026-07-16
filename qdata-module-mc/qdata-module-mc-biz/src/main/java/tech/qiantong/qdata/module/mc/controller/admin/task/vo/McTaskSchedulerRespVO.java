package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * Data integration scheduling information Response VO object MC_TASK_SCHEDULER
 *
 * @author qdata
 * @date 2025-12-16
 */
@Schema(description = "数据集成调度信息 Response VO")
@Data
public class McTaskSchedulerRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "任务id")
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Excel(name = "调度器id")
    @Schema(description = "调度器ID", example = "")
    private String jobId;

    @Excel(name = "DolphinScheduler任务编码")
    @Schema(description = "DolphinScheduler任务编码（用于API调用）", example = "")
    private String taskCode;

    @Excel(name = "调度引擎")
    @Schema(description = "调度引擎", example = "DOLPHINSCHEDULER")
    private String taskScheduler;

    @Excel(name = "Quartz调度任务id")
    @Schema(description = "Quartz调度任务id", example = "")
    private Long quartzId;

    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间", example = "")
    private Date startTime;

    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间", example = "")
    private Date endTime;

    @Excel(name = "时区")
    @Schema(description = "时区", example = "")
    private String timezoneId;

    @Excel(name = "cron表达式")
    @Schema(description = "cron表达式", example = "")
    private String cronExpression;

    @Excel(name = "失败策略")
    @Schema(description = "失败策略", example = "")
    private String failureStrategy;

    @Excel(name = "调度状态")
    @Schema(description = "调度状态", example = "")
    private String status;

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
