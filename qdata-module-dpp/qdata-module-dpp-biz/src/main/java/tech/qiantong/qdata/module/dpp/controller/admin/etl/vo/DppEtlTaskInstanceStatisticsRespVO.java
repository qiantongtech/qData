package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "数据集成任务实例统计 Response VO")
public class DppEtlTaskInstanceStatisticsRespVO {

    @Schema(description = "全部实例数", example = "10")
    private Long allCount;

    @Schema(description = "运行中实例数", example = "2")
    private Long runningCount;

    @Schema(description = "成功实例数", example = "7")
    private Long successCount;

    @Schema(description = "失败实例数", example = "1")
    private Long failCount;

    @Schema(description = "今日异常实例数")
    private Long todayErrorCount;

    @Schema(description = "今日执行次数")
    private Long todayExecuteCount;

    @Schema(description = "今日成功率")
    private BigDecimal todaySuccessRate;

    @Schema(description = "最后刷新时间，返回当前服务器时间", example = "2026-07-08 09:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date refreshTime;
}

