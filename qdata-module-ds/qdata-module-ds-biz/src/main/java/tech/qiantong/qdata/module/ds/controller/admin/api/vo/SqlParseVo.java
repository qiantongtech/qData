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

package tech.qiantong.qdata.module.ds.controller.admin.api.vo;

import lombok.Data;
import tech.qiantong.qdata.module.ds.dal.dataobject.dto.ReqParam;
import tech.qiantong.qdata.module.ds.dal.dataobject.dto.ResParam;

import java.io.Serializable;
import java.util.List;

@Data
public class SqlParseVo implements Serializable {

    private static final long serialVersionUID=1L;

    private List<ReqParam> reqParams;
    private List<ResParam> resParams;
}
