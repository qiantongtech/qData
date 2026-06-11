/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

import defaultSettings from '@/settings'
import {i18n} from '@/plugins/vueI18n'
import useSettingsStore from '@/store/system/settings'

/**
 * 动态修改标题
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