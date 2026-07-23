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

// Example module dynamic routing, dynamic loading based on user permissions
export default [
    {
        path: '/da',
        component: Layout,
        hidden: true,
        children: [
            {
                path: 'tag/detail',
                component: () => import('@/views/att/tag/detail/index.vue'),
                name: 'tagDetail',
                meta: { title: 'Tag Details', activeMenu: '/da/tag', lang: 'public.tagDetail' }
            }
        ]
    },
];
