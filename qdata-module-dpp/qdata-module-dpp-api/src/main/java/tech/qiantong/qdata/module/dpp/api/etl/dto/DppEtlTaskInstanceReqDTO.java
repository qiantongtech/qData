package tech.qiantong.qdata.module.dpp.api.etl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 数据集成任务实例 DTO 对象 DPP_ETL_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlTaskInstanceReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 任务实例名称 */
    private String name;

    /** 任务id */
    private Long taskId;

    /** 任务编码 */
    private String taskCode;

    /** 任务版本 */
    private Long taskVersion;

    /** 状态历史(json列表) */
    private String statusHistory;

    /** 责任人 */
    private String personCharge;

    /** 项目id */
    private Long projectId;

    /** 项目编码 */
    private String projectCode;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 运行类型 */
    private String commandType;

    /** 最大重试次数 */
    private Long maxTryTimes;

    /** 失败策略 */
    private String failureStrategy;

    /** 是否是子任务 */
    private String subTaskFlag;

    /** 状态 */
    private String status;

    /** DolphinScheduler的id */
    private Long dsId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
