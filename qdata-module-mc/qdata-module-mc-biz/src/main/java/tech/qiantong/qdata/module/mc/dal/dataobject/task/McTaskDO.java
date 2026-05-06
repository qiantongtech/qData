package tech.qiantong.qdata.module.mc.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 采集任务 DO 对象 MC_TASK
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
@TableName(value = "MC_TASK")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("MC_TASK_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McTaskDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 来源系统ID */
    private Long sourceSystemId;

    /** 来源系统名称 */
    private String sourceSystemName;

    /** 任务名称 */
    private String name;

    /** 数据连接id */
    private Long datasourceId;

    /** 数据库类型 */
    private String dbType;

    /** 责任人 */
    private Long leader;

    /** 责任人电话 */
    private String leaderPhone;

    /** 采集模式 */
    private String collectionMode;

    /** 采集范围 */
    private String collectionScope;

    /** 任务状态 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    /** 描述 */
    private String description;

    /**
     * 采集任务类型：1-采集，2-DDL
     */
    @Schema(description = "采集任务类型：1-采集，2-DDL", example = "1")
    private String collectType;

    /**
     * 采集黑名单
     */
    @Schema(description = "采集黑名单", example = "")
    private String blacklist;


    /**
     * cron表达式
     */
    @TableField(exist = false)
    private String cronExpression;

    /**
     * 调度状态
     */
    @TableField(exist = false)
    private String schedulerStatus;

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

    /**
     * 联系人名称
     */
    @TableField(exist = false)
    private String personChargeName;

    /**
     * 最近执行时间
     */
    @TableField(exist = false)
    private String lastExecuteTime;

    /**
     * 联系人手机号
     */
    @TableField(exist = false)
    private String createPhoneNumber;

    /**
     * 负责部门
     */
    private Long responsibleDept;
}
