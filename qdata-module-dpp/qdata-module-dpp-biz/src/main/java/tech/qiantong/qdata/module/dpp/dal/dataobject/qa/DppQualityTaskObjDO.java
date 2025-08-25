package tech.qiantong.qdata.module.dpp.dal.dataobject.qa;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据质量任务-稽查对象 DO 对象 DPP_QUALITY_TASK_OBJ
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Data
@TableName(value = "DPP_QUALITY_TASK_OBJ")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DPP_QUALITY_TASK_OBJ_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppQualityTaskObjDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 数据质量任务ID */
    private Long taskId;

    /** 稽查对象名称 */
    private String name;

    /** 数据源id */
    private Long datasourceId;

    /** 表名称 */
    private String tableName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
