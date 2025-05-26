package tech.qiantong.qdata.module.dpp.api.etl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 数据集成调度信息 DTO 对象 DPP_ETL_SCHEDULER
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlSchedulerRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 任务id */
    private Long taskId;

    /** 任务编码 */
    private String taskCode;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 时区 */
    private String timezoneId;

    @Schema(description = "任务状态", example = "")
    private String status;

    /** cron表达式 */
    private String cronExpression;

    /** 失败策略 */
    private String failureStrategy;

    /** DolphinScheduler的id */
    private Long dsId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
