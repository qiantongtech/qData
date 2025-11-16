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

package tech.qiantong.qdata.module.att.api.client.dto;

import lombok.Data;

/**
 * 应用管理 DTO 对象 ATT_CLIENT
 *
 * @author qdata
 * @date 2025-02-18
 */
@Data
public class AttClientRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 应用名称 */
    private String name;

    /** 应用类型 */
    private String type;

    /** 应用秘钥 */
    private String secret;

    /** 主页地址 */
    private String homepageUrl;

    /** 允许授权的url */
    private String allowUrl;

    /** 同步地址 */
    private String syncUrl;

    /** 应用图标 */
    private String logo;

    /** 应用描述 */
    private String description;

    /** 是否公开 */
    private String publicFlag;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
