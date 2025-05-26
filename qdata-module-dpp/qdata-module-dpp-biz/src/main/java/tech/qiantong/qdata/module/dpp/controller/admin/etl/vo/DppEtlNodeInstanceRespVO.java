package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据集成节点实例 Response VO 对象 DPP_ETL_NODE_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成节点实例 Response VO")
@Data
public class DppEtlNodeInstanceRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "节点实例名称")
    @Schema(description = "节点实例名称", example = "")
    private String name;

    @Excel(name = "节点类型")
    @Schema(description = "节点类型", example = "")
    private String nodeType;

    @Excel(name = "节点id")
    @Schema(description = "节点id", example = "")
    private Long nodeId;

    @Excel(name = "节点编码")
    @Schema(description = "节点编码", example = "")
    private String nodeCode;

    @Excel(name = "节点版本")
    @Schema(description = "节点版本", example = "")
    private Long nodeVersion;

    @Excel(name = "任务实例id")
    @Schema(description = "任务实例id", example = "")
    private Long taskInstanceId;

    @Excel(name = "任务实例名称")
    @Schema(description = "任务实例名称", example = "")
    private String taskInstanceName;

    @Excel(name = "项目id")
    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Excel(name = "项目编码")
    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "提交时间", example = "")
    private Date submitTime;

    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Excel(name = "执行路径")
    @Schema(description = "执行路径", example = "")
    private String executePath;

    @Excel(name = "日志路径")
    @Schema(description = "日志路径", example = "")
    private String logPath;

    @Excel(name = "节点参数")
    @Schema(description = "节点参数", example = "")
    private String parameters;

    @Excel(name = "节点优先级")
    @Schema(description = "节点优先级", example = "")
    private String priority;

    @Excel(name = "重试次数")
    @Schema(description = "重试次数", example = "")
    private Long retryTimes;

    @Excel(name = "重试间隔", readConverterExp = "分=钟")
    @Schema(description = "重试间隔", example = "")
    private Long fretryInterval;

    @Excel(name = "延迟执行时间", readConverterExp = "分=钟")
    @Schema(description = "延迟执行时间", example = "")
    private Long delayTime;

    @Excel(name = "CPU配额")
    @Schema(description = "CPU配额", example = "")
    private Long cpuQuota;

    @Excel(name = "最大内存")
    @Schema(description = "最大内存", example = "")
    private Long memoryMax;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private String status;

    @Excel(name = "DolphinScheduler的id")
    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

    @Excel(name = "DolphinScheduler的任务实例id")
    @Schema(description = "DolphinScheduler的任务实例id", example = "")
    private Long dsTaskInstanceId;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    @Excel(name = "责任人名称")
    @Schema(description = "责任人名称", example = "")
    private String personChargeName;

    @Excel(name = "运行类型")
    @Schema(description = "运行类型", example = "")
    private String commandType;
}
