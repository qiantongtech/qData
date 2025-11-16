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

package tech.qiantong.qdata.common.database.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DbColumn {

    /**
     * 列名
     */
    private String colName;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据长度
     */
    private String dataLength;

    /**
     * 数据精度
     */
    private String dataPrecision;

    /**
     * 数据小数位
     */
    private String dataScale;

    /**
     * 是否主键 true是主键 false不是主键
     */
    private Boolean colKey;

    /**
     * 是否允许为空 true 允许为空 false 不允许为空
     */
    private Boolean nullable;

    /**
     * 列的序号
     */
    private Integer colPosition;

    /**
     * 列默认值
     */
    private String dataDefault;

    /**
     * 列注释
     */
    private String colComment;
}
