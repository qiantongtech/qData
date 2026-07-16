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
 * User constant information
 *
 * @author qdata
 */
public class UserConstants
{
    /**
     * The unique identifier of the system user within the platform
     */
    public static final String SYS_USER = "SYS_USER";

    /** Normal state */
    public static final String NORMAL = "0";

    /** Abnormal status */
    public static final String EXCEPTION = "1";

    /** User ban status */
    public static final String USER_DISABLE = "1";

    /** Character ban status */
    public static final String ROLE_DISABLE = "1";

    /** Department normal status */
    public static final String DEPT_NORMAL = "0";

    /** Department deactivation status */
    public static final String DEPT_DISABLE = "1";

    /** Dictionary normal state */
    public static final String DICT_NORMAL = "0";

    /** Whether it is the system default (yes) */
    public static final String YES = "Y";

    /** Whether the menu is externally linked (yes) */
    public static final String YES_FRAME = "0";

    /** Whether the menu is externally linked (no) */
    public static final String NO_FRAME = "1";

    /** Menu type (directory) */
    public static final String TYPE_DIR = "M";

    /** Menu type (menu) */
    public static final String TYPE_MENU = "C";

    /** Menu type (button) */
    public static final String TYPE_BUTTON = "F";

    /** Layout component identifier */
    public final static String LAYOUT = "Layout";

    /** ParentView component identification */
    public final static String PARENT_VIEW = "ParentView";

    /** InnerLink component identifier */
    public final static String INNER_LINK = "InnerLink";

    /** Verify whether the return identifier is unique */
    public final static boolean UNIQUE = true;
    public final static boolean NOT_UNIQUE = false;

    /**
     * Username length limit
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * Password length limit
     */
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 20;
}
