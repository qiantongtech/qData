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

package tech.qiantong.qdata.quartz.util;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * cron expression tool class
 *
 * @author qdata
 *
 */
public class CronUtils
{
    /**
     * Returns a Boolean value representing the validity of a given Cron expression
     *
     * @param cronExpression Cron expression
     * @return boolean whether the expression is valid
     */
    public static boolean isValid(String cronExpression)
    {
        return CronExpression.isValidExpression(cronExpression);
    }

    /**
     * Returns a string value indicating that the message is invalid Cron expression gives validity
     *
     * @param cronExpression Cron expression
     * @return String Returns expression error description when invalid, returns null if valid
     */
    public static String getInvalidMessage(String cronExpression)
    {
        try
        {
            new CronExpression(cronExpression);
            return null;
        }
        catch (ParseException pe)
        {
            return pe.getMessage();
        }
    }

    /**
     * Returns the next execution time based on the given Cron expression
     *
     * @param cronExpression Cron expression
     * @return Date The next Cron expression execution time
     */
    public static Date getNextExecution(String cronExpression)
    {
        try
        {
            CronExpression cron = new CronExpression(cronExpression);
            return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        }
        catch (ParseException e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
