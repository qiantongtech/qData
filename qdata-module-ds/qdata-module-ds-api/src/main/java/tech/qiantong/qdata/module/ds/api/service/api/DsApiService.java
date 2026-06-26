/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
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
