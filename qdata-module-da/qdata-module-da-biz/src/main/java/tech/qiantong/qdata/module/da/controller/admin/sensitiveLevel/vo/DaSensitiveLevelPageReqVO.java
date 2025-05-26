package tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 敏感等级 Request VO 对象 DA_SENSITIVE_LEVEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "敏感等级 Request VO")
@Data
public class DaSensitiveLevelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "敏感级别", example = "")
    private String sensitiveLevel;

    @Schema(description = "敏感规则", example = "")
    private String sensitiveRule;

    @Schema(description = "起始字符位置", example = "")
    private Long startCharLoc;

    @Schema(description = "截止字符位置", example = "")
    private Long endCharLoc;

    @Schema(description = "遮盖字符", example = "")
    private String maskCharacter;

    @Schema(description = "上下线标识", example = "")
    private String onlineFlag;

    @Schema(description = "描述", example = "")
    private String description;




}
