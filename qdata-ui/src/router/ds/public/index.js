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

// Data asset module moves public routing
export default [
    {
        path: '/ds/api/detail',
        component: Layout,
        redirect: 'detail',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/ds/api/detail/index.vue'),
                name: 'dsApiDetail',
                meta: { title: 'API服务详情', activeMenu: '/ds/api', lang: 'public.apiServiceDetail' }
            }
        ]
    },

    {
        path: '/ds/api/edit',
        component: Layout,
        redirect: 'edit',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/ds/api/edit/index.vue'),
                name: 'dsApiEdit',
                meta: { title: 'API服务修改', activeMenu: '/ds/api', lang: 'public.apiServiceEdit' }
            }
        ]
    },
    {
        path: '/ds/api/add',
        component: Layout,
        redirect: 'add',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/ds/api/edit/index.vue'),
                name: 'dsApiAdd',
                meta: { title: 'API服务新增', activeMenu: '/ds/api', lang: 'public.apiServiceAdd' }
            }
        ]
    },

];
