package tech.qiantong.qdata.module.mc.api.task.dto;

import lombok.Data;

import java.util.Date;

/**
 * 采集任务实例 DTO 对象 MC_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskInstanceReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 来源系统ID */
    private Long sourceSystemId;

    /** 来源系统名称 */
    private String sourceSystemName;

    /** 采集任务id */
    private Long taskId;

    /** 采集模式 */
    private String collectionMode;

    /** 采集范围 */
    private String collectionScope;

    /** 采集表总数量 */
    private Long totalCount;

    /** 采集表成功数量 */
    private Long successCount;

    /** 采集表失败数量 */
    private Long failCount;

    /** 失败原因 */
    private String failCause;

    /** 新增数量 */
    private Long addCount;

    /** 删减数量 */
    private Long delCount;

    /** 变更数量 */
    private Long updateCount;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 耗时 */
    private Long duration;

    /** 状态 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    /** 描述 */
    private String description;


}
