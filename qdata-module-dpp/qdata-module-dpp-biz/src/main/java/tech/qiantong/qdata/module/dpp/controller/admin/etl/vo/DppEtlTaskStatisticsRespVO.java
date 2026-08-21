package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "数据集成任务统计 Response VO")
public class DppEtlTaskStatisticsRespVO {

    @Schema(description = "运行中的任务数")
    private Long runningCount;

    @Schema(description = "最近一次执行结果为失败的任务数")
    private Long todayErrorCount;

    @Schema(description = "今日执行次数")
    private Long todayExecuteCount;

    @Schema(description = "今日成功率")
    private BigDecimal todaySuccessRate;

    @Schema(description = "数据统计截止时间，返回当前服务器时间", example = "2026-07-08 09:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date statisticsTime;
}

