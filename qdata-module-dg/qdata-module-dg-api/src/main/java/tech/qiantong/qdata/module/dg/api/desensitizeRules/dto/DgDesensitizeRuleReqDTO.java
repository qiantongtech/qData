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

package tech.qiantong.qdata.module.dg.api.desensitizeRules.dto;

import lombok.*;

/**
 * 脱敏规则 DTO 对象 DG_DESENSITIZE_RULE
 *
 * @author qdata
 * @date 2026-04-10
 */
@Data
public class DgDesensitizeRuleReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 分级名称 */
    private String name;

    /** 数据分类ID */
    private Long dataCategoryId;

    /** 应用场景;1：数据资产  2：数据查询  3：数据服务 */
    private String applicationScene;

    /** 脱敏方式;1：底层脱敏  2：展示脱敏 */
    private String maskType;

    /** 替换规则 */
    private String replaceRule;

    /** 替换内容 */
    private String replaceContent;

    /** 排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    private Boolean delFlag;


}
