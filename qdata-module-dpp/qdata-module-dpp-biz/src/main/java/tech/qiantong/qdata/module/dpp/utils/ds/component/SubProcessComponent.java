package tech.qiantong.qdata.module.dpp.utils.ds.component;

import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.utils.JSONUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <P>
 * 用途:开发任务 组件
 * </p>
 **/
public class SubProcessComponent implements ComponentItem {

    /**
     *
     * taskParams SUB_PROCESS（子任务，开发任务也是这个）
     * {
     *     "localParams": [],//默认 []
     *     "resourceList": [],//默认 []
     *     "processDefinitionCode": 135576103357024//子任务编码
     * }
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> parse(Map<String, Object> params) {
        Map<String, Object> taskParams = new LinkedHashMap<>();
        taskParams.put("localParams", params.getOrDefault("localParams", new ArrayList<>())); // 默认空列表
        taskParams.put("resourceList", params.getOrDefault("resourceList", new ArrayList<>())); // 默认空列表
        String processDefinitionCode = MapUtils.getString(params,"processDefinitionCode", "");
        taskParams.put("processDefinitionCode", JSONUtils.convertToLong(processDefinitionCode)); // 默认空字符串
        return taskParams;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.SUB_PROCESS.getCode();
    }


    /**
     * 将字符串转换为 long 类型
     *
     * @param processDefinitionCode 要转换的字符串
     * @return 转换后的 long 值，若转换失败返回 -1
     */
    public static long convertToLong(String processDefinitionCode) {
        if (processDefinitionCode == null || processDefinitionCode.trim().isEmpty()) {
            return -1;
        }
        try {
            return Long.parseLong(processDefinitionCode.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
