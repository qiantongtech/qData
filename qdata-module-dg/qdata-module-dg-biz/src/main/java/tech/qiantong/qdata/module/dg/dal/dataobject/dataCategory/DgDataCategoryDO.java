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

package tech.qiantong.qdata.module.dg.dal.dataobject.dataCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据分类 DO 对象 DG_DATA_CATEGORY
 *
 * @author qdata
 * @date 2026-04-07
 */
@Data
@TableName(value = "DG_DATA_CATEGORY")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DG_DATA_CATEGORY_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DgDataCategoryDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 类目id
     */
    private Long catId;

    /**
     * 类目编码
     */
    private String catCode;

    /**
     * 分类名称
     */
    private String name;

    /** 分类名称缩写名 */
    private String shortName;

    /**
     * 数据分级
     */
    private Long dataLevelId;

    /**
     * 任务优先级;HIGHEST,HIGH,MEDIUM,LOW,LOWEST
     */
    private String priority;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否有效;0：无效，1：有效
     */
    private Boolean validFlag;

    /**
     * 删除标志;1：已删除，0：未删除
     */
    @TableLogic
    private Boolean delFlag;

    /**
     * 类目名称
     */
    @TableField(exist = false)
    private String catName;

    /**
     * 数据分级缩写
     */
    @TableField(exist = false)
    private String dataLevelShortName;

    /**
     * 脱敏配置（0:否 1:是）
     */
    @TableField(exist = false)
    private String desensitizationRulesFlag;

    @TableField(exist = false)
    @Schema(description = "脱敏规则id")
    private Long desensitizationRulesId;

}
