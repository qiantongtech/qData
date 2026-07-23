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
 * Certificate management domain object ca_cert
 *
 * @author qdata
 * @date 2024-08-18
 */
public class CaCert extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** certificate name */
    @Excel(name = "certificate name")
    private String name;

    /** subject ID */
    @Excel(name = "subject ID")
    private Long subjectId;

    /** subject name */
    @Excel(name = "subject name")
    private String subjectName;

    /** certificate */
    @Excel(name = "certificate")
    private String certificate;

    /** private key */
    @Excel(name = "private key")
    private String privateKey;

    /** issuer */
    @Excel(name = "issuer")
    private String issuer;

    /** possessor */
    @Excel(name = "possessor")
    private String possessor;

    /** validity period */
    @Excel(name = "validity period")
    private String validTime;

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
    public void setSubjectId(Long subjectId)
    {
        this.subjectId = subjectId;
    }

    public Long getSubjectId()
    {
        return subjectId;
    }
    public void setSubjectName(String subjectName)
    {
        this.subjectName = subjectName;
    }

    public String getSubjectName()
    {
        return subjectName;
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
    public void setIssuer(String issuer)
    {
        this.issuer = issuer;
    }

    public String getIssuer()
    {
        return issuer;
    }
    public void setPossessor(String possessor)
    {
        this.possessor = possessor;
    }

    public String getPossessor()
    {
        return possessor;
    }
    public void setValidTime(String validTime)
    {
        this.validTime = validTime;
    }

    public String getValidTime()
    {
        return validTime;
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
            .append("subjectId", getSubjectId())
            .append("subjectName", getSubjectName())
            .append("certificate", getCertificate())
            .append("privateKey", getPrivateKey())
            .append("issuer", getIssuer())
            .append("possessor", getPossessor())
            .append("validTime", getValidTime())
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
