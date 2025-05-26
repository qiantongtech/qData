package tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据资产-视频数据 Response VO 对象 DA_ASSET_VIDEO
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-视频数据 Response VO")
@Data
public class DaAssetVideoRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "资产id")
    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Excel(name = "IP")
    @Schema(description = "IP", example = "")
    private String ip;

    @Excel(name = "端口号")
    @Schema(description = "端口号", example = "")
    private Long port;

    @Excel(name = "协议")
    @Schema(description = "协议", example = "")
    private String protocol;

    @Excel(name = "平台")
    @Schema(description = "平台", example = "")
    private String platform;

    @Excel(name = "配置JSON")
    @Schema(description = "配置JSON", example = "")
    private String config;

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
    private String remark;

}
