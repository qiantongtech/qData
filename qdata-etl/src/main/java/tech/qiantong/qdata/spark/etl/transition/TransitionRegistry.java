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
