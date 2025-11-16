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

package tech.qiantong.qdata.module.system.ca.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 主体管理对象 ca_subject
 *
 * @author qdata
 * @date 2024-08-18
 */
public class CaSubject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID;主体ID */
    private Long id;

    /** 主体名称;主体名称 */
    @Excel(name = "主体名称;主体名称")
    private String name;

    /** 通用名称;通用名称 */
    @Excel(name = "通用名称;通用名称")
    private String cn;

    /** 组织部门;组织单位名称 */
    @Excel(name = "组织部门;组织单位名称")
    private String ou;

    /** 组织名称 */
    @Excel(name = "组织名称")
    private String o;

    /** 城市名称 */
    @Excel(name = "城市名称")
    private String l;

    /** 省名称 */
    @Excel(name = "省名称")
    private String st;

    /** 国家 */
    @Excel(name = "国家")
    private String c;

    /** 证书;证书 */
    @Excel(name = "证书;证书")
    private String certificate;

    /** 私钥;私钥 */
    @Excel(name = "私钥;私钥")
    private String privateKey;

    /** 是否有效;是否有效 0：无效，1：有效 */
    @Excel(name = "是否有效;是否有效 0：无效，1：有效")
    private Integer validFlag;

    /** 删除标志;删除标志 1：已删除，0：未删除 */
    private Integer delFlag;

    /** 创建人id;创建人id */
    @Excel(name = "创建人id;创建人id")
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
