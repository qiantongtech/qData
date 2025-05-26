package tech.qiantong.qdata.module.att.controller.admin.client.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用管理 Response VO 对象 ATT_CLIENT
 *
 * @author qdata
 * @date 2025-02-18
 */
@Schema(description = "应用管理 Response VO")
@Data
public class AttClientRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "应用名称")
    @Schema(description = "应用名称", example = "")
    private String name;

    @Excel(name = "应用类型")
    @Schema(description = "应用类型", example = "")
    private String type;

    @Excel(name = "应用秘钥")
    @Schema(description = "应用秘钥", example = "")
    private String secret;

    @Excel(name = "主页地址")
    @Schema(description = "主页地址", example = "")
    private String homepageUrl;

    @Excel(name = "允许授权的url")
    @Schema(description = "允许授权的url", example = "")
    private String allowUrl;

    @Excel(name = "同步地址")
    @Schema(description = "同步地址", example = "")
    private String syncUrl;

    @Excel(name = "应用图标")
    @Schema(description = "应用图标", example = "")
    private String logo;

    @Excel(name = "应用描述")
    @Schema(description = "应用描述", example = "")
    private String description;

    @Excel(name = "是否公开")
    @Schema(description = "是否公开", example = "")
    private String publicFlag;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
