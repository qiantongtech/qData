package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.List;

/**
 * 数据集成节点 Request VO 对象 DPP_ETL_NODE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成节点 Request VO")
@Data
public class DppEtlNodePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "节点类型", example = "")
    private String type;

    @Schema(description = "节点名称", example = "")
    private String name;

    @Schema(description = "节点编码", example = "")
    private String code;

    @Schema(description = "节点版本", example = "")
    private Long version;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "节点参数", example = "")
    private String parameters;

    @Schema(description = "任务优先级", example = "")
    private String priority;

    @Schema(description = "失败重试次数", example = "")
    private Long failRetryTimes;

    @Schema(description = "失败重试间隔", example = "")
    private Long failRetryInterval;

    @Schema(description = "超时时间", example = "")
    private Long timeout;

    @Schema(description = "延迟执行时间", example = "")
    private Long delayTime;

    @Schema(description = "CPU配额", example = "")
    private Long cpuQuota;

    @Schema(description = "最大内存", example = "")
    private Long memoryMax;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;


    @TableField(exist = false)
    private List<String> codeList;
}
