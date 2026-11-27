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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import { useI18n } from 'vue-i18n';

function useDefaultLang() {
    const i18n = useI18n();

    const interpolate = (str, params) => {
        if (!params || !str) return str;
        return str.replace(/\{(\w+)\}/g, (match, key) => {
            return params[key] !== undefined ? params[key] : match;
        });
    };

    const td = (key, def, params) => {
        // Use try-catch to ensure that no error is reported even if i18n is not initialized correctly
        try {
            // Use te() to check whether key exists, and return the default value if it does not exist
            const result = i18n.te(key) ? i18n.t(key, params) : def;
            return result;
        } catch (e) {
            return interpolate(def, params);
        }
    };

    return {
        ...i18n,
        td
    };
}

export default useDefaultLang;
