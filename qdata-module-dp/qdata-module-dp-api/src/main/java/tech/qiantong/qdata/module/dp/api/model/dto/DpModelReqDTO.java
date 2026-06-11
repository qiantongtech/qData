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

package tech.qiantong.qdata.module.dp.api.model.dto;

import lombok.Data;

/**
 * 逻辑模型 DTO 对象 DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpModelReqDTO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long ID;

    /**
     * 模型编码
     */
    private String modelName;

    /**
     * 模型名称
     */
    private String modelComment;

    /**
     * 类目编码
     */
    private String catCode;

    /**
     * 表类型;1:明细表 2:汇总表 3:维度表 4:应用表
     */
    private String tableType;
    /**
     * 数仓分层id
     */
    private Long dataLayerId;
    /**
     * 业务分类id;只有表类型为非应用表是才有值
     */
    private Long businessCategoryId;
    /**
     * 业务分类层级编码
     */
    private String businessCategoryCode;
    /**
     * 数据分域id;只有表类型为非应用表是才有值
     */
    private Long dataDomainId;
    /**
     * 所属主题id（主题规划）;只有表类型为应用表是才有值
     */
    private Long themeDomainId;
    /**
     * 所属主题层级编码
     */
    private String themeDomainCode;
    /**
     * 表名大小写;1：大写 2：小写
     */
    private String tableCase;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建方式
     */
    private String createType;

    /**
     * 数据源id
     */
    private Long datasourceId;

    private Long documentId;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否有效
     */
    private Boolean validFlag;

    /**
     * 删除标志
     */
    private Boolean delFlag;


}
