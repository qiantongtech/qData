package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 数据集成节点实例 创建/修改 Request VO DPP_ETL_NODE_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成节点实例 Response VO")
@Data
public class DppEtlNodeInstanceSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "节点实例名称", example = "")
    @Size(max = 256, message = "节点实例名称长度不能超过256个字符")
    private String name;

    @Schema(description = "节点类型", example = "")
    @Size(max = 256, message = "节点类型长度不能超过256个字符")
    private String nodeType;

    @Schema(description = "节点id", example = "")
    private Long nodeId;

    @Schema(description = "节点编码", example = "")
    @Size(max = 256, message = "节点编码长度不能超过256个字符")
    private String nodeCode;

    @Schema(description = "节点版本", example = "")
    private Long nodeVersion;

    @Schema(description = "任务实例id", example = "")
    private Long taskInstanceId;

    @Schema(description = "任务实例名称", example = "")
    @Size(max = 256, message = "任务实例名称长度不能超过256个字符")
    private String taskInstanceName;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    @Size(max = 256, message = "项目编码长度不能超过256个字符")
    private String projectCode;

    @Schema(description = "提交时间", example = "")
    private Date submitTime;

    @Schema(description = "开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(description = "执行路径", example = "")
    @Size(max = 256, message = "执行路径长度不能超过256个字符")
    private String executePath;

    @Schema(description = "日志路径", example = "")
    @Size(max = 256, message = "日志路径长度不能超过256个字符")
    private String logPath;

    @Schema(description = "节点参数", example = "")
    @Size(max = 256, message = "节点参数长度不能超过256个字符")
    private String parameters;

    @Schema(description = "节点优先级", example = "")
    @Size(max = 256, message = "节点优先级长度不能超过256个字符")
    private String priority;

    @Schema(description = "重试次数", example = "")
    private Long retryTimes;

    @Schema(description = "重试间隔", example = "")
    private Long fretryInterval;

    @Schema(description = "延迟执行时间", example = "")
    private Long delayTime;

    @Schema(description = "CPU配额", example = "")
    private Long cpuQuota;

    @Schema(description = "最大内存", example = "")
    private Long memoryMax;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

    @Schema(description = "DolphinScheduler的任务实例id", example = "")
    private Long dsTaskInstanceId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
