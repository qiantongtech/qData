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

package tech.qiantong.qdata.api.ds.api.base;

import lombok.Data;

/**
 * <P>
 * 用途:DS结果VO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 15:58
 **/
@Data
public class DsResultDTO {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 是否失败
     */
    private Boolean failed;

    /**
     * 是否成功
     */
    private Boolean success;
}
