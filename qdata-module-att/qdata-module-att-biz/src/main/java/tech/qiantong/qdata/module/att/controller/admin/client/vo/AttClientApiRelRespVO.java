package tech.qiantong.qdata.module.att.controller.admin.client.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用API服务关联 Response VO 对象 ATT_CLIENT_API_REL
 *
 * @author FXB
 * @date 2025-08-21
 */
@Schema(description = "应用API服务关联 Response VO")
@Data
public class AttClientApiRelRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "应用ID")
    @Schema(description = "应用ID", example = "")
    private Long clientId;

    @Excel(name = "API服务ID")
    @Schema(description = "API服务ID", example = "")
    private Long apiId;


    @Excel(name = "API服务名称")
    @Schema(description = "API服务名称", example = "")
    private String apiName;

    @Excel(name = "API服务路径")
    @Schema(description = "API服务路径", example = "")
    private String apiUrl;

    @Excel(name = "请求方式")
    @Schema(description = "请求方式 1：get，2：post（字典：ds_api_bas_info_api_method_type）", example = "")
    private String reqMethod;

    @Excel(name = "是否永久有效")
    @Schema(description = "是否永久有效  0否 1是 （字典：sys_is_or_not）", example = "")
    private String pvFlag;

    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "开始时间", example = "")
    private Date startTime;

    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "结束时间", example = "")
    private Date endTime;

    @Excel(name = "授权状态")
    @Schema(description = "授权状态 1:开启  0:关闭", example = "")
    private String status;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String REMARK;

}
