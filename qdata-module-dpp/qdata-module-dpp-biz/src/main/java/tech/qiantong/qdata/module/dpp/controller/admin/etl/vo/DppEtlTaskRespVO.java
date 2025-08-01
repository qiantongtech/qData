package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qdata.common.annotation.Excel;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * 数据集成任务 Response VO 对象 DPP_ETL_TASK
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成任务 Response VO")
@Data
public class DppEtlTaskRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "类目名称", example = "")
    private String catName;

    @Excel(name = "任务类型")
    @Schema(description = "任务类型", example = "")
    private String type;

    @Excel(name = "数据源类型")
    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Excel(name = "任务名称")
    @Schema(description = "任务名称", example = "")
    private String name;

    @Excel(name = "任务编码")
    @Schema(description = "任务编码", example = "")
    private String code;

    @Excel(name = "任务版本")
    @Schema(description = "任务版本", example = "")
    private Long version;

    @Excel(name = "项目id")
    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Excel(name = "项目编码")
    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Excel(name = "责任人")
    @Schema(description = "责任人", example = "")
    private String personCharge;

    @Excel(name = "联系电话")
    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Excel(name = "节点坐标信息")
    @Schema(description = "节点坐标信息", example = "")
    private String locations;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "任务的执行策略", example = "")
    private String executionType;

    @Excel(name = "超时时间")
    @Schema(description = "超时时间", example = "")
    private Long timeout;

    @Excel(name = "抽取量")
    @Schema(description = "抽取量", example = "")
    private Long extractionCount;

    @Excel(name = "写入量")
    @Schema(description = "写入量", example = "")
    private Long writeCount;

    @Excel(name = "任务状态")
    @Schema(description = "任务状态", example = "")
    private String status;

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


    @Excel(name = "最后执行时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后执行时间", example = "")
    @TableField(exist = false)
    private Date lastExecuteTime;


    @Schema(description = "草稿任务配置信息", example = "")
    private String draftJson;

    /** cron表达式 */
    @TableField(exist = false)
    private String cronExpression;

    /** 调度上下限 */
    @TableField(exist = false)
    private String schedulerState;


    @TableField(exist = false)
    List<DppEtlNodeRespVO> taskDefinitionList;

    @TableField(exist = false)
    List<DppEtlTaskNodeRelRespVO> taskRelationJson;

    @JsonProperty("label")
    public String getLabel() {
        return name; // label 字段动态取值
    }
}
