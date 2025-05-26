package tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据资产操作记录 Response VO 对象 DA_ASSET_OPERATE_LOG
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "数据资产操作记录 Response VO")
@Data
public class DaAssetOperateLogRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "资产id")
    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Excel(name = "数据连接id")
    @Schema(description = "数据连接id", example = "")
    private Long datasourceId;

    @Excel(name = "表名称")
    @Schema(description = "表名称", example = "")
    private String tableName;

    @Excel(name = "表注释/表描述")
    @Schema(description = "表注释/表描述", example = "")
    private String tableComment;

    @Excel(name = "操作类型")
    @Schema(description = "操作类型", example = "")
    private String operateType;

    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "操作时间", example = "")
    private Date operateTime;

    @Excel(name = "执行时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "执行时间", example = "")
    private Date executeTime;

    @Excel(name = "修改前数据(JSON数据)")
    @Schema(description = "修改前数据(JSON数据)", example = "")
    private String updateBefore;

    @Excel(name = "修改后数据(JSON数据)")
    @Schema(description = "修改后数据(JSON数据)", example = "")
    private String updateAfter;

    @Excel(name = "字段")
    @Schema(description = "字段", example = "")
    private String fieldNames;

    @Excel(name = "导入文件URL")
    @Schema(description = "导入文件URL", example = "")
    private String fileUrl;

    @Excel(name = "导入文件名称")
    @Schema(description = "导入文件名称", example = "")
    private String fileName;

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

    @Schema(description = "更新条件JSON MD5字符串", example = "")
    private String updateWhereMd5;


    /** 用户名称 */
    @TableField(exist = false)
    private String userName;

    /** 手机号码 */
    @TableField(exist = false)
    private String phoneNumber;

    /** 用户昵称 */
    @TableField(exist = false)
    private String nickName;
}
