package tech.qiantong.qdata.module.mc.dal.dataobject.metadata;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 元数据信息 DO 对象 MC_TABLE
 *
 * @author qdata
 * @date 2026-02-11
 */
@Data
@TableName(value = "MC_TABLE")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("MC_TABLE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McTableDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 采集任务id;预留字段，暂时不用 */
    private Long taskId;

    /** 库id */
    private Long dbId;

    /** 数据源id;冗余字段 */
    private Long datasourceId;

    /** 版本 */
    private Integer version;

    /** 表名称（表英文名称） */
    private String tableName;

    /** 表注释/表描述（表中文名称） */
    private String tableComment;

    /** 安全等级id */
    private Long safetyLevelId;

    /** 数据库名 */
    private String dbName;

    /** 模式名;可空 */
    private String schemaName;

    /** 存储类型 */
    private String storageType;

    /** 存储大小 */
    private Integer storageSize;

    /** 业务责任人 */
    private Long businessLeader;

    /** 业务责任人电话 */
    private String businessLeaderPhone;

    /** 技术责任人 */
    private Long techLeader;

    /** 技术责任人电话 */
    private String techLeaderPhone;

    /** 是否主表;0：否，1：是 */
    private String masterFlag;

    /** 是否临时表;0：否，1：是 */
    private String tempFlag;

    /** 数据质量 */
    private Integer dataQuality;

    /** 审核状态;1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常 */
    private String auditStatus;

    /** 审核时间 */
    private Date auditTime;

    /** 状态;0：未发布，1：已发布 */
    private String status;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    @TableLogic
    private Boolean delFlag;

    /** 描述 */
    private String description;

    /**
     * 是否在门户展示：0-不展示，1-展示
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;

    @TableField(exist = false)
    private Long sourceSystemId;

    @TableField(exist = false)
    private String sourceSystemName;

    /**
     * 业务责任人姓名
     */
    @TableField(exist = false)
    private String businessLeaderName;

    /**
     * 技术责任人姓名
     */
    @TableField(exist = false)
    private String techLeaderName;

    /**
     * 安全等级名称
     */
    @TableField(exist = false)
    private String safetyLevelName;


    /** cron表达式 */
    @TableField(exist = false)
    private String cronExpression;

    /** 字段数量 */
    private Long columnCount;

    /** 索引 */
    private String tbIndex;

    /** 行数 */
    private Long rowCount;

    /** 分区字段 */
    private String partitionKey;
    /**
     * 存储引擎
     */
    private String storageEngine;

    /**
     * 负责部门
     */
    private Long responsibleDept;

    /**
     * 主键字段
     */
    private String primaryKey;

    /**
     * 表创建时间
     */
    private Date tbCreateTime;

    /**
     * 数据更新时间
     */
    private Date dataUpdateTime;

    /**
     * 是否已经关联资产
     */
    @TableField(exist = false)
    private Boolean dssetFlag;

    /**
     * 创建人电话
     */
    @TableField(exist = false)
    private String createPhoneNumber;

    /**
     * 更新人电话
     */
    @TableField(exist = false)
    private String updatePhoneNumber;

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
