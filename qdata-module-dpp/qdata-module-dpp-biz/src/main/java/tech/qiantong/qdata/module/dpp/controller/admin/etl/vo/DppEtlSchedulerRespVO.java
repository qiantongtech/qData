package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据集成调度信息 Response VO 对象 DPP_ETL_SCHEDULER
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成调度信息 Response VO")
@Data
public class DppEtlSchedulerRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "任务id")
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Excel(name = "任务编码")
    @Schema(description = "任务编码", example = "")
    private String taskCode;

    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Excel(name = "时区")
    @Schema(description = "时区", example = "")
    private String timezoneId;

    @Schema(description = "任务状态", example = "")
    private String status;

    @Excel(name = "cron表达式")
    @Schema(description = "cron表达式", example = "")
    private String cronExpression;

    @Excel(name = "失败策略")
    @Schema(description = "失败策略", example = "")
    private String failureStrategy;

    @Excel(name = "DolphinScheduler的id")
    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

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
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
