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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.service.task.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbSaveReqVO;
import tech.qiantong.qdata.module.mc.service.metadata.IMcDbService;

import javax.annotation.Resource;

@Service
public class McDbTxService {

    @Resource
    @Lazy
    private IMcDbService mcDbApiService;

    /**
     * Library-level metadata: independent transaction, commit immediately
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long createDbAndCommit(McDbSaveReqVO dbScope) {
        try {
            return mcDbApiService.createMcDb(dbScope);
        } catch (Exception e) {
            // Only affects the current library and does not throw it out.
            return null;
        }
    }
}
