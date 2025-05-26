package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 在线单数据 创建/修改 Request VO DPP_ONL_DESFORM_DATA
 *
 * @author qdata
 * @date 2025-04-09
 */
@Schema(description = "在线单数据 Response VO")
@Data
public class DppOnlDesformDataSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "表单编码", example = "")
    @Size(max = 256, message = "表单编码长度不能超过256个字符")
    private String desformCode;

    @Schema(description = "表单名称", example = "")
    @Size(max = 256, message = "表单名称长度不能超过256个字符")
    private String desformName;

    @Schema(description = "表单ID", example = "")
    @Size(max = 256, message = "表单ID长度不能超过256个字符")
    private String desformId;

    @Schema(description = "表单数据", example = "")
//    @Size(max = 256, message = "表单数据长度不能超过256个字符")
    private String desformData;

    @Schema(description = "备注", example = "")
//    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
