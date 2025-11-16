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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
