package tech.qiantong.qdata.module.da.controller.admin.datasource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据源与项目关联关系 Request VO 对象 DA_DATASOURCE_PROJECT_REL
 *
 * @author qdata
 * @date 2025-03-13
 */
@Schema(description = "数据源与项目关联关系 Request VO")
@Data
public class DaDatasourceProjectRelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "是否分配到数据研发", example = "")
    private Boolean dppAssigned;


}
