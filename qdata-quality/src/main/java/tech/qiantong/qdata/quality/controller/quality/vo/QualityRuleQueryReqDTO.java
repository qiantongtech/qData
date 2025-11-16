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

package tech.qiantong.qdata.quality.controller.quality.vo;

import lombok.Data;
import tech.qiantong.qdata.quality.dal.dataobject.datasource.DaDatasourceDO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class QualityRuleQueryReqDTO implements Serializable {


    private String id;

    private String tableName;

    private String ruleColumn;

    private List<String> ruleColumns;

    private List<String> showErrorColumns;

    private String dataId;

    private DaDatasourceDO daDatasourceById;

    private String evaColumn;

    private String whereClause;

    private String ruleType;

    /**
     * 规则配置参数，如时间范围、是否忽略 null、是否忽略大小写等
     */
    private Map<String, Object> config;

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

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        computeLimitAndOffset();
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        computeLimitAndOffset();
    }

    private void computeLimitAndOffset() {
        if (this.pageNum != null && this.pageSize != null) {
            this.offset = (this.pageNum - 1) * this.pageSize;
            this.limit = this.pageSize;
        }
    }
}
