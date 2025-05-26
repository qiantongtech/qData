package tech.qiantong.qdata.module.dp.api.dataElem.dto;

import lombok.Data;

import java.util.Set;

/**
 * 数据元数据资产关联信息 DTO 对象 DP_DATA_ELEM_ASSET_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpDataElemAssetRelReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 数据元类型 */
    private String dataElemType;

    /** 数据元id */
    private Long dataElemId;

    /** 资产id(数据表id) */
    private Long assetId;

    /** 数据表 */
    private String tableName;

    /** 关联字段id */
    private Long columnId;

    /** 关联字段 */
    private String columnName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    /** 数据元id集合 */
    private Set<Long> elementIds;

}
