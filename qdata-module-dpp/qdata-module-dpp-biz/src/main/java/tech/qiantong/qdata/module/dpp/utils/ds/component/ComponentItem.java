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
 * For brand customization, please apply for brand customization authorization via official channels.
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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dpp.utils.ds.component;

import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.module.dpp.utils.model.DsResource;

import java.util.List;
import java.util.Map;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-12 16:29
 **/
public interface ComponentItem {

    public static final long DEFAULT_ENVIRONMENT_CODE = 133155949418208L; // 默认环境编码
    public static final String DEFAULT_WORKER_GROUP = "default"; // 默认工作组
    public static final String DEFAULT_FLAG = "YES"; // 默认标志，表示节点启用
    public static final String DEFAULT_IS_CACHE = "NO"; // 默认不启用缓存
    public static final String DEFAULT_TASK_PRIORITY = "MEDIUM"; // 默认任务优先级
    public static final String DEFAULT_TASK_TYPE = "SPARK"; // 默认任务类型，SPARK或DATAX等
    public static final String DEFAULT_PROGRAM_TYPE = "JAVA"; // 默认程序类型，JAVA
    public static final String DEFAULT_MAIN_JAR = "file:/dolphinscheduler/default/resources/spart-demo-1.0.jar"; // 默认主Jar路径
    public static final String DEFAULT_DEPLOY_MODE = "client"; // 默认部署模式
    public static final int DEFAULT_DRIVER_CORES = 1; // 默认驱动核心数
    public static final String DEFAULT_DRIVER_MEMORY = "512M"; // 默认驱动内存
    public static final int DEFAULT_NUM_EXECUTORS = 1; // 默认执行器数量
    public static final String DEFAULT_EXECUTOR_MEMORY = "1G"; // 默认执行器内存
    public static final int DEFAULT_EXECUTOR_CORES = 1; // 默认执行器核心数
    public static final String DEFAULT_SQL_EXECUTION_TYPE = "SCRIPT"; // 默认SQL执行类型
    public static final String DEFAULT_CONDITION_TYPE = "NONE"; // 默认条件类型为 "NONE"

    Map<String, Object> parse(Map<String, Object> params);


    default Map<String, Object> parse2(String nodeCode, Integer nodeVersion, TaskComponentTypeEnum componentType, Map<String, Object> taskParams, String resourceUrl, List<DsResource> resourceList) {
        return null;
    }

    String code();
}
