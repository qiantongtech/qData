package tech.qiantong.qdata.module.da.controller.admin.datasource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据源与项目关联关系 创建/修改 Request VO DA_DATASOURCE_PROJECT_REL
 *
 * @author qdata
 * @date 2025-03-13
 */
@Schema(description = "数据源与项目关联关系 Response VO")
@Data
public class DaDatasourceProjectRelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    @Size(max = 256, message = "项目编码长度不能超过256个字符")
    private String projectCode;

    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    @Schema(description = "是否分配到数据研发", example = "")
    private Boolean dppAssigned;

}
