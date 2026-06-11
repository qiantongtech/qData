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

package tech.qiantong.qdata.spark.etl.transition;

import tech.qiantong.qdata.common.exception.ServiceException;

import java.util.Optional;

/**
 * <P>
 * 用途:转换工厂
 * </p>
 *
 * @author: FXB
 * @create: 2025-06-20 09:10
 **/
public class TransitionFactory {

    private static final TransitionRegistry COMPONENT_ITEM_REGISTRY = new TransitionRegistry();

    public TransitionFactory() {
    }

    public static Transition getTransition(String code) {
        return Optional.ofNullable(COMPONENT_ITEM_REGISTRY.getTransition(code)).orElseThrow(() -> new ServiceException(String.format("%s not supported.", code)));
    }
}
