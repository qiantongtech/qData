package tech.qiantong.qdata.module.att.controller.admin.cat.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 作业类目管理 Request VO 对象 ATT_JOB_CAT
 *
 * @author qdata
 * @date 2025-03-11
 */
@Schema(description = "作业类目管理 Request VO")
@Data
public class AttJobCatPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "项目code")
    private String projectCode;

    @Schema(description = "类别名称", example = "")
    private String name;

    @Schema(description = "关联上级ID", example = "")
    private Long parentId;

    @Schema(description = "类别排序", example = "")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "层级编码", example = "")
    private String code;


}
