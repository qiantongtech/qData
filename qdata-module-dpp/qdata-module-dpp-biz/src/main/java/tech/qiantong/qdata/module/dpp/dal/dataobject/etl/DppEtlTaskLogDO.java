package tech.qiantong.qdata.module.dpp.dal.dataobject.etl;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据集成任务-日志 DO 对象 DPP_ETL_TASK_LOG
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
@TableName(value = "DPP_ETL_TASK_LOG")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DPP_ETL_TASK_LOG_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlTaskLogDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 1：离线任务 2：实时任务 3：数据开发任务 4：作业任务 */
    private String type;

    /** 任务名称 */
    private String name;

    /** 任务编码 */
    private String code;

    /** 任务版本 */
    private Long version;

    /** 项目id */
    private Long projectId;

    /** 项目编码 */
    private String projectCode;

    /** 责任人 */
    private String personCharge;

    /** 节点坐标信息 */
    private String locations;

    @Schema(description = "任务的执行策略", example = "")
    private String executionType;

    /** 描述 */
    private String description;

    /** 超时时间 */
    private Long timeout;

    /** 抽取量 */
    private Long extractionCount;

    /** 写入量 */
    private Long writeCount;

    /** 任务状态 */
    private String status;

    /** DolphinScheduler的id */
    private Long dsId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
