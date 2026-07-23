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

package tech.qiantong.qdata.api.ds.api.service.etl;

import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.DSExecuteDTO;

/**
 * <P>
 * Description: Execution related interfaces
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-27 14:29
 **/
public interface IDsEtlExecutorService {
    /**
     * Execute command
     *
     * @param dsExecuteDTO
     * @param projectCode
     * @return
     */
    DsStatusRespDTO execute(DSExecuteDTO dsExecuteDTO, String projectCode);
}
