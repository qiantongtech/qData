package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.utils.JSONUtils;

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
@NoArgsConstructor
@AllArgsConstructor
public class DppEtlNewNodeSaveReqVO extends BaseEntity {

    /** 类目编码 */
    @Parameter(name = "catCode", description = "类目编码")
    private String catCode;
    private Long catId;

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
    private String status;
    private String code;
    private String crontab;
    private String id;

    /**
     * taskType（SPARK、FINK）
     * taskPriority任务优先级
     * workerGroup分组
     * failRetryTimes失败重试次数
     * delayTime延时执行时间
     * failRetryInterval失败重试间隔
     *
     *
     * SPARK如下：
     * driverCores驱动核心数
     * driverMemory驱动内存
     * numExecutors执行器数量
     * executorMemory执行器内存数
     * executorCores执行器核心数
     * yarnQueue    -----yarm队列
     *
     *
     * FINK如下：
     * jobManagerMemory----JobManager内存数
     * taskManagerMemory------TaskManager内存数
     * slot-----Slot数量
     * taskManager-----TaskManager数量
     * parallelism并行度
     * yarnQueue    -----yarm队列
     */
    @Schema(description = "草稿任务配置信息", example = "")
    private String draftJson;

    public DppEtlNewNodeSaveReqVO(DppEtlTaskUpdateQueryRespVO src) {
        if (src == null) {
            return;
        }

        this.catCode = src.getCatCode();
        this.catId = src.getCatId();
        this.personCharge = src.getPersonCharge();
        this.contactNumber = src.getContactNumber();
        this.projectCode = JSONUtils.convertToLong(src.getProjectCode());
        this.projectId = src.getProjectId();
        this.type = src.getType();
        this.name = src.getName();
        this.description = src.getDescription();
        this.locations = src.getLocations();
        this.timeout = src.getTimeout();
        this.executionType = src.getExecutionType();
        this.crontab = src.getCrontab();
        this.draftJson = src.getDraftJson();

        // 需要转 JSON 的字段
        if (src.getTaskRelationJson() != null) {
            this.taskRelationJson = JSONUtils.toJson(src.getTaskRelationJson());
        }
        if (src.getTaskDefinitionList() != null) {
            this.taskDefinitionList = JSONUtils.toJson(src.getTaskDefinitionList());
        }

        // 全部固定为未上线
        this.releaseState = "0";
        this.schedulerState = "0";
        this.status = "0";
    }
}
