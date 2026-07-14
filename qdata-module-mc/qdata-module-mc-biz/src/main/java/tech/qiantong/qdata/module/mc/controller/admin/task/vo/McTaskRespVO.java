package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskScopeDO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Handle task-related data and operations.
 *
 * @author qdata
 * @date 2025-12-16
 */
@Schema(description = "采集任务 Response VO")
@Data
public class McTaskRespVO implements Serializable {

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

    @Excel(name = "任务名称")
    @Schema(description = "任务名称", example = "")
    private String name;

    @Excel(name = "数据连接id")
    @Schema(description = "数据连接id", example = "")
    private Long datasourceId;

    @Excel(name = "数据库类型")
    @Schema(description = "数据库类型", example = "")
    private String dbType;

    @Excel(name = "责任人")
    @Schema(description = "责任人", example = "")
    private Long leader;

    @Excel(name = "责任人电话")
    @Schema(description = "责任人电话", example = "")
    private String leaderPhone;

    @Excel(name = "采集模式")
    @Schema(description = "采集模式", example = "")
    private String collectionMode;

    @Excel(name = "采集范围")
    @Schema(description = "采集范围", example = "")
    private String collectionScope;

    @Excel(name = "任务状态")
    @Schema(description = "任务状态", example = "")
    private String status;

    @Excel(name = "调度器")
    @Schema(description = "调度器", example = "DOLPHINSCHEDULER")
    private String scheduler;

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
     * Handle DolphinScheduler operations.
     */
    @TableField(exist = false)
    private String taskCode;

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
     * Handle scheduling configuration and operations.
     */
    @TableField(exist = false)
    private String jobId;

    /**
     * Implementation details.
     */
    @TableField(exist = false)
    private List<McTaskScopeDO> scopeSaveReqVOS;

    /**
     * Handle database and data source configuration.
     */
    @TableField(exist = false)
    private DaDatasourceRespDTO datasourceDO;

    /**
     * Handle database and data source configuration.
     */
    @TableField(exist = false)
    private String datasourceName;

    /**
     * Handle database and data source configuration.
     */
    @TableField(exist = false)
    private String datasourceType;


    /**
     * Implementation details.
     */
    @TableField(exist = false)
    private String personChargeName;

    /**
     * Implementation details.
     */
    @TableField(exist = false)
    private String lastExecuteTime;

    /**
     * Create the required record.
     */
    private String createPhoneNumber;

    /**
     * Implementation details.
     */
    private Long responsibleDept;
}
