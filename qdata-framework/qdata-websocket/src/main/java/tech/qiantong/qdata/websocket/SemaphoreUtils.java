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

package tech.qiantong.qdata.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

/**
 * Semaphore utility methods
 *
 * @author qdata
 */
public class SemaphoreUtils
{
    /**
     * SemaphoreUtils logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SemaphoreUtils.class);

    /**
     * Acquire semaphore
     *
     * @param semaphore
     * @return
     */
    public static boolean tryAcquire(Semaphore semaphore)
    {
        boolean flag = false;

        try
        {
            flag = semaphore.tryAcquire();
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to acquire semaphore", e);
        }

        return flag;
    }

    /**
     * Release semaphore
     *
     * @param semaphore
     */
    public static void release(Semaphore semaphore)
    {

        try
        {
            semaphore.release();
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to release semaphore", e);
        }
    }
}
