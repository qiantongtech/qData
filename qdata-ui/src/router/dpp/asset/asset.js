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

/* Layout */
import Layout from '@/layout/index.vue';

// 数据资产模块动公共路由
export default [
    {
        path: '/dpp/asset/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/asset/detail/index.vue'),
                name: 'dppDaAssetDetail',
                meta: { title: '数据资产详情', activeMenu: '/dpp/asset', lang: 'public.dataAssetDetail' }
            }
        ]
    },
    {
        path: '/da/asset/addAsset/:assetType?',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/asset/addAsset/index.vue'),
                name: 'dppDaAssetAdd',
                meta: { title: '新增数据资产', activeMenu: '/da/asset', lang: 'public.addDataAsset' }
            }
        ]
    },

];
