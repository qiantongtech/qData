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
 */

package tech.qiantong.qdata.module.system.domain.dto;

import lombok.Data;

/**
 * 系统配置 DTO 对象 system_content
 *
 * @author qdata
 * @date 2024-12-31
 */
@Data
public class SystemContentRespDTO {

    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 系统名称 */
    private String sysName;

    /** logo */
    private String logo;

    /** 轮播图 */
    private String carouselImage;

    /** 联系电话 */
    private String contactNumber;

    /** 电子邮箱 */
    private String email;

    /** 版权方 */
    private String copyright;

    /** 备案号 */
    private String recordNumber;

    /** 删除标记 */
    private Boolean delFlag;

    /** 状态 */
    private Integer status;

    /** 备注 */
    private String remarks;


}
