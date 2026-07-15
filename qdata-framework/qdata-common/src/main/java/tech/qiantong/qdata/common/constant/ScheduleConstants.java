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

package tech.qiantong.qdata.common.constant;

/**
 * Handle task-related data and operations.
 *
 * @author qdata
 */
public class ScheduleConstants
{
    /** Handle Quartz scheduling operations. */
    public static final String QUARTZ = "QUARTZ";

    /** Handle DolphinScheduler operations. */
    public static final String DOLPHINSCHEDULER = "DOLPHINSCHEDULER";

    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    public static final String QUARTZ_JOB_NAMESPACE = "_QUARTZ_JOB_";

    /** Implementation details. */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /** Implementation details. */
    public static final String MISFIRE_DEFAULT = "0";

    /** Implementation details. */
    public static final String MISFIRE_IGNORE_MISFIRES = "1";

    /** Implementation details. */
    public static final String MISFIRE_FIRE_AND_PROCEED = "2";

    /** Implementation details. */
    public static final String MISFIRE_DO_NOTHING = "3";

    public enum Status
    {
        /**
         * Implementation details.
         */
        NORMAL("0"),
        /**
         * Implementation details.
         */
        PAUSE("1");

        private String value;

        private Status(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }
}
