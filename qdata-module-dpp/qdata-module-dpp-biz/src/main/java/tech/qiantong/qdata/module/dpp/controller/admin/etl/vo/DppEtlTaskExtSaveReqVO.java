package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据集成任务-扩展数据 创建/修改 Request VO DPP_ETL_TASK_EXT
 *
 * @author qdata
 * @date 2025-04-16
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "数据集成任务-扩展数据 Response VO")
@Data
public class DppEtlTaskExtSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据汇聚任务id", example = "")
    private Long taskId;

    @Schema(description = "数据汇聚任务编码", example = "")
    private String etlTaskCode;

    @Schema(description = "数据汇聚任务版本", example = "")
    private Integer etlTaskVersion;

    @Schema(description = "数据汇聚节点id", example = "")
    private Long etlNodeId;

    @Schema(description = "数据汇聚节点名称", example = "")
    @Size(max = 256, message = "数据汇聚节点名称长度不能超过256个字符")
    private String etlNodeName;

    @Schema(description = "数据汇聚节点编码", example = "")
    @Size(max = 256, message = "数据汇聚节点编码长度不能超过256个字符")
    private String etlNodeCode;

    @Schema(description = "数据汇聚节点版本", example = "")
    private Integer etlNodeVersion;

    @Schema(description = "数据汇聚节点关系id", example = "")
    private Long etlRelationId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
