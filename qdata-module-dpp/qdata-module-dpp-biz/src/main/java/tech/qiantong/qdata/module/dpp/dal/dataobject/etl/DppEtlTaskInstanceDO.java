package tech.qiantong.qdata.module.dpp.dal.dataobject.etl;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 数据集成任务实例 DO 对象 DPP_ETL_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
@TableName(value = "DPP_ETL_TASK_INSTANCE")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DPP_ETL_TASK_INSTANCE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlTaskInstanceDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 类目Id */
    private Long catId;

    /** 类目编码 */
    private String catCode;

    /**
     * 任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：作业任务
     */
    private String taskType;

    /**
     * 任务实例名称
     */
    private String name;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务编码
     */
    private String taskCode;

    /**
     * 任务版本
     */
    private Integer taskVersion;

    /**
     * 状态历史(json列表)
     */
    private String statusHistory;

    /**
     * 责任人
     */
    private String personCharge;

    /**
     * 责任人名称
     */
    @TableField(exist = false)
    private String personChargeName;


    /**
     * 联系电话
     */
    private String contactNumber;
    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 调度时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scheduleTime;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 运行次数
     */
    private Integer runTimes;

    /**
     * 运行类型
     */
    private String commandType;

    /**
     * 最大重试次数
     */
    private Integer maxTryTimes;

    /**
     * 失败策略
     */
    private String failureStrategy;

    /**
     * 是否是子任务
     */
    private String subTaskFlag;

    /**
     * 状态
     */
    private String status;

    /**
     * 父任务实例id;只有为子任务时才有该值
     */
    private Long parentTaskInstanceId;

    /**
     * 父任务节点实例id;只有为子任务时才有该值
     */
    private Long parentNodeInstanceId;

    /**
     * DolphinScheduler的id
     */
    private Long dsId;

    /**
     * 是否有效
     */
    private Boolean validFlag;

    /**
     * 删除标志
     */
    @TableLogic
    private Boolean delFlag;
}
