/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
