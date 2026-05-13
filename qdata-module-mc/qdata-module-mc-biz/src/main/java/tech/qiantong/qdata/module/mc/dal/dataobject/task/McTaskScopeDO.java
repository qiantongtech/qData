package tech.qiantong.qdata.module.mc.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 采集范围 DO 对象 MC_TASK_SCOPE
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
@TableName(value = "MC_TASK_SCOPE")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("MC_TASK_SCOPE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McTaskScopeDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务id */
    private Long taskId;

    /** 数据库名称 */
    private String dbName;

    /** 模式名 */
    private String schemaName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    /** 描述 */
    private String description;

}
