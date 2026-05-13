package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 采集任务实例 Request VO 对象 MC_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-12-16
 */
@Schema(description = "采集任务实例 Request VO")
@Data
public class McTaskInstancePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "来源系统ID", example = "")
    private Long sourceSystemId;

    @Schema(description = "来源系统名称", example = "")
    private String sourceSystemName;

    @Schema(description = "采集任务id", example = "")
    private Long taskId;

    @Schema(description = "采集模式", example = "")
    private String collectionMode;

    @Schema(description = "采集范围", example = "")
    private String collectionScope;

    @Schema(description = "采集表总数量", example = "")
    private Long totalCount;

    @Schema(description = "采集表成功数量", example = "")
    private Long successCount;

    @Schema(description = "采集表失败数量", example = "")
    private Long failCount;

    @Schema(description = "失败原因", example = "")
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
    private String status;


    /** 是否有效 */
    private String validFlag;


    @Schema(description = "描述", example = "")
    private String description;

    @TableField(exist = false)
    private String name;


    @Schema(description = "创建时间-开始")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @TableField(exist = false)
    private Date createTimeStart;

    @Schema(description = "创建时间-结束")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @TableField(exist = false)
    private Date createTimeEnd;

    private Long datasourceId;
}
