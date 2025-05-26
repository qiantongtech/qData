package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * 新的数据集成请求 VO
 *
 * @author qdata
 * @date 2025-02-19
 */
@Schema(description = "新数据集成请求 VO")
@Data
public class DppEtlNewNodeSaveReqVO extends BaseEntity {

    /** 类目编码 */
    @Parameter(name = "catCode", description = "类目编码")
    private String catCode;

    /** 责任人 */
    @Parameter(name = "catCode", description = "责任人")
    private String personCharge;

    /** 联系电话 */
    @Parameter(name = "catCode", description = "联系电话")
    private String contactNumber;

    @Parameter(name = "projectCode", description = "项目编码", required = true)
    @NotNull(message = "项目编码不能为空")
    private Long projectCode;

    @Schema(description = "任务类型", example = "")
    @Size(max = 256, message = "任务类型长度不能超过256个字符")
    private String type;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Parameter(name = "name", description = "名称", required = true)
    private String name;

    @Parameter(name = "description", description = "描述", required = true)
    @Size(max = 255, message = "描述长度不能超过256个字符")
    private String description;

    @Parameter(name = "globalParams", description = "全局参数", required = false)
    private String globalParams = "[]";  // 默认值

    @Parameter(name = "locations", description = "位置参数", required = false)
    private List<Map<String,Object>> locations;

    @Parameter(name = "timeout", description = "超时时间", required = false)
    private Long timeout = 0L;  // 默认值

    @Parameter(name = "taskRelationJson", description = "任务关系的 JSON", required = true)
    private String taskRelationJson;

    @Parameter(name = "taskDefinitionList", description = "任务定义的 JSON", required = true)
    private String taskDefinitionList;

    @Parameter(name = "otherParamsJson", description = "其他参数的 JSON", required = false)
    private String otherParamsJson;

    @Parameter(name = "executionType", description = "执行类型", required = false)
    private String executionType;

    //上下限  0:未上线，1:已上线
    private String releaseState;
    //上下限  0:未上线，1:已上线
    private String schedulerState;
    private String code;
    private String crontab;
    private String id;

}
