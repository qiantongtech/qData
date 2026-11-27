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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

/* Layout */
import Layout from '@/layout/index.vue';

export default [
    {
        path: '/dg/meta/management/add',
        component: Layout,
        hidden: true,
        permissions: ['meta:unreleased:structured:table:add'],
        children: [
            {
                path: '',
                component: () =>
                    import('@/views/meta/unreleased/structured/table/handle/index.vue'),
                name: 'UnreleasedStructuredTableAdd',
                meta: { title: 'Add Table Metadata', activeMenu: '/dg/meta/management', lang: 'public.addTableMetadata' }
            }
        ]
    },
    {
        path: '/dg/meta/management/edit',
        component: Layout,
        hidden: true,
        permissions: ['meta:unreleased:structured:table:edit'],
        children: [
            {
                path: '',
                component: () =>
                    import('@/views/meta/unreleased/structured/table/handle/index.vue'),
                name: 'UnreleasedStructuredTableEdit',
                meta: { title: 'Edit Table Metadata', activeMenu: '/dg/meta/management', lang: 'public.editTableMetadata' }
            }
        ]
    },

    {
        path: '/dg/meta/management/detail',
        component: Layout,
        hidden: true,
        permissions: ['meta:unreleased:structured:table:detail'],
        children: [
            {
                path: '',
                component: () =>
                    import('@/views/meta/unreleased/structured/table/detail/index.vue'),
                name: 'UnreleasedStructuredTableDetail',
                meta: { title: 'Table Metadata Details', activeMenu: '/dg/meta/management', lang: 'public.tableMetadataDetail' }
            }
        ]
    },

    {
        path: '/dg/meta/comparison/detail',
        component: Layout,
        hidden: true,
        permissions: ['meta:released:structured:table:detail'],
        children: [
            {
                path: '',
                component: () =>
                    import('@/views/meta/unreleased/structured/table/detail/index.vue'),
                name: 'ReleasedStructuredTableDetail',
                meta: { title: 'Table Metadata Details', activeMenu: '/dg/meta/comparison', lang: 'public.tableMetadataDetail' }
            }
        ]
    },

];
