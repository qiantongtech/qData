package tech.qiantong.qdata.module.dpp.controller.admin.qa.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据质量任务-稽查对象 Request VO 对象 DPP_QUALITY_TASK_OBJ
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Schema(description = "数据质量任务-稽查对象 Request VO")
@Data
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

    /** 数据质量任务ID */
    private Long taskId;




}
