package tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据资产与项目关联关系 创建/修改 Request VO DA_ASSET_PROJECT_REL
 *
 * @author qdata
 * @date 2025-04-18
 */
@Schema(description = "数据资产与项目关联关系 Response VO")
@Data
public class DaAssetProjectRelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产id", example = "")
    private Long assetId;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    @Size(max = 256, message = "项目编码长度不能超过256个字符")
    private String projectCode;


}
