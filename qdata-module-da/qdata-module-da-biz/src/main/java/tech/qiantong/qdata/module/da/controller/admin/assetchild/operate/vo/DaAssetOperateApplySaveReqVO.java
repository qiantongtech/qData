package tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 数据资产操作申请 创建/修改 Request VO DA_ASSET_OPERATE_APPLY
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "数据资产操作申请 Response VO")
@Data
public class DaAssetOperateApplySaveReqVO extends BaseEntity {

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

    @Schema(description = "操作类型", example = "")
    @Size(max = 256, message = "操作类型长度不能超过256个字符")
    private String operateType;

    @Schema(description = "操作JSON数据", example = "")
    @Size(max = 256, message = "操作JSON数据长度不能超过256个字符")
    private String operateJson;

    @Schema(description = "操作时间", example = "")
    private Date operateTime;

    @Schema(description = "是否已执行", example = "")
    @Size(max = 256, message = "是否已执行长度不能超过256个字符")
    private String executeFlag;

    @Schema(description = "执行时间", example = "")
    private Date executeTime;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
