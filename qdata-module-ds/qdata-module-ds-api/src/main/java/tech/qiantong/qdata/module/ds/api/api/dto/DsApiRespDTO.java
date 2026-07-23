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

package tech.qiantong.qdata.module.ds.api.api.dto;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * API service DTO DS_API
 *
 * @author lhs
 * @date 2025-02-12
 */
@Data
public class DsApiRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Category ID */
    private Long catId;

    /** Category code */
    private String catCode;

    /** Category name */
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

    private ExecuteConfig executeConfig;

    /** Open properties */
    private String openAttribute;

    /** Information provider */
    private String provider;

    /** Sharing type */
    private String shareType;

    /** Update cycle */
    private String updatePeriod;

    /** Publication time */
    private String createTime;
    /** Update time */
    private String updateTime;
    /** Process status (0: pending approval, 1: approved, 2: rejected, 3: withdrawn, 4: approval error) */
    private String actStatus;
    /** Process business instance ID */
    private String processInstanceId;


    /** Application count */
    private Integer count;
    /**
     * Whether caching is enabled (0: no, 1: yes)
     */
    private String cacheSwitch;

    /** Applicant ID */
    private Long applyId;

    /** Applicant */
    private String applyBy;

    /** Applicant mobile number */
    private String applyByPhone;

    /** Applicant department */
    private String applyByDeptName;

    /** Applicant department collection */
    private List<String> applyByDeptIdList;

    /** Application time */
    private Date applyTime;

    /** Application reason */
    private String applyReason;

    /** Validity period type */
    private String validType;

    /** Validity start time */
    private Date validStartTime;

    /** Validity end time */
    private Date validEndTime;

    /** Data permissions */
    private String authId;

    private String authName;

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
