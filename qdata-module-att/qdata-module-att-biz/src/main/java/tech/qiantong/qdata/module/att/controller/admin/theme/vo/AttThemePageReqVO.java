package tech.qiantong.qdata.module.att.controller.admin.theme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 主题 Request VO 对象 ATT_THEME
 *
 * @author qdata
 * @date 2025-01-20
 */
@Schema(description = "主题 Request VO")
@Data
public class AttThemePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主题名称", example = "")
    private String name;

    @Schema(description = "图标url", example = "")
    private String icon;

    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    private String description;




}
