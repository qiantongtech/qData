package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.List;

/**
 * Handle task-related data and operations.
 *
 * @author qdata
 * @date 2025-12-16
 */
@Schema(description = "采集任务 Response VO")
@Data
public class McTaskSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "来源系统ID", example = "")
    private Long sourceSystemId;

    @Schema(description = "来源系统名称", example = "")
    private String sourceSystemName;

    @Schema(description = "任务名称", example = "")
    private String name;

    @Schema(description = "数据连接id", example = "")
    private Long datasourceId;

    @Schema(description = "数据库类型", example = "")
    private String dbType;

    @Schema(description = "责任人", example = "")
    private Long leader;

    @Schema(description = "责任人电话", example = "")
    private String leaderPhone;

    @Schema(description = "采集模式", example = "")
    private String collectionMode;

    @Schema(description = "采集范围", example = "")
    private String collectionScope;

    @Schema(description = "任务状态", example = "")
    private String status;

    @Schema(description = "调度器", example = "DOLPHINSCHEDULER")
    private String scheduler;

    @Schema(description = "备注", example = "")
    private String remark;

    @Schema(description = "描述", example = "")
    private String description;

    /**
     * Handle task-related data and operations.
     */
    @Schema(description = "采集任务类型：1-采集，2-DDL", example = "1")
    private String collectType;

    /**
     * Implementation details.
     */
    @Schema(description = "采集黑名单", example = "")
    private String blacklist;

    /**
     * Implementation details.
     */
    @TableField(exist = false)
    private String cronExpression;

    /**
     * Handle scheduling configuration and operations.
     */
    @TableField(exist = false)
    private String schedulerStatus;

    /**
     * Implementation details.
     */
    @TableField(exist = false)
    private List<McTaskScopeSaveReqVO> scopeSaveReqVOS;

    @Schema(description = "所属部门", example = "")
    private Long responsibleDept;

}
