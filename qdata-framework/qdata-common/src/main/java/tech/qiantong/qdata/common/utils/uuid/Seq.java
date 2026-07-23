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

import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.common.utils.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qdata sequence generation class
 */
public class Seq
{
    // Generic sequence type
    public static final String commSeqType = "COMMON";

    // Upload sequence type
    public static final String uploadSeqType = "UPLOAD";

    // Common interface sequence number
    private static AtomicInteger commSeq = new AtomicInteger(1);

    // Upload interface sequence number
    private static AtomicInteger uploadSeq = new AtomicInteger(1);

    // Machine identification
    private static final String machineCode = "A";

    /**
     * Get universal serial number
     *
     * @return sequence value
     */
    public static String getId()
    {
        return getId(commSeqType);
    }

    /**
     * Default 16-digit serial number yyMMddHHmmss + one-digit machine identification + 3-length circularly increasing string
     *
     * @return sequence value
     */
    public static String getId(String type)
    {
        AtomicInteger atomicInt = commSeq;
        if (uploadSeqType.equals(type))
        {
            atomicInt = uploadSeq;
        }
        return getId(atomicInt, 3);
    }

    /**
     * General interface serial number yyMMddHHmmss + one-bit machine identification + length length circularly increasing string
     *
     * @param atomicInt sequence number
     * @param length numerical length
     * @return sequence value
     */
    public static String getId(AtomicInteger atomicInt, int length)
    {
        String result = DateUtils.dateTimeNow();
        result += machineCode;
        result += getSeq(atomicInt, length);
        return result;
    }

    /**
     * The sequence loop increments the string [1, 10 raised to the power of (length)), left-padding length digits with 0
     *
     * @return sequence value
     */
    private synchronized static String getSeq(AtomicInteger atomicInt, int length)
    {
        // Get the value first and then +1
        int value = atomicInt.getAndIncrement();

        // If the updated value >= 10 raised to the (length) power, it is reset to 1
        int maxSeq = (int) Math.pow(10, length);
        if (atomicInt.get() >= maxSeq)
        {
            atomicInt.set(1);
        }
        // Convert to string, left-padded with 0
        return StringUtils.padl(value, length);
    }
}
