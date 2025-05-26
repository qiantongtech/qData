package tech.qiantong.qdata.module.dpp.utils.ds.component;

import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <P>
 * 用途:SHELL
 * </p>
 **/
public class ShellComponent implements ComponentItem {

    /**
     * taskParams PROCEDURE (存储过程)
     * "localParams": [],//默认 []
     * "resourceList": [],//默认 []
     * "rawScript":"脚本"
     * }
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> parse(Map<String, Object> params) {
        Map<String, Object> taskParams = new LinkedHashMap<>();
        taskParams.put("localParams", params.getOrDefault("localParams", new ArrayList<>()));
        taskParams.put("resourceList", params.getOrDefault("resourceList", new ArrayList<>()));
        taskParams.put("rawScript", params.getOrDefault("rawScript", ""));
        return taskParams;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.SHELL_DEV.getCode();
    }
}
