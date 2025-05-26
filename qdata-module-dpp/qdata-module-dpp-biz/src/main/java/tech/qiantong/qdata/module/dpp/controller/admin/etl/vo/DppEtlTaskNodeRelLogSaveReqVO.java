package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据集成任务节点关系-日志 创建/修改 Request VO DPP_ETL_TASK_NODE_REL_LOG
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成任务节点关系-日志 Response VO")
@Data
public class DppEtlTaskNodeRelLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    @Size(max = 256, message = "项目编码长度不能超过256个字符")
    private String projectCode;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "任务编码", example = "")
    @Size(max = 256, message = "任务编码长度不能超过256个字符")
    private String taskCode;

    @Schema(description = "任务版本", example = "")
    private Integer taskVersion;

    @Schema(description = "前节点id", example = "")
    private Long preNodeId;

    @Schema(description = "前节点编码", example = "")
    @Size(max = 256, message = "前节点编码长度不能超过256个字符")
    private String preNodeCode;

    @Schema(description = "前节点版本", example = "")
    private Integer preNodeVersion;

    @Schema(description = "后节点id", example = "")
    private Long postNodeId;

    @Schema(description = "后节点编码", example = "")
    @Size(max = 256, message = "后节点编码长度不能超过256个字符")
    private String postNodeCode;

    @Schema(description = "后节点版本", example = "")
    private Integer postNodeVersion;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

}
