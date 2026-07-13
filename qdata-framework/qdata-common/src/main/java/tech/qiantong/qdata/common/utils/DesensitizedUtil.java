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

package tech.qiantong.qdata.common.utils;

/**
 * Desensitization tools
 *
 * @author qdata
 */
public class DesensitizedUtil
{
    /**
     * All characters in the password are replaced with *, for example: ******
     *
     * @param password password
     * @return Password after desensitization
     */
    public static String password(String password)
    {
        if (StringUtils.isBlank(password))
        {
            return StringUtils.EMPTY;
        }
        return StringUtils.repeat('*', password.length());
    }

    /**
     * Use * in the middle of the license plate. If it is the wrong license plate, it will not be processed.
     *
     * @param carLicense complete license plate number
     * @return the desensitized license plate
     */
    public static String carLicense(String carLicense)
    {
        if (StringUtils.isBlank(carLicense))
        {
            return StringUtils.EMPTY;
        }
        // Ordinary license plate
        if (carLicense.length() == 7)
        {
            carLicense = StringUtils.hide(carLicense, 3, 6);
        }
        else if (carLicense.length() == 8)
        {
            // New energy license plate
            carLicense = StringUtils.hide(carLicense, 3, 7);
        }
        return carLicense;
    }
}
