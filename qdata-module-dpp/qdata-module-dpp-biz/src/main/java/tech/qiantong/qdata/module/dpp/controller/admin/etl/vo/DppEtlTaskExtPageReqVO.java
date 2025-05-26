package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据集成任务-扩展数据 Request VO 对象 DPP_ETL_TASK_EXT
 *
 * @author qdata
 * @date 2025-04-16
 */
@Schema(description = "数据集成任务-扩展数据 Request VO")
@Data
public class DppEtlTaskExtPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "数据汇聚任务id", example = "")
    private Long taskId;

    @Schema(description = "数据汇聚节点id", example = "")
    private Long etlNodeId;

    @Schema(description = "数据汇聚节点名称", example = "")
    private String etlNodeName;

    @Schema(description = "数据汇聚节点编码", example = "")
    private String etlNodeCode;

    @Schema(description = "数据汇聚节点版本", example = "")
    private Long etlNodeVersion;

    @Schema(description = "数据汇聚节点关系id", example = "")
    private Long etlRelationId;




}
