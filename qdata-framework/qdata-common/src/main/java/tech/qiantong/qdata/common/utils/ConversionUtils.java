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

public class ConversionUtils {

    /**
     * Convert a string to type Long. If the string is empty or cannot be converted, 0L is returned.
     *
     * @param dataLength The string to be converted
     * @return converted Long type value
     */
    public static Long getStringToLong(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0L;
        }
        try {
            return Long.parseLong(dataLength);
        } catch (NumberFormatException e) {
            // If the conversion fails, 0L is returned
            return 0L;
        }
    }

    /**
     * Convert a string to type Integer. Returns 0 if the string is empty or cannot be converted.
     *
     * @param dataLength The string to be converted
     * @return converted Integer type value
     */
    public static Integer getStringToInt(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0;
        }
        try {
            return Integer.parseInt(dataLength);
        } catch (NumberFormatException e) {
            // If the conversion fails, returns 0
            return 0;
        }
    }

    /**
     * Convert a string to type Double. If the string is empty or cannot be converted, 0.0 is returned.
     *
     * @param dataLength The string to be converted
     * @return converted Double type value
     */
    public static Double getStringToDouble(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0.0;
        }
        try {
            return Double.parseDouble(dataLength);
        } catch (NumberFormatException e) {
            // If the conversion fails, 0.0 is returned
            return 0.0;
        }
    }

    /**
     * Convert a string to type Boolean. Returns false if the string is empty or cannot be converted.
     *
     * @param value the string to convert
     * @return converted Boolean type value
     */
    public static Boolean getStringToBoolean(String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }


}
