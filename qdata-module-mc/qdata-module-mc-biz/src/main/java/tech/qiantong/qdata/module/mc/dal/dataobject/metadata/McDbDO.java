package tech.qiantong.qdata.module.mc.dal.dataobject.metadata;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;

import java.util.Date;

/**
 * 数据库 DO 对象 MC_DB
 *
 * @author qdata
 * @date 2026-02-11
 */
@Data
@TableName(value = "MC_DB")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("MC_DB_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McDbDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 采集任务id;预留字段，暂时不用 */
    private Long taskId;

    /** 来源系统ID */
    private Long sourceSystemId;

    /** 来源系统名称 */
    private String sourceSystemName;

    /** 版本 */
    private Integer version;

    /** 数据源id */
    private Long datasourceId;

    /** IP */
    private String ip;

    /** 端口号 */
    private Integer port;

    /** 数据源配置(json字符串) */
    private String datasourceConfig;

    /** 数据库类型 */
    private String dbType;

    /** 数据库名称 */
    private String dbName;

    /** 模式名;可空 */
    private String schemaName;

    /** 安全等级id */
    private Long safetyLevelId;

    /** 所属分层;1:ODS 2:DWD 3:DWS  4:ADS 5:外部系统） */
    private String belongingLayer;

    /** 所属系统 */
    private String belongingSystem;

    /** 业务责任人 */
    private Long businessLeader;

    /** 业务责任人电话 */
    private String businessLeaderPhone;

    /** 技术责任人 */
    private Long techLeader;

    /** 技术责任人电话 */
    private String techLeaderPhone;

    /** 存储大小 */
    private Integer storageSize;

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
    private DaDatasourceRespDTO datasource;

    @TableField(exist = false)
    private Integer tableCount;

    /**
     * 字段数量
     */
    @TableField(exist = false)
    private Integer columnCount;

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

    /**
     * 负责部门
     */
    private Long responsibleDept;

    /**
     * 负责部门名称
     */
    @TableField(exist = false)
    private String responsibleDeptName;

    /**
     * 数据行数
     */
    private Long dataRowCount;

}
