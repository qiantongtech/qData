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

import defaultSettings from '@/settings'
import {i18n} from '@/plugins/vueI18n'
import useSettingsStore from '@/store/system/settings'

/**
 * Dynamically modify title
 */
const title = () => i18n.global.t('common.html.appTitle') || defaultSettings.title;

export function useDynamicTitle() {
    const settingsStore = useSettingsStore();
    if (settingsStore.dynamicTitle) {
        document.title = settingsStore.title ? settingsStore.title + ' - ' + title() : title();
    } else {
        document.title = title();
    }
}