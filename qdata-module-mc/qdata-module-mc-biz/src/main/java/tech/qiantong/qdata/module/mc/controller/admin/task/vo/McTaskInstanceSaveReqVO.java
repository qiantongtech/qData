package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 采集任务实例 创建/修改 Request VO MC_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-12-16
 */
@Schema(description = "采集任务实例 Response VO")
@Data
public class McTaskInstanceSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "来源系统ID", example = "")
    private Long sourceSystemId;

    @Schema(description = "来源系统名称", example = "")
    @Size(max = 256, message = "来源系统名称长度不能超过256个字符")
    private String sourceSystemName;

    @Schema(description = "采集任务id", example = "")
    private Long taskId;

    @Schema(description = "采集模式", example = "")
    @Size(max = 256, message = "采集模式长度不能超过256个字符")
    private String collectionMode;

    @Schema(description = "采集范围", example = "")
    @Size(max = 256, message = "采集范围长度不能超过256个字符")
    private String collectionScope;

    @Schema(description = "采集表总数量", example = "")
    private Long totalCount;

    @Schema(description = "采集表成功数量", example = "")
    private Long successCount;

    @Schema(description = "采集表失败数量", example = "")
    private Long failCount;

    @Schema(description = "失败原因", example = "")
    @Size(max = 256, message = "失败原因长度不能超过256个字符")
    private String failCause;

    @Schema(description = "新增数量", example = "")
    private Long addCount;

    @Schema(description = "删减数量", example = "")
    private Long delCount;

    @Schema(description = "变更数量", example = "")
    private Long updateCount;

    @Schema(description = "开始时间", example = "")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    private Date endTime;

    @Schema(description = "耗时", example = "")
    private Long duration;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "备注", example = "")
    @Size(max = 3000, message = "备注长度不能超过3000个字符")
    private String remark;

    @Schema(description = "描述", example = "")
    @Size(max = 3000, message = "描述长度不能超过3000个字符")
    private String description;


}
