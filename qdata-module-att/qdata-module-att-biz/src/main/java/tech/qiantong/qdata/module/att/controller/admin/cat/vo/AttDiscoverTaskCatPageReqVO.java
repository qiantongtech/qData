package tech.qiantong.qdata.module.att.controller.admin.cat.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据发现任务类目管理 Request VO 对象 ATT_DISCOVER_TASK_CAT
 *
 * @author qdata
 * @date 2025-01-20
 */
@Schema(description = "数据发现任务类目管理 Request VO")
@Data
public class AttDiscoverTaskCatPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "项目code")
    private String projectCode;
    @Schema(description = "类别名称", example = "")
    private String name;
    @Schema(description = "关联上级ID", example = "")
    private Long parentId;
    @Schema(description = "类别编码", example = "")
    private String code;








}
