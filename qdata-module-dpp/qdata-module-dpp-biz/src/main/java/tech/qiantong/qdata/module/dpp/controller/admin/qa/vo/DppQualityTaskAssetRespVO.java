package tech.qiantong.qdata.module.dpp.controller.admin.qa.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogStatisticsVO;

import java.util.List;

@Data
public class DppQualityTaskAssetRespVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "资产id")
    private Long assetId;


    /** 评分 */
    private Long score;

    /** 问题数据 */
    private Long problemData;

    private List<DppEvaluateLogStatisticsVO> voList;

    private List<DppEvaluateLogRespVO> evaluateLogRespVOS;
}
