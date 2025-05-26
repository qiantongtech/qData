package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据集成任务-日志 Response VO 对象 DPP_ETL_TASK_LOG
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成任务-日志 Response VO")
@Data
public class DppEtlTaskLogRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "任务类型")
    @Schema(description = "任务类型", example = "")
    private String type;

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

    @Excel(name = "节点坐标信息")
    @Schema(description = "节点坐标信息", example = "")
    private String locations;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

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

}
