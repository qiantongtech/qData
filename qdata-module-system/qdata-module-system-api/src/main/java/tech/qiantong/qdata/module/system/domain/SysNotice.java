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
 * Notification/Announcement table sys_notice
 *
 * @author qdata
 */
public class SysNotice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Announcement ID */
    private Long noticeId;

    /** Announcement Title */
    private String noticeTitle;

    /** Announcement Type (1=notification, 2=announcement) */
    private String noticeType;

    /** Announcement Content */
    private String noticeContent;

    /** Announcement Status (0=normal, 1=closed) */
    private String status;

    /** Whether to Pin to Top (0=no, 1=yes) */
    private Integer topFlag;

    /** Whether to Show as Popup (0=no, 1=yes) */
    private Integer alertFlag;

    /** Popup Start Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alertStartTime;

    /** Popup End Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alertEndTime;

    /** Plain text of announcement content */
    private String noticeContentText;

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    @Xss(message = "Announcement title must not contain script characters")
    @NotBlank(message = "Announcement title is required")
    @Size(min = 0, max = 50, message = "Announcement title must not exceed 50 characters")
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
