package tech.qiantong.qdata.module.system.ca.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 证书管理对象 ca_cert
 *
 * @author qdata
 * @date 2024-08-18
 */
public class CaCert extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID;ID */
    private Long id;

    /** 证书名称;证书名称 */
    @Excel(name = "证书名称")
    private String name;

    /** 主体id;主体id */
    @Excel(name = "主体id")
    private Long subjectId;

    /** 主体名称;主体名称 */
    @Excel(name = "主体名称")
    private String subjectName;

    /** 证书;证书 */
    @Excel(name = "证书")
    private String certificate;

    /** 私钥;私钥 */
    @Excel(name = "私钥")
    private String privateKey;

    /** 颁发者;颁发者 */
    @Excel(name = "颁发者")
    private String issuer;

    /** 所有者;所有者 */
    @Excel(name = "所有者")
    private String possessor;

    /** 有效期;有效期 */
    @Excel(name = "有效期")
    private String validTime;

    /** 是否有效;是否有效 0：无效，1：有效 */
    @Excel(name = "是否有效 0：无效，1：有效")
    private Integer validFlag;

    /** 删除标志;删除标志 1：已删除，0：未删除 */
    private Integer delFlag;

    /** 创建人id;创建人id */
    @Excel(name = "创建人id")
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
