package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据质量日志 Request VO 对象 DPP_QUALITY_LOG
 *
 * @author qdata
 * @date 2025-07-19
 */
@Schema(description = "数据质量日志 Request VO")
@Data
public class DppQualityLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "名称", example = "")
    private String name;

    @Schema(description = "状态", example = "")
    private String successFlag;

    @Schema(description = "开始时间", example = "")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    private Date endTime;

    @Schema(description = "任务id", example = "")
    private String qualityId;

    @Schema(description = "评分", example = "")
    private Long score;

    @Schema(description = "问题数据", example = "")
    private Long problemData;


    private String path;



}
