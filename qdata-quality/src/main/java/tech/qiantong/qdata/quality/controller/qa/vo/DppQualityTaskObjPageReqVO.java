package tech.qiantong.qdata.quality.controller.qa.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据质量任务-稽查对象 Request VO 对象 DPP_QUALITY_TASK_OBJ
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Schema(description = "数据质量任务-稽查对象 Request VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DppQualityTaskObjPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;

    @Schema(description = "稽查对象名称", example = "")
    private String name;

    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    @Schema(description = "表名称", example = "")
    private String tableName;




}
