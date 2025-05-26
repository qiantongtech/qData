package tech.qiantong.qdata.module.att.controller.admin.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 项目与用户关联关系 创建/修改 Request VO ATT_PROJECT_USER_REL
 *
 * @author qdata
 * @date 2025-02-11
 */
@Schema(description = "项目与用户关联关系 Response VO")
@Data
public class AttProjectUserRelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "项目空间ID", example = "")
    private Long projectId;

    @Schema(description = "用户ID", example = "")
    private Long userId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    @Schema(description = "用户id集合", example = "")
    private List<Long> userIdList;

    @Schema(description = "角色id集合", example = "")
    private List<Long> roleIdList;


}
