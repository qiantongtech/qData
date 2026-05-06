package tech.qiantong.qdata.module.mc.dal.dataobject.metadata;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 元数据字段信息 DO 对象 MC_COLUMN
 *
 * @author qdata
 * @date 2026-02-11
 */
@Data
@TableName(value = "MC_COLUMN")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("MC_COLUMN_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McColumnDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 采集任务id;预留字段，暂时不用 */
    private Long taskId;

    /** 库id */
    private Long dbId;

    /** 表信息id */
    private Long tableId;

    /** 数据源id;冗余字段 */
    private Long datasourceId;

    /** 版本 */
    private Integer version;

    /** 安全等级id */
    private Long safetyLevelId;

    /** 数据元id */
    private Long dataElemId;

    /** 字段名称 */
    private String columnName;

    /** 字段注释 */
    private String columnComment;

    /** 字段类型 */
    private String columnType;

    /** 数据长度 */
    private Integer columnLength;

    /** 数据精度 */
    private Integer columnPrecision;

    /** 数据小数位 */
    private Integer columnScale;

    /** 数据默认值 */
    private String defaultValue;

    /** 是否主键;0:否 1:是 */
    private String pkFlag;

    /** 是否外键;0:否 1:是 */
    private String fkFlag;

    /** 是否可空;0:否 1:是 */
    private String nullableFlag;

    /** 业务定义 */
    private String businessDefinition;

    /** 度量单位 */
    private String measuringUnit;

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
    private String dataElemName;

    @TableField(exist = false)
    private String dbName;

    @TableField(exist = false)
    private String tableName;

    /**
     * 安全等级名称
     */
    @TableField(exist = false)
    private String safetyLevelName;

    @TableField(exist = false)
    private Long mcTableLogId;

    //字段变更内容
    @TableField(exist = false)
    private String updateMsg;

    //字段变更类型 - 数字代码表示不同变更类型：1-字段注释变更, 2-字段类型变更, 3-字段长度变更, 4-字段精度变更, 5-字段小数位数变更, 6-字段默认值变更, 7-主键标识变更, 8-外键标识变更, 9-可空标识变更
    @TableField(exist = false)
    private String updateType;

    /**
     * 是否自增
     */
    private String autoIncrementFlag;

    /**
     * 是否分区字段
     */
    private String partitionFlag;

    /**
     * 字段规范
     */
    private String columnStandard;

    /**
     * 取值逻辑
     */
    private String valueRule;

    /** 责任人 */
    private Long businessLeader;

    /**
     * 负责部门
     */
    private Long responsibleDept;

    /**
     * 是否唯一
     */
    private String uniqueFlag;
}
