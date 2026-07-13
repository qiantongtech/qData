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

package tech.qiantong.qdata.redis.constant;


import java.util.Locale;

/**
 * Common constant information
 *
 * @author qdata
 */
public class Constants
{
    /**
     * UTF-8 character set
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK character set
     */
    public static final String GBK = "GBK";

    /**
     * System language
     */
    public static final Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE;

    /**
     * www main domain
     */
    public static final String WWW = "www.";

    /**
     * http request
     */
    public static final String HTTP = "http://";

    /**
     * https request
     */
    public static final String HTTPS = "https://";

    /**
     * Universal success mark
     */
    public static final String SUCCESS = "0";

    /**
     * Common failure flag
     */
    public static final String FAIL = "1";

    /**
     * Login successful
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * Log out
     */
    public static final String LOGOUT = "Logout";

    /**
     * Register
     */
    public static final String REGISTER = "Register";

    /**
     * Login failed
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * All permission identifiers
     */
    public static final String ALL_PERMISSION = "*:*:*";

    /**
     * Administrator role permission ID
     */
    public static final String SUPER_ADMIN = "admin";

    /**
     * Role permission separator
     */
    public static final String ROLE_DELIMETER = ",";

    /**
     * Permission identifier separator
     */
    public static final String PERMISSION_DELIMETER = ",";

    /**
     * Verification code validity period (minutes)
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * Token
     */
    public static final String TOKEN = "token";

    /**
     * Token prefix
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Token prefix
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * User ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * User avatar
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * Creation time
     */
    public static final String JWT_CREATED = "created";

    /**
     * User permissions
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * Resource mapping path prefix
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * RMI remote method invocation
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     * LDAP remote method invocation
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS remote method invocation
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * Automatically identify json object whitelist configuration (only parsed package names are allowed, the smaller the range, the safer)
     */
    public static final String[] JSON_WHITELIST_STR = { "org.springframework", "tech.qiantong" };

    /**
     * Scheduled task whitelist configuration (only the package names that are allowed to be accessed, you can add it yourself if needed)
     */
    public static final String[] JOB_WHITELIST_STR = { "tech.qiantong.quartz.task" };

    /**
     * Characters that violate scheduled tasks
     */
    public static final String[] JOB_ERROR_STR = { "java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "tech.qiantong.common.utils.file", "tech.qiantong.common.config", "tech.qiantong.generator" };
}
