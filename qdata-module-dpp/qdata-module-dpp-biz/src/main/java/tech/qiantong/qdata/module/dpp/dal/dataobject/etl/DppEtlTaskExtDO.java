package tech.qiantong.qdata.module.dpp.dal.dataobject.etl;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据集成任务-扩展数据 DO 对象 DPP_ETL_TASK_EXT
 *
 * @author qdata
 * @date 2025-04-16
 */
@Data
@TableName(value = "DPP_ETL_TASK_EXT")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DPP_ETL_TASK_EXT_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlTaskExtDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 数据汇聚任务id */
    private Long taskId;

    /**
     * 数据汇聚任务编码
     */
    private String etlTaskCode;
    /**
     * 数据汇聚任务版本
     */
    private Integer etlTaskVersion;

    /** 数据汇聚节点id */
    private Long etlNodeId;

    /** 数据汇聚节点名称 */
    private String etlNodeName;

    /** 数据汇聚节点编码 */
    private String etlNodeCode;

    /** 数据汇聚节点版本 */
    private Integer etlNodeVersion;

    /** 数据汇聚节点关系id */
    private Long etlRelationId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
