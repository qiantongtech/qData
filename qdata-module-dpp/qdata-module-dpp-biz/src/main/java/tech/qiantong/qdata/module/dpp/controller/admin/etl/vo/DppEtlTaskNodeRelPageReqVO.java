package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据集成任务节点关系 Request VO 对象 DPP_ETL_TASK_NODE_REL
 *
 * @author qdata
 * @date 2025-02-13
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "数据集成任务节点关系 Request VO")
@Data
public class DppEtlTaskNodeRelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "任务编码", example = "")
    private String taskCode;

    @Schema(description = "任务版本", example = "")
    private Long taskVersion;

    @Schema(description = "前节点id", example = "")
    private Long preNodeId;

    @Schema(description = "前节点编码", example = "")
    private String preNodeCode;

    @Schema(description = "前节点版本", example = "")
    private Long preNodeVersion;

    @Schema(description = "后节点id", example = "")
    private Long postNodeId;

    @Schema(description = "后节点编码", example = "")
    private String postNodeCode;

    @Schema(description = "后节点版本", example = "")
    private Long postNodeVersion;




}
