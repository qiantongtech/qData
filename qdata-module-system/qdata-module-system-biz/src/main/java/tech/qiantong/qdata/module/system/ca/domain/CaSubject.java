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

package tech.qiantong.qdata.module.system.ca.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Subject management domain object ca_subject
 *
 * @author qdata
 * @date 2024-08-18
 */
public class CaSubject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID; subject ID */
    private Long id;

    /** subject name */
    @Excel(name = "subject name")
    private String name;

    /** common name */
    @Excel(name = "common name")
    private String cn;

    /** organizational unit */
    @Excel(name = "organizational unit")
    private String ou;

    /** organization name */
    @Excel(name = "organization name")
    private String o;

    /** locality */
    @Excel(name = "locality")
    private String l;

    /** state or province */
    @Excel(name = "state or province")
    private String st;

    /** country */
    @Excel(name = "country")
    private String c;

    /** certificate */
    @Excel(name = "certificate")
    private String certificate;

    /** private key */
    @Excel(name = "private key")
    private String privateKey;

    /** validity flag: 0=invalid, 1=valid */
    @Excel(name = "validity flag")
    private Integer validFlag;

    /** deletion flag: 1=deleted, 0=not deleted */
    private Integer delFlag;

    /** creator ID */
    @Excel(name = "creator ID")
    private Long creatorId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setCn(String cn)
    {
        this.cn = cn;
    }

    public String getCn()
    {
        return cn;
    }
    public void setOu(String ou)
    {
        this.ou = ou;
    }

    public String getOu()
    {
        return ou;
    }
    public void setO(String o)
    {
        this.o = o;
    }

    public String getO()
    {
        return o;
    }
    public void setL(String l)
    {
        this.l = l;
    }

    public String getL()
    {
        return l;
    }
    public void setSt(String st)
    {
        this.st = st;
    }

    public String getSt()
    {
        return st;
    }
    public void setC(String c)
    {
        this.c = c;
    }

    public String getC()
    {
        return c;
    }
    public void setCertificate(String certificate)
    {
        this.certificate = certificate;
    }

    public String getCertificate()
    {
        return certificate;
    }
    public void setPrivateKey(String privateKey)
    {
        this.privateKey = privateKey;
    }

    public String getPrivateKey()
    {
        return privateKey;
    }
    public void setValidFlag(Integer validFlag)
    {
        this.validFlag = validFlag;
    }

    public Integer getValidFlag()
    {
        return validFlag;
    }
    public void setDelFlag(Integer delFlag)
    {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag()
    {
        return delFlag;
    }
    public void setCreatorId(Long creatorId)
    {
        this.creatorId = creatorId;
    }

    public Long getCreatorId()
    {
        return creatorId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("cn", getCn())
            .append("ou", getOu())
            .append("o", getO())
            .append("l", getL())
            .append("st", getSt())
            .append("c", getC())
            .append("certificate", getCertificate())
            .append("privateKey", getPrivateKey())
            .append("validFlag", getValidFlag())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("creatorId", getCreatorId())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
