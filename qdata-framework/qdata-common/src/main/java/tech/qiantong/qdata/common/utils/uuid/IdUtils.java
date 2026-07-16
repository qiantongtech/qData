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

package tech.qiantong.qdata.common.utils.uuid;

import java.security.SecureRandom;

/**
 * ID generator tool class
 *
 * @author qdata
 */
public class IdUtils
{
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Get random UUID
     *
     * @return random UUID
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * Simplified UUID, with horizontal lines removed
     *
     * @return Simplified UUID, with horizontal lines removed
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }

    /**
     * Get a random UUID and use ThreadLocalRandom with better performance to generate UUID
     *
     * @return random UUID
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * Simplified UUID, remove the horizontal lines, use ThreadLocalRandom with better performance to generate UUID
     *
     * @return Simplified UUID, with horizontal lines removed
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }



    /**
     * Generate a long integer artificial ID, combining the current timestamp and random number part to ensure high uniqueness
     *
     * @return artificial ID of type long
     */
    public static long generateArtificialId() {
        // Get the current timestamp (milliseconds)
        long timestamp = System.currentTimeMillis();
        // Randomly generate a number from 0-999, making sure it is a three-digit number (if there are less than 3 digits, add 0 on the left, but just add the values directly)
        int randomDigits = RANDOM.nextInt(1000);
        // Expand the timestamp by 1000 times and add a random number to ensure that the ID is a long integer number
        return timestamp * 1000 + randomDigits;
    }
}
