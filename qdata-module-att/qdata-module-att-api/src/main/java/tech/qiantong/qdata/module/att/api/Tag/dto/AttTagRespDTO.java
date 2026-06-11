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

package tech.qiantong.qdata.module.att.api.Tag.dto;

import lombok.Data;

/**
 * 标签管理 DTO 对象 ATT_TAG
 *
 * @author qdata
 * @date 2025-07-11
 */
@Data
public class AttTagRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 类目编码 */
    private String catCode;

    /** 资产数量 */
    private Long aeestCount;

    /** 状态 */
    private String status;

    /** 扩展信息别名 */
    private String allas;

    /** 近义词 */
    private String nearSynonyms;

    /** 同义词 */
    private String synonyms;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
