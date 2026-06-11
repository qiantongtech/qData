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

package tech.qiantong.qdata.module.ds.api.service.api;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.module.ds.api.api.dto.DsApiReqDTO;
import tech.qiantong.qdata.module.ds.api.api.dto.DsApiRespDTO;
import tech.qiantong.qdata.module.ds.api.apiLog.dto.DsApiLogReqDTO;
import tech.qiantong.qdata.module.ds.api.apiLog.dto.DsApiLogRespDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DsApiService {

    Long getCountByCatCode(String catCode);

}
