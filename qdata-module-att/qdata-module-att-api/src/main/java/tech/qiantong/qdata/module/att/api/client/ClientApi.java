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

package tech.qiantong.qdata.module.att.api.client;

import tech.qiantong.qdata.module.att.api.client.dto.AttClientRespDTO;

/**
 * 应用 API 接口
 *
 * @author Ming
 */
public interface ClientApi {

    /**
     * 获得应用信息
     *
     * @param id 应用编号
     * @return 应用信息
     */
    AttClientRespDTO getClient(Long id);
}
