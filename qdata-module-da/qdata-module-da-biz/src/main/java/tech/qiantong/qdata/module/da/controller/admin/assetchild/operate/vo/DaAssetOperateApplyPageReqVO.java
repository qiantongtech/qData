package tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 数据资产操作申请 Request VO 对象 DA_ASSET_OPERATE_APPLY
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "数据资产操作申请 Request VO")
@Data
public class DaAssetOperateApplyPageReqVO extends PageParam {

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

    @Schema(description = "操作JSON数据", example = "")
    private String operateJson;

    @Schema(description = "操作时间", example = "")
    private Date operateTime;

    @Schema(description = "是否已执行", example = "")
    private String executeFlag;

    @Schema(description = "执行时间", example = "")
    private Date executeTime;




}
