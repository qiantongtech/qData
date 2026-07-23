package tech.qiantong.qdata.module.mc.utils.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TaskSaveReqInput extends BaseEntity {

    @Schema(description = "任务名称", example = "")
    private String name;
    private Long id;
    /** Node id */
    @Schema(description = "节点id", example = "")
    private Long nodeId;

    /** Node encoding */
    @Schema(description = "节点编码", example = "")
    private String nodeCode;

    /** task id */
    @Schema(description = "任务id", example = "")
    private Long taskId;

    /** Task encoding */
    @Schema(description = "任务编码", example = "")
    private String taskCode;

    /**
     * {
     *   "prop": "id",
     *   "httpParametersType": "PARAMETER",
     *   "value": "111111"
     * }
     *
     * 1. PARAMETER: Indicates passing parameters as URL parameters.
     * 2. BODY: Indicates that the parameters are passed as the request body, usually used in POST requests.
     * 3. HEADER: Indicates that parameters are passed as part of the HTTP request header.
     */
    private List<Map<String, Object>> httpParams;



    // Constructor
    public TaskSaveReqInput() {
        this.httpParams = new ArrayList<>(); // Initialize httpParams
    }

    // Method: Dynamically add httpParams
    public void addHttpParam(String prop, String httpParametersType, Object value) {
        Map<String, Object> param = new HashMap<>();
        param.put("prop", prop);
        param.put("httpParametersType", httpParametersType);
        param.put("value", value);
        this.httpParams.add(param); // Add new parameters to the httpParams list
    }
}
