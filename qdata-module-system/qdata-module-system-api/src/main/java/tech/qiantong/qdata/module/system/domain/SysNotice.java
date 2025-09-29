package tech.qiantong.qdata.module.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.xss.Xss;

/**
 * 通知公告表 sys_notice
 *
 * @author qdata
 */
public class SysNotice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    private Long noticeId;

    /** 公告标题 */
    private String noticeTitle;

    /** 公告类型（1通知 2公告） */
    private String noticeType;

    /** 公告内容 */
    private String noticeContent;

    /** 公告状态（0正常 1关闭） */
    private String status;

    /** 是否置顶（0否 1是） */
    private Integer topFlag;

    /** 是否弹窗（0否 1是） */
    private Integer alertFlag;

    /** 开始弹窗时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alertStartTime;

    /** 结束弹窗时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alertEndTime;

    /** 公告内容文本 */
    private String noticeContentText;

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    @Xss(message = "公告标题不能包含脚本字符")
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTopFlag() {
        return topFlag;
    }

    public void setTopFlag(Integer topFlag) {
        this.topFlag = topFlag;
    }

    public Integer getAlertFlag() {
        return alertFlag;
    }

    public void setAlertFlag(Integer alertFlag) {
        this.alertFlag = alertFlag;
    }

    public Date getAlertStartTime() {
        return alertStartTime;
    }

    public void setAlertStartTime(Date alertStartTime) {
        this.alertStartTime = alertStartTime;
    }

    public Date getAlertEndTime() {
        return alertEndTime;
    }

    public void setAlertEndTime(Date alertEndTime) {
        this.alertEndTime = alertEndTime;
    }

    public String getNoticeContentText() {
        return noticeContentText;
    }

    public void setNoticeContentText(String noticeContentText) {
        this.noticeContentText = noticeContentText;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("noticeId", getNoticeId())
                .append("noticeTitle", getNoticeTitle())
                .append("noticeType", getNoticeType())
                .append("noticeContent", getNoticeContent())
                .append("status", getStatus())
                .append("topFlag", getTopFlag())
                .append("alertFlag", getAlertFlag())
                .append("alertStartTime", getAlertStartTime())
                .append("alertEndTime", getAlertEndTime())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("noticeContentText", getNoticeContentText())
                .toString();
    }
}
