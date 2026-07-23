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

package tech.qiantong.qdata.module.ds.dal.dataobject.api;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.ds.dal.dataobject.dto.ReqParam;
import tech.qiantong.qdata.module.ds.dal.dataobject.dto.ResParam;

import java.util.List;

/**
 * API service DO DS_API
 *
 * @author lhs
 * @date 2025-02-12
 */
@Data
@TableName(value = "DS_API")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, and H2; optional for databases such as MySQL.
// @KeySequence("DS_API_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DsApiDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Category ID */
    private Long catId;

    /** Category code */
    private String catCode;

    /** Category name */
    @TableField(exist = false)
    private String catName;

    /** API service name */
    private String name;

    /** API version */
    private String apiVersion;

    /** API path */
    private String apiUrl;

    /** Request method */
    private String reqMethod;

    /** Service provider type */
    private String apiServiceType;

    /** Result type
     * 1: detail, 2: list, 3: paginated
     * */
    private String resDataType;

    /** Multiple IP blacklist entries, separated by commas */
    private String denyIp;

    /** Execution configuration JSON */
    private String configJson;

    /** Rate-limit configuration JSON */
    private String limitJson;

    /**
     *Forwarding type (1: API, 2: geospatial data)
     */
    private String transmitType;

    /**
     *apiId
     */
    private String apiId;

    /**
     *Header configuration JSON
     */
    private String headerJson;

    /** Request parameters */
    @TableField(value = "REQ_PARAMS", exist = false, typeHandler = JacksonTypeHandler.class)
    private List<ReqParam> reqParamsList;

    /** Response parameters */
    @TableField(value = "RES_PARAMS",exist = false, typeHandler = JacksonTypeHandler.class)
    private List<ResParam> resParamsList;

    private String resParams;

    private String reqParams;

    /** Description */
    private String description;

    /** Status */
    private String status;

    /** Whether the record is active */
    private Boolean validFlag;

    /** Deletion flag */
    @TableLogic
    private Boolean delFlag;


    @TableField(exist = false)
    private ExecuteConfig executeConfig;


    /**
     * Whether caching is enabled (0: no, 1: yes)
     */
    @TableField(exist = false)
    private String cacheSwitch;


    //Convert resParams and reqParams into reqParamsList and resParamsList.
    public  void setResParamsList() {
        if (this.resParams != null) {
            this.resParamsList = JSONArray.parseArray(this.resParams, ResParam.class);
        }
        if (this.reqParams != null) {
            this.reqParamsList = JSONArray.parseArray(this.reqParams, ReqParam.class);
        }
    }

}
