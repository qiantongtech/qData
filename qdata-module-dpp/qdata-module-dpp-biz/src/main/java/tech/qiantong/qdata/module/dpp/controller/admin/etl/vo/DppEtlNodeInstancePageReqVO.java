package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 数据集成节点实例 Request VO 对象 DPP_ETL_NODE_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成节点实例 Request VO")
@Data
public class DppEtlNodeInstancePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "类目", example = "")
    private String catCode;
    @Schema(description = "任务类型", example = "")
    private String taskType;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "节点实例名称", example = "")
    private String name;

    @Schema(description = "节点类型", example = "")
    private String nodeType;

    @Schema(description = "节点id", example = "")
    private Long nodeId;

    @Schema(description = "节点编码", example = "")
    private String nodeCode;

    @Schema(description = "节点版本", example = "")
    private Long nodeVersion;

    @Schema(description = "任务实例id", example = "")
    private Long taskInstanceId;

    @Schema(description = "任务实例名称", example = "")
    private String taskInstanceName;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "提交时间", example = "")
    private Date submitTime;

    @Schema(description = "起始开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description = "结束开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(description = "执行路径", example = "")
    private String executePath;

    @Schema(description = "日志路径", example = "")
    private String logPath;

    @Schema(description = "节点参数", example = "")
    private String parameters;

    @Schema(description = "节点优先级", example = "")
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
    private String status;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

    @Schema(description = "DolphinScheduler的任务实例id", example = "")
    private Long dsTaskInstanceId;

    @Schema(description = "用来右模糊查询", example = "")
    private String jobName;

}
