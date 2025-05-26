package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据集成节点 Response VO 对象 DPP_ETL_NODE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成节点 Response VO")
@Data
public class DppEtlNodeRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：作业任务")
    @Schema(description = "任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：作业任务", example = "")
    private String taskType;

    @Excel(name = "节点类型")
    @Schema(description = "节点类型", example = "")
    private String type;

    @Excel(name = "节点名称")
    @Schema(description = "节点名称", example = "")
    private String name;

    @Excel(name = "节点编码")
    @Schema(description = "节点编码", example = "")
    private String code;

    @Excel(name = "节点版本")
    @Schema(description = "节点版本", example = "")
    private Long version;

    @Excel(name = "项目id")
    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Excel(name = "项目编码")
    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Excel(name = "节点参数")
    @Schema(description = "节点参数", example = "")
    private String parameters;

    @Excel(name = "任务优先级")
    @Schema(description = "任务优先级", example = "")
    private String priority;

    @Excel(name = "失败重试次数")
    @Schema(description = "失败重试次数", example = "")
    private Long failRetryTimes;

    @Excel(name = "失败重试间隔", readConverterExp = "分=钟")
    @Schema(description = "失败重试间隔", example = "")
    private Long failRetryInterval;

    @Excel(name = "超时时间")
    @Schema(description = "超时时间", example = "")
    private Long timeout;

    @Excel(name = "延迟执行时间", readConverterExp = "分=钟")
    @Schema(description = "延迟执行时间", example = "")
    private Long delayTime;

    @Excel(name = "CPU配额")
    @Schema(description = "CPU配额", example = "")
    private Long cpuQuota;

    @Excel(name = "最大内存")
    @Schema(description = "最大内存", example = "")
    private Long memoryMax;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Excel(name = "组件类型")
    @Schema(description = "组件类型", example = "")
    private String componentType;

    @Excel(name = "DolphinScheduler的id")
    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

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
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    /**
     *     //任务上下限  0:未上线，1:已上线
     */
    @TableField(exist = false)
    private String releaseState;

    @TableField(exist = false)
    private String taskParams = this.parameters;

}
