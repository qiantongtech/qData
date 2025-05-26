package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 在线单数据 Request VO 对象 DPP_ONL_DESFORM_DATA
 *
 * @author qdata
 * @date 2025-04-09
 */
@Schema(description = "在线单数据 Request VO")
@Data
public class DppOnlDesformDataPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "表单编码", example = "")
    private String desformCode;

    @Schema(description = "表单名称", example = "")
    private String desformName;

    @Schema(description = "表单ID", example = "")
    private String desformId;

    @Schema(description = "表单数据", example = "")
    private String desformData;




}
