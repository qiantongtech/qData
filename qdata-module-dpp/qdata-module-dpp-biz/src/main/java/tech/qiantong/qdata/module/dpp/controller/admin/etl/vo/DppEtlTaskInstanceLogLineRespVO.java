package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "数据集成任务实例日志行 Response VO")
public class DppEtlTaskInstanceLogLineRespVO {

    @Schema(description = "行号", example = "1")
    private Integer lineNo;

    @Schema(description = "日志时间", example = "09:30:46.123")
    private String logTime;

    @Schema(description = "错误等级：INFO、WARN、ERROR", example = "INFO")
    private String level;

    @Schema(description = "日志处理阶段", example = "表输入组件")
    private String stage;

    @Schema(description = "日志内容", example = "任务启动，节点初始化完成，准备开始数据处理")
    private String content;

    @Schema(description = "日志详情内容，错误日志保留完整堆栈信息", example = "任务启动，节点初始化完成，准备开始数据处理")
    private String detailContent;
}

