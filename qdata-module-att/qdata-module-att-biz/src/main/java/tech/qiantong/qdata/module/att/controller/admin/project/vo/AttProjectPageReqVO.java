package tech.qiantong.qdata.module.att.controller.admin.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 项目 Request VO 对象 ATT_PROJECT
 *
 * @author shu
 * @date 2025-01-20
 */
@Schema(description = "项目 Request VO")
@Data
public class AttProjectPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "项目名称", example = "")
    private String name;
    @Schema(description = "负责人", example = "")
    private Long managerId;






}
