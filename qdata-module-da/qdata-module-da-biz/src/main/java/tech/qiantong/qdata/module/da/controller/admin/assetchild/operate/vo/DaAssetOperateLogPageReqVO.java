package tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 数据资产操作记录 Request VO 对象 DA_ASSET_OPERATE_LOG
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "数据资产操作记录 Request VO")
@Data
public class DaAssetOperateLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "数据连接id", example = "")
    private Long datasourceId;

    @Schema(description = "表名称", example = "")
    private String tableName;

    @Schema(description = "表注释/表描述", example = "")
    private String tableComment;

    @Schema(description = "操作类型", example = "")
    private String operateType;

    @Schema(description = "操作时间", example = "")
    private Date operateTime;

    @Schema(description = "执行时间", example = "")
    private Date executeTime;

    @Schema(description = "修改前数据(JSON数据)", example = "")
    private String updateBefore;
    @Schema(description = "更新条件JSON MD5字符串", example = "")
    private String updateWhereMd5;

    @Schema(description = "修改后数据(JSON数据)", example = "")
    private String updateAfter;

    @Schema(description = "字段", example = "")
    private String fieldNames;

    @Schema(description = "导入文件URL", example = "")
    private String fileUrl;

    @Schema(description = "导入文件名称", example = "")
    private String fileName;

    @Schema(description = "状态", example = "")
    private String status;



    @Schema(description = "起始时间", example = "")
    private Date startTime;

    @Schema(description = "结束时间", example = "")
    private Date endTime;



}
