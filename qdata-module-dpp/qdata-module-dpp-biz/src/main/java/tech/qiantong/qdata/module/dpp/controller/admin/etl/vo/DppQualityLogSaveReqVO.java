package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 数据质量日志 创建/修改 Request VO DPP_QUALITY_LOG
 *
 * @author qdata
 * @date 2025-07-19
 */
@Schema(description = "数据质量日志 Response VO")
@Data
public class DppQualityLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称", example = "")
    @Size(max = 256, message = "名称长度不能超过256个字符")
    private String name;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String successFlag;

    @Schema(description = "开始时间", example = "")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    private Date endTime;

    @Schema(description = "任务id", example = "")
    @Size(max = 256, message = "任务id长度不能超过256个字符")
    private String qualityId;

    @Schema(description = "评分", example = "")
    private Long score;

    @Schema(description = "问题数据", example = "")
    private Long problemData;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


    private String path;

}
