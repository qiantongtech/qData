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

/* Layout */
import Layout from '@/layout/index.vue';

// 数据资研发模块公共路由
export default [

    {
        path: '/dpp/task/developTask/edit',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/task/developTask/detail/index.vue'),
                name: 'developTaskEdit',
                meta: { title: '数据开发配置转换', activeMenu: '/dpp/task/developTask', lang: 'public.dataDevelopmentConfigTransform' }
            },
        ]
    },
    {
        path: '/dpp/task/developTask/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/task/developTask/detail/index.vue'),
                name: 'developTaskDetail',
                meta: { title: '数据开发详情', activeMenu: '/dpp/task/developTask', lang: 'public.dataDevelopmentDetail' }
            }
        ]
    },
];
