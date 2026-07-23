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

package tech.qiantong.qdata.spark.etl.writer;

import tech.qiantong.qdata.common.exception.ServiceException;

import java.util.Optional;

/**
 * <P>
 * Purpose: Output component factory
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-25 09:35
 **/
public class WriterFactory {
    private static final WriterRegistry COMPONENT_ITEM_REGISTRY = new WriterRegistry();

    public WriterFactory() {
    }

    public static Writer getWriter(String code) {
        return Optional.ofNullable(COMPONENT_ITEM_REGISTRY.getWriter(code)).orElseThrow(() -> new ServiceException(String.format("%s not supported.", code)));
    }
}
