package tech.qiantong.qdata.module.mc.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 采集任务实例 DO 对象 MC_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
@TableName(value = "MC_TASK_INSTANCE")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("MC_TASK_INSTANCE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McTaskInstanceDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 来源系统ID */
    private Long sourceSystemId;

    /** 来源系统名称 */
    private String sourceSystemName;

    /** 采集任务id */
    private Long taskId;

    /** 采集模式 */
    private String collectionMode;

    /** 采集范围 */
    private String collectionScope;

    /** 采集表总数量 */
    private Long totalCount;

    /** 采集表成功数量 */
    private Long successCount;

    /** 采集表失败数量 */
    private Long failCount;

    /** 失败原因 */
    private String failCause;

    /** 新增数量 */
    private Long addCount;

    /** 删减数量 */
    private Long delCount;

    /** 变更数量 */
    private Long updateCount;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 耗时 */
    private Long duration;

    /** 状态 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    /** 描述 */
    private String description;

    /**
     * 任务名称
     */
    @TableField(exist = false)
    private String name;
    /**
     * 任务状态
     */
    @TableField(exist = false)
    private String taskStatus;

    /**
     * 创建人电话
     */
    @TableField(exist = false)
    private String createPhoneNumber;

    /**
     * 数据源名称
     */
    @TableField(exist = false)
    private String datasourceName;


    /**
     * 数据源类型
     */
    @TableField(exist = false)
    private String datasourceType;
}
