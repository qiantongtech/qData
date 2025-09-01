package tech.qiantong.qdata.module.dpp.api.etl.dto;

import lombok.Data;

import java.util.Date;

/**
 * 数据集成任务实例-日志 DTO 对象 DPP_ETL_TASK_INSTANCE_LOG
 *
 * @author qdata
 * @date 2025-08-05
 */
@Data
public class DppEtlTaskInstanceLogReqDTO {

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
    private Boolean delFlag;


}
