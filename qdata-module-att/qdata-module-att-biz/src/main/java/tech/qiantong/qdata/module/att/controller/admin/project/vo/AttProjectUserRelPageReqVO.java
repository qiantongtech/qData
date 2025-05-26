package tech.qiantong.qdata.module.att.controller.admin.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 项目与用户关联关系 Request VO 对象 ATT_PROJECT_USER_REL
 *
 * @author qdata
 * @date 2025-02-11
 */
@Schema(description = "项目与用户关联关系 Request VO")
@Data
public class AttProjectUserRelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "项目空间ID", example = "")
    private Long projectId;

    @Schema(description = "用户ID", example = "")
    private Long userId;

    @Schema(description = "用户名称", example = "")
    private String userName;

    @Schema(description = "用户昵称", example = "")
    private String nickName;

    @Schema(description = "手机号码", example = "")
    private String phoneNumber;

    @Schema(description = "起始时间", example = "")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    private Date endTime;

}
