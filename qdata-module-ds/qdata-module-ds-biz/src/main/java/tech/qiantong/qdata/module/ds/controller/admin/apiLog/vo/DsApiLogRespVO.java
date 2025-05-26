package tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * API服务调用日志 Response VO 对象 DS_API_LOG
 *
 * @author lhs
 * @date 2025-02-12
 */
@Schema(description = "API服务调用日志 Response VO")
@Data
public class DsApiLogRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "类目ID")
    @Schema(description = "类目ID", example = "")
    private Long catId;

    @Excel(name = "类目编码")
    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Excel(name = "类目名称")
    @Schema(description = "类目名称", example = "")
    private String catName;

    @Excel(name = "ID")
    @Schema(description = "ID", example = "")
    private Long id;

    @Excel(name = "调用API服务Id")
    @Schema(description = "调用API服务Id", example = "")
    private Long apiId;

    @TableField(exist = false)
    private String apiName;

    @TableField(exist = false)
    private String reqMethod;

    @Excel(name = "调用者id")
    @Schema(description = "调用者id", example = "")
    private String callerId;

    @Excel(name = "调用者")
    @Schema(description = "调用者", example = "")
    private String callerBy;

    @Excel(name = "调用者ip")
    @Schema(description = "调用者ip", example = "")
    private String callerIp;

    @Excel(name = "调用url")
    @Schema(description = "调用url", example = "")
    private String callerUrl;

    @Excel(name = "调用参数")
    @Schema(description = "调用参数", example = "")
    private String callerParams;

    @Excel(name = "调用开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "调用开始时间", example = "")
    private Date callerStartDate;

    @Excel(name = "调用结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "调用结束时间", example = "")
    private Date callerEndDate;

    @Excel(name = "调用数据量")
    @Schema(description = "调用数据量", example = "")
    private Long callerSize;

    @Excel(name = "调用耗时(毫秒)")
    @Schema(description = "调用耗时(毫秒)", example = "")
    private Long callerTime;

    @Excel(name = "信息记录")
    @Schema(description = "信息记录", example = "")
    private String msg;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private Integer status;

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
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
