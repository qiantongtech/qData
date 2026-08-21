package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "数据集成任务实例格式化日志详情 Response VO")
public class DppEtlTaskInstanceLogDetailRespVO {

    @Schema(description = "任务实例ID", example = "178252803754528")
    private Long taskInstanceId;

    @Schema(description = "任务名称", example = "清洗_水位异常值处理")
    private String taskName;

    @Schema(description = "任务实例状态", example = "6")
    private String status;

    @Schema(description = "任务实例状态名称", example = "失败")
    private String statusName;

    @Schema(description = "当前状态：running、success、failed、idle", example = "failed")
    private String currentStatus;

    @Schema(description = "开始时间", example = "2026-07-08 09:28:28")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description = "刷新时间，返回当前服务器时间", example = "2026-07-08 09:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date refreshTime;

    @Schema(description = "运行时长", example = "2分18秒")
    private String duration;

    @Schema(description = "格式化日志列表")
    private List<DppEtlTaskInstanceLogLineRespVO> logList;

    @Schema(description = "日志")
    private String log;
}

