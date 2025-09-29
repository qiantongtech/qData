package tech.qiantong.qdata.module.att.controller.admin.client.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 应用API服务关联 创建/修改 Request VO ATT_CLIENT_API_REL
 *
 * @author FXB
 * @date 2025-08-21
 */
@Schema(description = "应用API服务关联 Response VO")
@Data
public class AttClientApiRelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "应用ID", example = "")
    private Long clientId;

    @Schema(description = "API服务ID", example = "")
    private Long apiId;

    @Schema(description = "是否永久有效", example = "")
    @Size(max = 256, message = "是否永久有效长度不能超过256个字符")
    private String pvFlag;

    @Schema(description = "开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @Schema(description = "授权状态", example = "")
    @Size(max = 256, message = "授权状态长度不能超过256个字符")
    private String status;
}
