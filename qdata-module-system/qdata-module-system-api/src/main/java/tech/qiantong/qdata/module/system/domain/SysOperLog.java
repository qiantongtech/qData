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

package tech.qiantong.qdata.module.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.annotation.Excel.ColumnType;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Operation log table oper_log
 *
 * @author qdata
 */
public class SysOperLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Log primary key */
    @Excel(name = "Oper ID", cellType = ColumnType.NUMERIC)
    private Long operId;

    /** Operation Module */
    @Excel(name = "Module")
    private String title;

    /** Business Type (0=other, 1=add, 2=modify, 3=delete) */
    @Excel(name = "Business Type", readConverterExp = "0=other,1=add,2=modify,3=delete,4=authorize,5=export,6=import,7=force logout,8=generate code,9=clear data")
    private Integer businessType;

    /** Business type array */
    private Integer[] businessTypes;

    /** Request Method */
    @Excel(name = "Method")
    private String method;

    /** HTTP Method */
    @Excel(name = "HTTP Method")
    private String requestMethod;

    /** Operator Type (0=other, 1=back-end user, 2=mobile user) */
    @Excel(name = "Operator Type", readConverterExp = "0=other,1=back-end user,2=mobile user")
    private Integer operatorType;

    /** Operator Name */
    @Excel(name = "Operator")
    private String operName;

    /** Department Name */
    @Excel(name = "Department")
    private String deptName;

    /** Request URL */
    @Excel(name = "Request URL")
    private String operUrl;

    /** Operation IP Address */
    @Excel(name = "IP Address")
    private String operIp;

    /** Operation Location */
    @Excel(name = "Location")
    private String operLocation;

    /** Request Parameters */
    @Excel(name = "Request Params")
    private String operParam;

    /** Return Parameters */
    @Excel(name = "Response Params")
    private String jsonResult;

    /** Operation Status (0=normal, 1=exception) */
    @Excel(name = "Status", readConverterExp = "0=normal,1=exception")
    private Integer status;

    /** Error Message */
    @Excel(name = "Error Message")
    private String errorMsg;

    /** Operation Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Operation Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    /** Elapsed Time */
    @Excel(name = "Elapsed Time", suffix = "ms")
    private Long costTime;

    public Long getOperId()
    {
        return operId;
    }

    public void setOperId(Long operId)
    {
        this.operId = operId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Integer getBusinessType()
    {
        return businessType;
    }

    public void setBusinessType(Integer businessType)
    {
        this.businessType = businessType;
    }

    public Integer[] getBusinessTypes()
    {
        return businessTypes;
    }

    public void setBusinessTypes(Integer[] businessTypes)
    {
        this.businessTypes = businessTypes;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    public Integer getOperatorType()
    {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType)
    {
        this.operatorType = operatorType;
    }

    public String getOperName()
    {
        return operName;
    }

    public void setOperName(String operName)
    {
        this.operName = operName;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getOperUrl()
    {
        return operUrl;
    }

    public void setOperUrl(String operUrl)
    {
        this.operUrl = operUrl;
    }

    public String getOperIp()
    {
        return operIp;
    }

    public void setOperIp(String operIp)
    {
        this.operIp = operIp;
    }

    public String getOperLocation()
    {
        return operLocation;
    }

    public void setOperLocation(String operLocation)
    {
        this.operLocation = operLocation;
    }

    public String getOperParam()
    {
        return operParam;
    }

    public void setOperParam(String operParam)
    {
        this.operParam = operParam;
    }

    public String getJsonResult()
    {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult)
    {
        this.jsonResult = jsonResult;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public Date getOperTime()
    {
        return operTime;
    }

    public void setOperTime(Date operTime)
    {
        this.operTime = operTime;
    }

    public Long getCostTime()
    {
        return costTime;
    }

    public void setCostTime(Long costTime)
    {
        this.costTime = costTime;
    }
}
