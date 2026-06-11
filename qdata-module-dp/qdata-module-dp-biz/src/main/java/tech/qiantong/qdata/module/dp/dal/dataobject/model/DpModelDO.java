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

package tech.qiantong.qdata.module.dp.dal.dataobject.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 逻辑模型 DO 对象 DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_MODEL")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DP_MODEL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpModelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

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

    @TableField(exist = false)
    private String catName;

    /**
     * 表类型;1:明细表 2:汇总表 3:维度表 4:应用表
     */
    @Schema(name = "表类型 1:明细表 2:汇总表 3:维度表 4:应用表")
    private String tableType;
    /**
     * 数仓分层id
     */
    @Schema(name = "数仓分层id ")
    private Long dataLayerId;
    /**
     * 业务分类id;只有表类型为非应用表是才有值
     */
    @Schema(name = "业务分类id 只有表类型为非应用表是才有值")
    private Long businessCategoryId;
    /**
     * 业务分类层级编码
     */
    @Schema(name = "业务分类层级编码 ")
    private String businessCategoryCode;
    /**
     * 数据分域id;只有表类型为非应用表是才有值
     */
    @Schema(name = "数据分域id 只有表类型为非应用表是才有值")
    private Long dataDomainId;
    /**
     * 所属主题id（主题规划）;只有表类型为应用表是才有值
     */
    @Schema(name = "所属主题id（主题规划） 只有表类型为应用表是才有值")
    private Long themeDomainId;
    /**
     * 所属主题层级编码
     */
    @Schema(name = "所属主题层级编码 ")
    private String themeDomainCode;
    /**
     * 表名大小写;1：大写 2：小写
     */
    @Schema(name = "表名大小写 1：大写 2：小写")
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
    @TableLogic
    private Boolean delFlag;

    @TableField(exist = false)
    private long columnCount;

    /**
     * 数据源名称
     */
    @TableField(exist = false)
    private String datasourceName;

    /**
     * 数据源类型
     */
    @TableField(exist = false)
    private String datasourceType;

    /**
     * 数据源配置(json字符串)
     */
    @TableField(exist = false)
    private String datasourceConfig;

    /**
     * IP
     */
    @TableField(exist = false)
    private String ip;

    /**
     * 端口号
     */
    @TableField(exist = false)
    private Long port;

    /**
     * 名称
     */
    @TableField(exist = false)
    private String documentName;

    /**
     * 名称
     */
    @TableField(exist = false)
    private String documentCode;

    /**
     * 文件标准类型字段，
     */
    @TableField(exist = false)
    private String documentType;

    @Schema(name = "数仓分层名称")
    @TableField(exist = false)
    private String dataLayerName;

    @Schema(name = "数仓分层英文缩写")
    @TableField(exist = false)
    private String dataLayerEngName;

    @Schema(name = "业务名称")
    @TableField(exist = false)
    private String businessCategoryName;

    @Schema(name = "业务英文缩写")
    @TableField(exist = false)
    private String businessCategoryEngName;

    @Schema(name = "数据分域名称")
    @TableField(exist = false)
    private String dataDomainName;

    @Schema(name = "数据分域英文缩写")
    @TableField(exist = false)
    private String dataDomainEngName;

    @Schema(name = "所属主题名称")
    @TableField(exist = false)
    private String themeDomainName;

    @Schema(name = "所属主题英文缩写")
    @TableField(exist = false)
    private String themeDomainEngName;

    @Schema(name = "发布状态 1:未发布 3:发布成功 4:发布失败")
    @TableField(exist = false)
    private String releaseStatus;

    @Schema(name = "发布发布数据源列表")
    @TableField(exist = false, typeHandler = JacksonTypeHandler.class)
    private String releaseDatabaseList;

    @Schema(name = "创建人联系电话")
    @TableField(exist = false)
    private String createUserPhoneNumber;


    @Schema(name = "修改人联系电话")
    @TableField(exist = false)
    private String updateUserPhoneNumber;

    /**
     * 获取表名（拼接上表命名规范）
     * 规则：
     * 1. 数仓分层 + 业务分类 + 数据分域 + 模型编码（非应用表）
     * 2. 数仓分层 + 所属主题 + 模型编码（应用表）
     * 3. 根据 tableCase 转换为大写(1)或小写(2)
     *
     * @return 拼接后的表名
     */
    public String getTableName() {
        java.util.List<String> parts = new java.util.ArrayList<>();

        // 1. 数仓分层
        if (dataLayerEngName != null && !dataLayerEngName.isEmpty()) {
            parts.add(dataLayerEngName);
        }

        // 2. 根据表类型添加不同的层级
        if ("4".equals(tableType)) {
            // 应用表：添加所属主题
            if (themeDomainEngName != null && !themeDomainEngName.isEmpty()) {
                parts.add(themeDomainEngName);
            }
        } else {
            // 非应用表：添加业务分类和数据分域
            if (businessCategoryEngName != null && !businessCategoryEngName.isEmpty()) {
                parts.add(businessCategoryEngName);
            }
            if (dataDomainEngName != null && !dataDomainEngName.isEmpty()) {
                parts.add(dataDomainEngName);
            }
        }

        // 3. 添加模型编码
        if (modelName != null && !modelName.isEmpty()) {
            parts.add(modelName);
        }

        // 4. 拼接并转换大小写
        String tableName = String.join("_", parts);
        if (tableName != null && !tableName.isEmpty()) {
            // tableCase: 1-大写, 2-小写
            if ("2".equals(tableCase)) {
                return tableName.toLowerCase();
            } else {
                return tableName.toUpperCase();
            }
        }

        return tableName;
    }

}
