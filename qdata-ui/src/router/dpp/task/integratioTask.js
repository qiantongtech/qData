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

// Data resource R&D module public routing
export default [
    {
        path: '/dpp/task/integratioTask/edit',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/task/integratioTask/edit/index.vue'),
                name: 'integratioTaskEdit',
                meta: { title: 'Configure Data Integration Task', activeMenu: '/dpp/task/integratioTask', lang: 'public.dataIntegrationTaskConfig' }
            },
        ]
    },
    {
        path: '/dpp/task/integratioTask/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/task/integratioTask/detail/index.vue'),
                name: 'integratioTaskDetail',
                meta: { title: 'Data Integration Task Details', activeMenu: '/dpp/task/integratioTask', lang: 'public.dataIntegrationTaskDetail' }
            }
        ]
    },

];
