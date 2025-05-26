package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 数据集成任务实例 创建/修改 Request VO DPP_ETL_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成任务实例 Response VO")
@Data
public class DppEtlTaskInstanceSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "任务实例名称", example = "")
    @Size(max = 256, message = "任务实例名称长度不能超过256个字符")
    private String name;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "任务编码", example = "")
    @Size(max = 256, message = "任务编码长度不能超过256个字符")
    private String taskCode;

    @Schema(description = "任务版本", example = "")
    private Long taskVersion;

    @Schema(description = "状态历史(json列表)", example = "")
    @Size(max = 256, message = "状态历史(json列表)长度不能超过256个字符")
    private String statusHistory;

    @Schema(description = "责任人", example = "")
    @Size(max = 256, message = "责任人长度不能超过256个字符")
    private String personCharge;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    @Size(max = 256, message = "项目编码长度不能超过256个字符")
    private String projectCode;

    @Schema(description = "开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(description = "运行次数", example = "")
    private Integer runTimes;

    @Schema(description = "运行类型", example = "")
    @Size(max = 256, message = "运行类型长度不能超过256个字符")
    private String commandType;

    @Schema(description = "最大重试次数", example = "")
    private Long maxTryTimes;

    @Schema(description = "失败策略", example = "")
    @Size(max = 256, message = "失败策略长度不能超过256个字符")
    private String failureStrategy;

    @Schema(description = "是否是子任务", example = "")
    @Size(max = 256, message = "是否是子任务长度不能超过256个字符")
    private String subTaskFlag;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
