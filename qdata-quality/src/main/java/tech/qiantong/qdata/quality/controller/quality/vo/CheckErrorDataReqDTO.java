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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.quality.dal.dataobject.quality.CheckErrorData;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckErrorDataReqDTO implements Serializable {

    /**
     * 报告id
     */
    private String reportId;

    private String id;

    private List<String> errorDataId;

    /**
     * 1-修改数据
     * 2-修改备注
     * 3-修改状态（忽略）
     */
    private String updateType;
    private String remark;

    private Integer pageNum;
    private Integer pageSize;


    private Integer repair;

    private Map<String,Object> updatedData;

    private Map<String,Object> oldData;

    private Long datasourceId;
    private String tableName;
    private String dataIssueId;

    /**
     * 查询参数
     */
    private Map<String,Object> keyWordData;

}
