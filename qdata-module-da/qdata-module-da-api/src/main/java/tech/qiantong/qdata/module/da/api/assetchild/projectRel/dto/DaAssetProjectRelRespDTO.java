package tech.qiantong.qdata.module.da.api.assetchild.projectRel.dto;

import lombok.Data;

/**
 * 数据资产与项目关联关系 DTO 对象 DA_ASSET_PROJECT_REL
 *
 * @author qdata
 * @date 2025-04-18
 */
@Data
public class DaAssetProjectRelRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产id */
    private Long assetId;

    /** 项目id */
    private Long projectId;

    /** 项目编码 */
    private String projectCode;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
