/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
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
