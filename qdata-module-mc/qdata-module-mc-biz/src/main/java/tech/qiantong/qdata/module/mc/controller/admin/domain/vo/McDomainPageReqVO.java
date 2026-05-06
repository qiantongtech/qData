package tech.qiantong.qdata.module.mc.controller.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 业务域 Request VO 对象 MC_DOMAIN
 *
 * @author qdata
 * @date 2026-02-12
 */
@Schema(description = "业务域 Request VO")
@Data
public class McDomainPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "业务名称", example = "")
    private String name;

    @Schema(description = "关联上级ID", example = "")
    private Long parentId;

    @Schema(description = "类别排序", example = "")
    private Integer sortOrder;

    @Schema(description = "层级编码", example = "")
    private String code;

    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;


    @Schema(description = "描述", example = "")
    private String description;


}
