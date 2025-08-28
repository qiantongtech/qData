package tech.qiantong.qdata.module.da.api.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据资产 DTO 对象 DA_ASSET
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
public class DaAssetRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产名称 */
    private String name;

    @Schema(description = "资产类型", example = "")
    private String type;

    /** 类目编码 */
    private String catCode;

    /** 数据连接id */
    private String datasourceId;

    /** 表名称 */
    private String tableName;

    /** 表描述 */
    private String tableComment;

    /** 数据量 */
    private Long dataCount;

    /** 字段量 */
    private Long fieldCount;

    /** 来源;1:数据发现；2:数据模型； */
    private String source;

    /** 状态 */
    private String STATUS;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;
    @Schema(description = "创建类型", example = "")
    private String createType;


}
