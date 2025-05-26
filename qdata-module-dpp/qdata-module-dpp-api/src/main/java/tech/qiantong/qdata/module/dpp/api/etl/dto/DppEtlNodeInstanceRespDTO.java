package tech.qiantong.qdata.module.dpp.api.etl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 数据集成节点实例 DTO 对象 DPP_ETL_NODE_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlNodeInstanceRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 节点实例名称 */
    private String name;

    /** 节点类型 */
    private String nodeType;

    /** 节点id */
    private Long nodeId;

    /** 节点编码 */
    private String nodeCode;

    /** 节点版本 */
    private Long nodeVersion;

    /** 任务实例id */
    private Long taskInstanceId;

    /** 任务实例名称 */
    private String taskInstanceName;

    /** 项目id */
    private Long projectId;

    /** 项目编码 */
    private String projectCode;

    /** 提交时间 */
    private Date submitTime;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 执行路径 */
    private String executePath;

    /** 日志路径 */
    private String logPath;

    /** 节点参数 */
    private String parameters;

    /** 节点优先级 */
    private String priority;

    /** 重试次数 */
    private Long retryTimes;

    /** 重试间隔（分钟） */
    private Long fretryInterval;

    /** 延迟执行时间（分钟） */
    private Long delayTime;

    /** CPU配额 */
    private Long cpuQuota;

    /** 最大内存 */
    private Long memoryMax;

    /** 状态 */
    private String status;

    /** DolphinScheduler的id */
    private Long dsId;

    /** DolphinScheduler的任务实例id */
    private Long dsTaskInstanceId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
