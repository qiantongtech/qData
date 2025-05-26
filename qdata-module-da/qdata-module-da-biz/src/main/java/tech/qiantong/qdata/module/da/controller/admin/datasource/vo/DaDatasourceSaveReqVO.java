package tech.qiantong.qdata.module.da.controller.admin.datasource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceProjectRelDO;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 数据源 创建/修改 Request VO DA_DATASOURCE
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据源 Response VO")
@Data
public class DaDatasourceSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据源名称", example = "")
    @Size(max = 256, message = "数据源名称长度不能超过256个字符")
    private String datasourceName;

    @Schema(description = "数据源类型", example = "")
    @Size(max = 256, message = "数据源类型长度不能超过256个字符")
    private String datasourceType;

    @Schema(description = "数据源配置(json字符串)", example = "")
    @Size(max = 256, message = "数据源配置(json字符串)长度不能超过256个字符")
    private String datasourceConfig;

    @Schema(description = "旧项目集合", example = "")
    private List<Long> projectListOld;

    @Schema(description = "项目集合", example = "")
    private List<DaDatasourceProjectRelDO> projectList;

    @Schema(description = "IP", example = "")
    @Size(max = 256, message = "IP长度不能超过256个字符")
    private String ip;

    @Schema(description = "端口号", example = "")
    private Long port;

    @Schema(description = "数据库表数", example = "")
    private Long listCount;

    @Schema(description = "同步记录数", example = "")
    private Long syncCount;

    @Schema(description = "同步数据量大小", example = "")
    private Long dataSize;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
