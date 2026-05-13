package tech.qiantong.qdata.module.mc.dal.dataobject.tableColumnRelLog;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 元数据数据库与信息及字段信息关系-日志 DO 对象 MC_TABLE_COLUMN_REL_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Data
@TableName(value = "MC_TABLE_COLUMN_REL_LOG")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("MC_TABLE_COLUMN_REL_LOG_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McTableColumnRelLogDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用 */
    private String dataType;

    /** 采集任务 id;预留字段，暂时不用 */
    private Long taskId;

    /** 库 id */
    private Long dbId;

    /** 库版本 */
    private Integer dbVersion;

    /** 表 id */
    private Long tableId;

    /** 表版本 */
    private Integer tableVersion;

    /** 字段 id */
    private String columnId;

    /** 字段版本 */
    private Integer columnVersion;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    @TableLogic
    private Boolean delFlag;

    /** 描述 */
    private String description;


}
