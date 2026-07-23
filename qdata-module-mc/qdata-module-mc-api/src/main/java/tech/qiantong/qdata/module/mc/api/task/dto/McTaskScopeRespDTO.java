package tech.qiantong.qdata.module.mc.api.task.dto;

import lombok.Data;

/**
 * Collection scope DTO object MC_TASK_SCOPE
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskScopeRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** task id */
    private Long taskId;

    /** Database name */
    private String dbName;

    /** Pattern name */
    private String schemaName;

    /** Is it valid */
    private Boolean validFlag;

    /** Delete flag */
    private Boolean delFlag;

    /** Description */
    private String description;


}
