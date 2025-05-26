package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据元代码 创建/修改 Request VO DP_DATA_ELEM_CODE
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元代码 Response VO")
@Data
public class DpDataElemCodeSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据元id", example = "")
    @Size(max = 256, message = "数据元id长度不能超过256个字符")
    private String dataElemId;

    @Schema(description = "代码值", example = "")
    @Size(max = 256, message = "代码值长度不能超过256个字符")
    private String codeValue;

    @Schema(description = "代码名称", example = "")
    @Size(max = 256, message = "代码名称长度不能超过256个字符")
    private String codeName;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
