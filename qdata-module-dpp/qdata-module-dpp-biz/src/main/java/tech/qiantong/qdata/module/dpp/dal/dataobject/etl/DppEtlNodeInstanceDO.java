package tech.qiantong.qdata.module.dpp.dal.dataobject.etl;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 数据集成节点实例 DO 对象 DPP_ETL_NODE_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
@TableName(value = "DPP_ETL_NODE_INSTANCE")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DPP_ETL_NODE_INSTANCE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlNodeInstanceDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：作业任务 */
    private String taskType;

    /** 节点实例名称 */
    private String name;

    /** 节点类型 */
    private String nodeType;

    /** 节点id */
    private Long nodeId;

    /** 节点编码 */
    private String nodeCode;

    /** 节点版本 */
    private Integer nodeVersion;

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
    private Integer retryTimes;

    /** 延迟执行时间（分钟） */
    private Integer delayTime;

    /** CPU配额 */
    private Integer cpuQuota;

    /** 最大内存 */
    private Integer memoryMax;

    /** 状态 */
    private String status;

    /** 组件类型 */
    private String componentType;

    /** DolphinScheduler的id */
    private Long dsId;

    /** DolphinScheduler的任务实例id */
    private Long dsTaskInstanceId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    /**
     * 责任人名称
     */
    @TableField(exist = false)
    private String personChargeName;

    /**
     * 运行类型
     */
    @TableField(exist = false)
    private String commandType;
}
