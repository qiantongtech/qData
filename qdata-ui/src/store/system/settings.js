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

import defaultSettings from '@/settings';
import { useDynamicTitle } from '@/utils/dynamicTitle';

const { sideTheme, showSettings, topNav, tagsView, fixedHeader, sidebarLogo, dynamicTitle } =
    defaultSettings;

const storageSetting = JSON.parse(localStorage.getItem('layout-setting')) || '';

const useSettingsStore = defineStore('settings', {
    state: () => ({
        title: '',
        theme: storageSetting.theme || '#2666fb',
        sideTheme: storageSetting.sideTheme || sideTheme,
        showSettings: showSettings,
        topNav: true,
        // topNav: storageSetting.topNav === undefined ? topNav : storageSetting.topNav,
        tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,
        fixedHeader:
            storageSetting.fixedHeader === undefined ? fixedHeader : storageSetting.fixedHeader,
        sidebarLogo:
            storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,
        dynamicTitle:
            storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle
    }),
    actions: {
        // 修改布局设置
        changeSetting(data) {
            const { key, value } = data;
            if (this.hasOwnProperty(key)) {
                this[key] = value;
            }
        },
        // 设置网页标题
        setTitle(title) {
            this.title = title;
            useDynamicTitle();
        }
    }
});

export default useSettingsStore;
