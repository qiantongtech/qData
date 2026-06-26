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

// 标准
export default [
    {
        path: '/dm/document/national/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/document/detail/index.vue'),
                name: 'national',
                meta: { title: '国家标准详情', activeMenu: '/dm/document/national', lang: 'public.nationalStandardDetail' }
            },
        ]
    },
    {
        path: '/dm/document/industry/detail',
        component: Layout,
        children: [

            {
                path: '',
                component: () => import('@/views/dp/document/detail/index.vue'),
                name: 'industrylocal',
                meta: { title: '行业标准详情', activeMenu: '/dm/document/industry', lang: 'public.industryStandardDetail' }
            },

        ]
    },
    {
        path: '/dm/document/provincial/detail',
        component: Layout,
        children: [


            {
                path: '',
                component: () => import('@/views/dp/document/detail/index.vue'),
                name: 'provincial',
                meta: { title: '地方标准详情', activeMenu: '/dm/document/provincial', lang: 'public.localStandardDetail' }
            },

        ]
    },
    {
        path: '/dm/document/group/detail',
        component: Layout,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/document/detail/index.vue'),
                name: 'groupDetail',
                meta: { title: '团体标准详情', activeMenu: '/dm/document/group', lang: 'public.groupStandardDetail' }
            },

        ]
    },
    {
        path: '/dm/document/search/detail',
        component: Layout,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/document/detail/index.vue'),
                name: 'search',
                meta: { title: '标准检索详情', activeMenu: '/dm/document/search', lang: 'public.standardSearchDetail' }
            },
        ]
    },
]
//     }
// ];
