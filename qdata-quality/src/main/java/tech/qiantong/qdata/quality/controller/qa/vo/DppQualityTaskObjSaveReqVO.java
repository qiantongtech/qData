package tech.qiantong.qdata.quality.controller.qa.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据质量任务-稽查对象 创建/修改 Request VO DPP_QUALITY_TASK_OBJ
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Schema(description = "数据质量任务-稽查对象 Response VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DppQualityTaskObjSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    // 数据质量任务id
    private Long taskId;

    @Schema(description = "稽查对象名称", example = "")
    @Size(max = 256, message = "稽查对象名称长度不能超过256个字符")
    private String name;

    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    @Schema(description = "表名称", example = "")
    @Size(max = 256, message = "表名称长度不能超过256个字符")
    private String tableName;

}
