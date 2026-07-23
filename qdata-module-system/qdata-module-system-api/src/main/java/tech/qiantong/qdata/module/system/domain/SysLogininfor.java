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
 * System access record table sys_logininfor
 *
 * @author qdata
 */
public class SysLogininfor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "ID", cellType = ColumnType.NUMERIC)
    private Long infoId;

    /** Login User Account */
    @Excel(name = "Username")
    private String userName;

    /** Login Status (0=success, 1=failure) */
    @Excel(name = "Status", readConverterExp = "0=success,1=failure")
    private String status;

    /** Login IP Address */
    @Excel(name = "IP Address")
    private String ipaddr;

    /** Login Location */
    @Excel(name = "Location")
    private String loginLocation;

    /** Browser Type */
    @Excel(name = "Browser")
    private String browser;

    /** Operating System */
    @Excel(name = "OS")
    private String os;

    /** Prompt Message */
    @Excel(name = "Message")
    private String msg;

    /** Access Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Access Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    public Long getInfoId()
    {
        return infoId;
    }

    public void setInfoId(Long infoId)
    {
        this.infoId = infoId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation()
    {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation)
    {
        this.loginLocation = loginLocation;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Date getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Date loginTime)
    {
        this.loginTime = loginTime;
    }
}
