package tech.qiantong.qdata.module.dpp.dal.dataobject.etl;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Objects;

/**
 * 数据集成任务节点关系-日志 DO 对象 DPP_ETL_TASK_NODE_REL_LOG
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
@TableName(value = "DPP_ETL_TASK_NODE_REL_LOG")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DPP_ETL_TASK_NODE_REL_LOG_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlTaskNodeRelLogDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 项目编码
     */
    private String projectCode;

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
    private Long taskVersion;

    /**
     * 前节点id
     */
    private Long preNodeId;

    /**
     * 前节点编码
     */
    private String preNodeCode;

    /**
     * 前节点版本
     */
    private Integer preNodeVersion;

    /**
     * 后节点id
     */
    private Long postNodeId;

    /**
     * 后节点编码
     */
    private String postNodeCode;

    /**
     * 后节点版本
     */
    private Integer postNodeVersion;

    /**
     * 是否有效
     */
    private Boolean validFlag;

    /**
     * 删除标志
     */
    @TableLogic
    private Boolean delFlag;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DppEtlTaskNodeRelLogDO that = (DppEtlTaskNodeRelLogDO) o;
        return Objects.equals(preNodeCode, that.preNodeCode)
                && Objects.equals(preNodeVersion, that.preNodeVersion)
                && Objects.equals(postNodeCode, that.postNodeCode)
                && Objects.equals(postNodeVersion, that.postNodeVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preNodeCode, preNodeVersion, postNodeCode, postNodeVersion);
    }
}
