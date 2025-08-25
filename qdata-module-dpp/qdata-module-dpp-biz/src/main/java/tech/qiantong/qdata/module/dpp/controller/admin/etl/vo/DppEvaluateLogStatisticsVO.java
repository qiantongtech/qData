package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 评测规则结果 创建/修改 Request VO DPP_EVALUATE_LOG
 *
 * @author qdata
 * @date 2025-07-21
 */
@Schema(description = "评测规则结果 Response VO")
@Data
public class DppEvaluateLogStatisticsVO {


    @Schema(description = "质量维度", example = "")
    @Size(max = 256, message = "质量维度长度不能超过256个字符")
    private String dimensionType;


    @Schema(description = "总数", example = "")
    private Long total;

    @Schema(description = "问题总数", example = "")
    private Long problemTotal;

    /**
     * 规则数
     */
    private Long succesTotal;

    // 占比
    private BigDecimal proportion;

    // 趋势 0：下降，1：上升
    private Long trendType;



}
