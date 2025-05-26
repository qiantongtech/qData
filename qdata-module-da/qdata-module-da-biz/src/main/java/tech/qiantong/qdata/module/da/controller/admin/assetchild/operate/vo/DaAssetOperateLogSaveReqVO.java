package tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 数据资产操作记录 创建/修改 Request VO DA_ASSET_OPERATE_LOG
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "数据资产操作记录 Response VO")
@Data
public class DaAssetOperateLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "数据连接id", example = "")
    private Long datasourceId;

    @Schema(description = "表名称", example = "")
    @Size(max = 256, message = "表名称长度不能超过256个字符")
    private String tableName;

    @Schema(description = "表注释/表描述", example = "")
    @Size(max = 256, message = "表注释/表描述长度不能超过256个字符")
    private String tableComment;

    //1: 新增 2:修改 3:删除 4:导入
    @Schema(description = "操作类型", example = "")
    @Size(max = 256, message = "操作类型长度不能超过256个字符")
    private String operateType;

    @Schema(description = "操作时间", example = "")
    private Date operateTime;

    @Schema(description = "执行时间", example = "")
    private Date executeTime;

    @Schema(description = "修改前数据(JSON数据)", example = "")
    private String updateBefore;

    @Schema(description = "修改后数据(JSON数据)", example = "")
    private String updateAfter;

    @Schema(description = "字段", example = "")
    private String fieldNames;

    @Schema(description = "导入文件URL", example = "")
    @Size(max = 256, message = "导入文件URL长度不能超过256个字符")
    private String fileUrl;

    @Schema(description = "导入文件名称", example = "")
    @Size(max = 256, message = "导入文件名称长度不能超过256个字符")
    private String fileName;

    //状态;1:执行中  2:失败  3:成功   4:回滚失败  5:回滚成功
    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    @Schema(description = "更新条件JSON MD5字符串", example = "")
    private String updateWhereMd5;


}
