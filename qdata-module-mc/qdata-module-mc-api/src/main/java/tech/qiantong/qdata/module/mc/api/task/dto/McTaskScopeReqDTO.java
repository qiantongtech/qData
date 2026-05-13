package tech.qiantong.qdata.module.mc.api.task.dto;

import lombok.Data;

/**
 * 采集范围 DTO 对象 MC_TASK_SCOPE
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskScopeReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
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
    private Boolean delFlag;

    /** 描述 */
    private String description;


}
