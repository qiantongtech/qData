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

package tech.qiantong.qdata.common.annotation;

import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.enums.OperatorType;

import java.lang.annotation.*;

/**
 * Custom operation logging annotations
 *
 * @author qdata
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    /**
     * Module
     */
    public String title() default "";

    /**
     * Function
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * Operator category
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * Whether to save requested parameters
     */
    public boolean isSaveRequestData() default true;

    /**
     * Whether to save response parameters
     */
    public boolean isSaveResponseData() default true;

    /**
     * Exclude specified request parameters
     */
    public String[] excludeParamNames() default {};
}
