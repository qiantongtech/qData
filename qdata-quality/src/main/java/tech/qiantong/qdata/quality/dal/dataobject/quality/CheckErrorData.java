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

package tech.qiantong.qdata.quality.dal.dataobject.quality;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("quality_error_data")
public class CheckErrorData implements Serializable {
    @Id
    private String id;

    /**
     * 报告id
     */
    private String reportId;


    /**
     * 错误数据数量
     */
    @Field("count")
    private Integer count;


    /**
     * 错误数据数量
     */
    @Field("error_count")
    private Integer errorCount;


    /**
     * 错误数据json列表
     */
    @Field("data_json")
    private String dataJsonStr;

    /**
     * 核查时间
     */
    @Field("time")
    private Date time;

    /**
     * 错误数据json列表
     */
    @Field("json_data")
    private JSONObject jsonData;

    /**
     * 错误数据json列表
     */
    @Field("json_data_old")
    private JSONObject jsonDataOld;
    /**
     * 错误数据json列表
     */
    @Field("data_json_old")
    private String dataJsonStrOLd;

    /**
     * 是否已修复 0:否 1:是 2:忽略
     */
    @Field("repair")
    private Integer repair;


    /**
     * 备注
     */
    @Field("remark")
    private String remark;

}
