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

import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * 用途:转换组件注册
 * </p>
 *
 * @author: FXB
 * @create: 2025-06-20 09:10
 **/
public class TransitionRegistry {

    private final Map<String, Transition> transitionMap = new HashMap<>();


    public TransitionRegistry() {
        this.transitionMap.put(TaskComponentTypeEnum.SPARK_CLEAN.getCode(), new CleanTransition());
        this.transitionMap.put(TaskComponentTypeEnum.SORT_RECORD.getCode(), new SortTransition());
        this.transitionMap.put(TaskComponentTypeEnum.FIELD_DERIVATION.getCode(), new FieldDerivationTransition());
        this.transitionMap.put(TaskComponentTypeEnum.DATA_DEDUPLICATION.getCode(), new DataDeduplicationTransition());
        this.transitionMap.put(TaskComponentTypeEnum.VALUE_MAP.getCode(), new ValueMapTransition());
        this.transitionMap.put(TaskComponentTypeEnum.ADD_CONSTANT.getCode(), new AddConstantTransition());
        this.transitionMap.put(TaskComponentTypeEnum.SELECT_FIELDS.getCode(), new SelectFieldsTransition());
    }

    public Transition getTransition(String code) {
        return this.transitionMap.get(code);
    }
}
