package tech.qiantong.qdata.module.dg.controller.admin.standard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Data Element Category Management Create/Update Request VO DG_DATA_ELEM_CAT
 *
 * @author qdata
 * @date 2025-01-20
 */
@Schema(description = "Data Element Category Response VO")
@Data
public class DgDataElemCatSaveReqVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Category name", example = "")
    @Size(max = 256, message = "Category name length must not exceed 256 characters")
    private String name;

    @Schema(description = "Valid status", example = "")
    private Boolean validFlag;

    @Schema(description = "Parent ID", example = "")
    private Long parentId;

    @Schema(description = "Sort order", example = "")
    private Long sortOrder;

    @Schema(description = "Description", example = "")
    @Size(max = 3000, message = "Description length must not exceed 3000 characters")
    private String description;

    @Schema(description = "Hierarchy code", example = "")
    @Size(max = 256, message = "Hierarchy code length must not exceed 256 characters")
    private String code;

    @Schema(description = "Remark", example = "")
    @Size(max = 3000, message = "Remark length must not exceed 3000 characters")
    private String remark;

}
