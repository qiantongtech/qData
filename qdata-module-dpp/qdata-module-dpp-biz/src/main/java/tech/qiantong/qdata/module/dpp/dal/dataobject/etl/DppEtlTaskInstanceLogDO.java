package tech.qiantong.qdata.module.dpp.dal.dataobject.etl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 数据集成任务实例-日志 DO 对象 DPP_ETL_TASK_INSTANCE_LOG
 *
 * @author qdata
 * @date 2025-08-05
 */
@Data
@TableName(value = "DPP_ETL_TASK_INSTANCE_LOG")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DPP_ETL_TASK_INSTANCE_LOG_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlTaskInstanceLogDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 任务实例id */
    private Long taskInstanceId;

    /** 时间 */
    private Date tm;

    /** 任务类型 */
    private String taskType;

    /** 任务id */
    private Long taskId;

    /** 任务编码 */
    private String taskCode;

    /** 日志内容 */
    private String logContent;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;
}
