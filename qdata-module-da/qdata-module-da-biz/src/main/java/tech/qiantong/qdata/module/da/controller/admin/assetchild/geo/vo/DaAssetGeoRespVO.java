package tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据资产-矢量 Response VO 对象 DA_ASSET_GEO
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-矢量 Response VO")
@Data
public class DaAssetGeoRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "资产id")
    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Excel(name = "文件名称")
    @Schema(description = "文件名称", example = "")
    private String fileName;

    @Excel(name = "文件路径")
    @Schema(description = "文件路径", example = "")
    private String fileUrl;

    @Schema(description = "文件类型", example = "")
    private String fileType;

    @Excel(name = "要素类型")
    @Schema(description = "要素类型", example = "")
    private String elementType;

    @Excel(name = "坐标系")
    @Schema(description = "坐标系", example = "")
    private String coordinateSystem;

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
