package tech.qiantong.qdata.module.att.controller.admin.client.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 应用API服务关联 Request VO 对象 ATT_CLIENT_API_REL
 *
 * @author FXB
 * @date 2025-08-21
 */
@Schema(description = "应用API服务关联 Request VO")
@Data
public class AttClientApiRelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "应用ID", example = "")
    private Long clientId;

    @Schema(description = "API服务ID", example = "")
    private Long apiId;

    @Schema(description = "是否永久有效", example = "")
    private String pvFlag;

    @Schema(description = "开始时间", example = "")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    private Date endTime;

    @Schema(description = "授权状态", example = "")
    private String status;


}
