package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.util.Date;
import java.util.List;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-26 15:30
 **/
@Data
public class DppEtlTaskInstanceTreeListRespVO {

    private String dataId;

    private Long id;

    @Excel(name = "实例名称")
    @Schema(description = "实例名称", example = "")
    private String name;

    @Excel(name = "任务名称")
    @Schema(description = "任务名称,只有实例节点才有", example = "")
    private String taskName;

    @Excel(name = "编码")
    @Schema(description = "任务编码", example = "")
    private String code;

    @Excel(name = "调度时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "调度时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scheduleTime;

    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Excel(name = "运行时长", width = 30)
    @Schema(description = "运行时长", example = "")
    private String duration;

    @Schema(description = "运行次数", example = "")
    private Integer runTimes;

    @Excel(name = "运行类型")
    @Schema(description = "运行类型", example = "")
    private String commandType;

    @Excel(name = "是否是子任务")
    @Schema(description = "是否是子任务", example = "")
    private String subTaskFlag;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private String status;

    @Schema(description = "数据类型 1:作业任务实例 2:作业任务节点实例  3:子任务节点实例", example = "")
    private String dataType;

    @Schema(description = "节点类型", example = "")
    private String nodeType;

    @Schema(description = "日志", example = "")
    private String logPath;

    private boolean hasChildren;

    private Long taskInstanceId;

    private Long nodeInstanceId;

    private List<DppEtlTaskInstanceTreeListRespVO> children;
}
