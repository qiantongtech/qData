package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-26 15:27
 **/
@Data
public class DppEtlTaskInstanceTreeListReqVO extends PageParam {

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "任务实例名称", example = "")
    private String name;

    @Schema(description = "右查询名称", example = "")
    private String jobName;

    @Schema(description = "执行状态", example = "")
    private String status;

    @Schema(description = "执行开始时间(格式 yyyy-MM-dd)", example = "")
    private String startTime;

    @Schema(description = "执行结束时间(格式 yyyy-MM-dd)", example = "")
    private String endTime;
}
