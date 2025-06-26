package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeLogDO;

import javax.validation.constraints.Size;
import java.util.Map;
import java.util.Objects;

/**
 * 数据集成节点 创建/修改 Request VO DPP_ETL_NODE
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成节点 Response VO")
@Data
public class DppEtlNodeSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    /**
     * 任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：作业任务
     */
    private String taskType;

    @Schema(description = "节点类型", example = "")
    @Size(max = 256, message = "节点类型长度不能超过256个字符")
    private String type;

    @Schema(description = "节点名称", example = "")
    @Size(max = 256, message = "节点名称长度不能超过256个字符")
    private String name;

    @Schema(description = "节点编码", example = "")
    @Size(max = 256, message = "节点编码长度不能超过256个字符")
    private String code;

    @Schema(description = "节点版本", example = "")
    private Integer version;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    @Size(max = 256, message = "项目编码长度不能超过256个字符")
    private String projectCode;

    @Schema(description = "节点参数", example = "")
    private String parameters;

    @Schema(description = "节点参数", example = "")
    private Map<String, Object> taskParams;

    @Schema(description = "任务优先级", example = "")
    @Size(max = 256, message = "任务优先级长度不能超过256个字符")
    private String priority;

    @Schema(description = "失败重试次数", example = "")
    private Long failRetryTimes;

    @Schema(description = "失败重试间隔", example = "")
    private Long failRetryInterval;

    @Schema(description = "超时时间", example = "")
    private Long timeout;

    @Schema(description = "延迟执行时间", example = "")
    private Long delayTime;

    @Schema(description = "CPU配额", example = "")
    private Long cpuQuota;

    @Schema(description = "最大内存", example = "")
    private Long memoryMax;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "组件类型", example = "")
    private String componentType;

    @Schema(description = "DolphinScheduler的id", example = "")
    private Long dsId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if(o instanceof DppEtlNodeLogDO){
            DppEtlNodeLogDO that = (DppEtlNodeLogDO) o;
            return Objects.equals(name, that.getName())
                    && Objects.equals(taskType, that.getTaskType())
                    && Objects.equals(type, that.getType())
                    && Objects.equals(priority, that.getPriority())
                    && Objects.equals(parameters, that.getParameters())
                    && Objects.equals(description, that.getDescription());
        }else{
            DppEtlNodeSaveReqVO that = (DppEtlNodeSaveReqVO) o;
            return Objects.equals(name, that.getName())
                    && Objects.equals(taskType, that.getTaskType())
                    && Objects.equals(type, that.getType())
                    && Objects.equals(priority, that.getPriority())
                    && Objects.equals(parameters, that.getParameters())
                    && Objects.equals(description, that.getDescription());
        }
    }
}
