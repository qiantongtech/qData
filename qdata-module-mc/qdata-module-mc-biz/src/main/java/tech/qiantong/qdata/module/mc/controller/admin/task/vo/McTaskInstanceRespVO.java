package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 采集任务实例 Response VO 对象 MC_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-12-16
 */
@Schema(description = "采集任务实例 Response VO")
@Data
public class McTaskInstanceRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "来源系统ID")
    @Schema(description = "来源系统ID", example = "")
    private Long sourceSystemId;

    @Excel(name = "来源系统名称")
    @Schema(description = "来源系统名称", example = "")
    private String sourceSystemName;

    @Excel(name = "采集任务id")
    @Schema(description = "采集任务id", example = "")
    private Long taskId;

    @Excel(name = "采集模式")
    @Schema(description = "采集模式", example = "")
    private String collectionMode;

    @Excel(name = "采集范围")
    @Schema(description = "采集范围", example = "")
    private String collectionScope;

    @Excel(name = "采集表总数量")
    @Schema(description = "采集表总数量", example = "")
    private Long totalCount;

    @Excel(name = "采集表成功数量")
    @Schema(description = "采集表成功数量", example = "")
    private Long successCount;

    @Excel(name = "采集表失败数量")
    @Schema(description = "采集表失败数量", example = "")
    private Long failCount;

    @Excel(name = "失败原因")
    @Schema(description = "失败原因", example = "")
    private String failCause;

    @Excel(name = "新增数量")
    @Schema(description = "新增数量", example = "")
    private Long addCount;

    @Excel(name = "删减数量")
    @Schema(description = "删减数量", example = "")
    private Long delCount;

    @Excel(name = "变更数量")
    @Schema(description = "变更数量", example = "")
    private Long updateCount;

    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间", example = "")
    private Date startTime;

    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间", example = "")
    private Date endTime;

    @Excel(name = "耗时")
    @Schema(description = "耗时", example = "")
    private Long duration;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private String status;

    @Excel(name = "调度引擎")
    @Schema(description = "调度引擎", example = "DOLPHINSCHEDULER")
    private String taskScheduler;

    @Excel(name = "Quartz调度任务id")
    @Schema(description = "Quartz调度任务id", example = "")
    private Long quartzId;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    /**
     * 任务名称
     */
    @TableField(exist = false)
    private String name;
    /**
     * 任务状态
     */
    @TableField(exist = false)
    private String taskStatus;

    /**
     * 创建人电话
     */
    private String createPhoneNumber;

    /**
     * 数据源名称
     */
    @TableField(exist = false)
    private String datasourceName;


    /**
     * 数据源类型
     */
    @TableField(exist = false)
    private String datasourceType;

}
