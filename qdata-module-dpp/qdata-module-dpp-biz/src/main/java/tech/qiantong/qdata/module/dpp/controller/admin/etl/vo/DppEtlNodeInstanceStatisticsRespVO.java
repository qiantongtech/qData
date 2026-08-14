package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "Data development node instance statistics Response VO")
public class DppEtlNodeInstanceStatisticsRespVO {

    private Long allCount;
    private Long runningCount;
    private Long successCount;
    private Long failCount;
    private Long todayErrorCount;
    private Long todayExecuteCount;
    private BigDecimal todaySuccessRate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date refreshTime;
}
