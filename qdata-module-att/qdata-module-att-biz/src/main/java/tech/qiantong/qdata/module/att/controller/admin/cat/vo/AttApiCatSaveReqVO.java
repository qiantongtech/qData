package tech.qiantong.qdata.module.att.controller.admin.cat.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据服务类目管理 创建/修改 Request VO ATT_API_CAT
 *
 * @author qdata
 * @date 2025-03-11
 */
@Schema(description = "数据服务类目管理 Response VO")
@Data
public class AttApiCatSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "类别名称", example = "")
    @Size(max = 256, message = "类别名称长度不能超过256个字符")
    private String name;

    @Schema(description = "关联上级ID", example = "")
    private Long parentId;

    @Schema(description = "类别排序", example = "")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;
    @Schema(description = "有效状态", example = "")
    private Boolean validFlag;

    @Schema(description = "层级编码", example = "")
    @Size(max = 256, message = "层级编码长度不能超过256个字符")
    private String code;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
