package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据元数据资产关联信息 创建/修改 Request VO DP_DATA_ELEM_ASSET_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元数据资产关联信息 Response VO")
@Data
public class DpDataElemAssetRelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据元类型", example = "")
    @Size(max = 256, message = "数据元类型长度不能超过256个字符")
    private String dataElemType;

    @Schema(description = "数据元id", example = "")
    @Size(max = 256, message = "数据元id长度不能超过256个字符")
    private String dataElemId;

    @Schema(description = "资产id(数据表id)", example = "")
    @Size(max = 256, message = "资产id(数据表id)长度不能超过256个字符")
    private String assetId;

    @Schema(description = "数据表", example = "")
    @Size(max = 256, message = "数据表长度不能超过256个字符")
    private String tableName;

    @Schema(description = "关联字段id", example = "")
    @Size(max = 256, message = "关联字段id长度不能超过256个字符")
    private String columnId;

    @Schema(description = "关联字段", example = "")
    @Size(max = 256, message = "关联字段长度不能超过256个字符")
    private String columnName;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
