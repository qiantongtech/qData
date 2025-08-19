package tech.qiantong.qdata.module.dpp.utils.model;

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
    /** 节点id */
    @Schema(description = "节点id", example = "")
    private Long nodeId;

    /** 节点编码 */
    @Schema(description = "节点编码", example = "")
    private String nodeCode;

    /** 任务id */
    @Schema(description = "任务id", example = "")
    private Long taskId;

    /** 任务编码 */
    @Schema(description = "任务编码", example = "")
    private String taskCode;

    /**
     * {
     *   "prop": "id",
     *   "httpParametersType": "PARAMETER",
     *   "value": "111111"
     * }
     *
     * 	1.	PARAMETER：表示将参数作为 URL 参数传递。
     * 	2.	BODY：表示参数作为请求体传递，通常在 POST 请求中使用。
     * 	3.	HEADER：表示参数作为 HTTP 请求头的一部分传递。
     */
    private List<Map<String, Object>> httpParams;



    // 构造器
    public TaskSaveReqInput() {
        this.httpParams = new ArrayList<>(); // 初始化 httpParams
    }

    // 方法：动态添加 httpParams
    public void addHttpParam(String prop, String httpParametersType, Object value) {
        Map<String, Object> param = new HashMap<>();
        param.put("prop", prop);
        param.put("httpParametersType", httpParametersType);
        param.put("value", value);
        this.httpParams.add(param); // 将新参数添加到 httpParams 列表中
    }
}
