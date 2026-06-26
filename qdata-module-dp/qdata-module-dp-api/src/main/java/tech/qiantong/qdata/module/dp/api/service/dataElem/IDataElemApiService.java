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

package tech.qiantong.qdata.module.dp.api.service.dataElem;

/**
 * <P>
 * 用途:数据元Service接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-05 18:03
 **/
public interface IDataElemApiService {
    /**
     * 根据类目编码查询数量
     *
     * @return
     */
    Long getCountByCatCode(String catCode);
}
