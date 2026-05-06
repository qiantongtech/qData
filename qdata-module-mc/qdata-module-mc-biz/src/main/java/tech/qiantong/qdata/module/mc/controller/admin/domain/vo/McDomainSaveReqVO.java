package tech.qiantong.qdata.module.mc.controller.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 业务域 创建/修改 Request VO MC_DOMAIN
 *
 * @author qdata
 * @date 2026-02-12
 */
@Schema(description = "业务域 Response VO")
@Data
public class McDomainSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "业务名称", example = "")
    @Size(max = 256, message = "业务名称长度不能超过256个字符")
    private String name;

    @Schema(description = "关联上级ID", example = "")
    private Long parentId;

    @Schema(description = "类别排序", example = "")
    private Integer sortOrder;

    @Schema(description = "层级编码", example = "")
    @Size(max = 256, message = "层级编码长度不能超过256个字符")
    private String code;

    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;

    @Schema(description = "备注", example = "")
    @Size(max = 512, message = "备注长度不能超过256个字符")
    private String remark;

    @Schema(description = "描述", example = "")
    @Size(max = 512, message = "描述长度不能超过256个字符")
    private String description;

}
