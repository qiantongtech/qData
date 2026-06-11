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
 */

package tech.qiantong.qdata.quality.controller.quality.vo;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import tech.qiantong.qdata.common.database.core.DbColumn;

import java.io.Serializable;
import java.util.List;

@Data
public class ValidationSqlResult  implements Serializable {
    private List<DbColumn> showErrorColumns;
    List<JSONObject> errorList;
    List<JSONObject> dataList;


    /**
     * 分页参数（可选）
     */
    private Integer pageNum;

    private Integer pageSize;



    /**
     * 偏移量（从第几行开始），自动计算
     */
    private Integer offset;

    /**
     * 限制行数（每页大小），自动设置
     */
    private Integer limit;
}
