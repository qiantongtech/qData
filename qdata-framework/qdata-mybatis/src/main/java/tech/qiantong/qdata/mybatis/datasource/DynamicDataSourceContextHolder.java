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

package tech.qiantong.qdata.mybatis.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data source switching processing
 *
 * @author qdata
 */
public class DynamicDataSourceContextHolder
{
    public static final Logger log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    /**
     * Maintain variables using ThreadLocal, which provides an independent copy of the variable for each thread that uses the variable.
     * Therefore, each thread can independently change its own copy without affecting the corresponding copies of other threads.
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * Set data source variables
     */
    public static void setDataSourceType(String dsType)
    {
        log.info("Switching to data source {}", dsType);
        CONTEXT_HOLDER.set(dsType);
    }

    /**
     * Get the variables of the data source
     */
    public static String getDataSourceType()
    {
        return CONTEXT_HOLDER.get();
    }

    /**
     * Clear data source variables
     */
    public static void clearDataSourceType()
    {
        CONTEXT_HOLDER.remove();
    }
}
