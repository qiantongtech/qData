package tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 数据资产申请 Response VO 对象 DA_ASSET_APPLY
 *
 * @author shu
 * @date 2025-03-19
 */
@Schema(description = "数据资产申请 Response VO")
@Data
public class DaAssetApplyRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "资产id")
    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Excel(name = "资产名称")
    @Schema(description = "资产名称", example = "")
    private String assetName;

    @Excel(name = "资产英文名称")
    @Schema(description = "资产英文名称", example = "")
    private String assetTableName;

    @Excel(name = "资产类目")
    @Schema(description = "资产类目", example = "")
    private String catAssetName;

    @Excel(name = "资产类目编码")
    @Schema(description = "资产类目编码", example = "")
    private String catAssetCode;

    @Excel(name = "是申请过来的资产还是项目自己生成的资产0：申请，1：自创")
    @Schema(description = "是申请过来的资产还是项目自己生成的资产0：申请，1：自创", example = "")
    private String sourceType;

    @Excel(name = "项目id")
    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Excel(name = "项目名称")
    @Schema(description = "项目名称", example = "")
    private String projectName;

    @Excel(name = "主题名称")
    @Schema(description = "主题名称", example = "")
    private String themeName;

    @TableField(exist = false)
    private List<DaAssetThemeRelRespVO> daAssetThemeRelList;

    @Excel(name = "项目编码")
    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Excel(name = "申请理由")
    @Schema(description = "申请理由", example = "")
    private String applyReason;

    @Excel(name = "审批理由")
    @Schema(description = "审批理由", example = "")
    private String approvalReason;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
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
    private String remark;

    @Excel(name = "数据源名称")
    @Schema(description = "数据源名称", example = "")
    private String datasourceName;

    @Excel(name = "数据源ip")
    @Schema(description = "数据源ip", example = "")
    private String datasourceIp;

    @Excel(name = "数据源类型")
    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Excel(name = "资产描述")
    @Schema(description = "资产描述", example = "")
    private String description;

    @Excel(name = "手机号码")
    @Schema(description = "手机号码", example = "")
    private String phonenumber;
}
