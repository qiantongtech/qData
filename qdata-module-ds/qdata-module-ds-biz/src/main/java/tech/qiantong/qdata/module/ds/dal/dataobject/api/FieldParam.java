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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.ds.dal.dataobject.api;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldParam implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据长度
     */
    private Long dataLength;

    /**
     * 数据精度
     */
    private Long dataPrecision;

    /**
     * 数据小数位
     */
    private Long dataScale;

    /**
     * 是否主键
     */
    private String columnKey;

    /**
     * 是否允许为空
     */
    private String columnNullable;

    /**
     * 列的序号
     */
    private Long columnPosition;

    /**
     * 列默认值
     */
    private String dataDefault;

    /**
     * 列注释
     */
    private String columnComment;

    /**
     * 作为请求参数
     */
    private String reqable;

    /**
     * 作为返回参数
     */
    private String resable;
}
