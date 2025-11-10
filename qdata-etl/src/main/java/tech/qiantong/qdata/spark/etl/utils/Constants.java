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

package tech.qiantong.qdata.spark.etl.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author lizhuo
 */
public class Constants {

    public static final String ARRAY_EXPRESSION = "\\[].";
    public static final String CASE_EXPRESSION = "[case]";
    public static final String SUM_EXPRESSION = "[sum].";
    public static final String MAX_EXPRESSION = "[max].";
    public static final String MIN_EXPRESSION = "[min].";
    public static final String SIZE_EXPRESSION = "[size]";

    public static final String EXEC_INSERT = "insert";
    public static final String EXEC_REPLACE = "replace";
    public static final String EXEC_UPDATE = "update";
    public static final String EXEC_DELETE = "delete";
    public static final String EXEC_INSERT_OR_UPDATE = "insertorupdate";
    public static final String EXEC_JAVA_BEAN = "java";

    public static final String CONDITION_EQUAL = "=";
    public static final String CONDITION_NOT_EQUAL = "!=";
    public static final String CONDITION_LESS = "<";
    public static final String CONDITION_MORE = ">";
    public static final String CONDITION_LESS_EQUAL = "<=";
    public static final String CONDITION_MORE_EQUAL = ">=";
    public static final String CONDITION_STRING = "string";
    public static final String CONDITION_INTEGER = "integer";

    public static final int CONTENT_TYPE_FORM = 0;
    public static final int CONTENT_TYPE_JSON = 1;
    public static final int CONTENT_TYPE_URLENCODED = 2;

    public static final List<String> EXEC_ALL =
            Arrays.asList(
                    EXEC_INSERT, EXEC_REPLACE,
                    EXEC_UPDATE, EXEC_DELETE,
                    EXEC_INSERT_OR_UPDATE, EXEC_JAVA_BEAN);

    public static final List<String> EXEC_INSERT_REPLACE =
            Arrays.asList(EXEC_INSERT, EXEC_REPLACE);

    public static final List<String> EXEC_NEED_CONDITION =
            Arrays.asList(
                    EXEC_UPDATE, EXEC_DELETE, EXEC_INSERT_OR_UPDATE);

    public static final List<String> CONDITION_ALL =
            Arrays.asList(
                    CONDITION_EQUAL, CONDITION_NOT_EQUAL,
                    CONDITION_LESS, CONDITION_MORE,
                    CONDITION_LESS_EQUAL, CONDITION_MORE_EQUAL,
                    CONDITION_STRING, CONDITION_INTEGER);

    public static final List<String> CONDITION_IN =
            Arrays.asList(
                    CONDITION_EQUAL, CONDITION_NOT_EQUAL);

    public static final Integer PROCESS_METHOD_SYNC = 0;
    public static final Integer PROCESS_METHOD_ASYNC = 1;
    public static final Integer PROCESS_METHOD_CUSTOM = 2;

    public static final Integer PROCESSING_STATUS_UNPROCESSED = 0;
    public static final Integer PROCESSING_STATUS_PENDING = 1;
    public static final Integer PROCESSING_STATUS_COMPLETED = 2;
    public static final Integer PROCESSING_STATUS_FAIL = 3;

}
