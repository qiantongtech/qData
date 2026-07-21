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
export default [

    {
        path: '/dm/model/detail',
        component: Layout,
        redirect: 'detail',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/model/detail/index.vue'),
                name: 'modelDetail',
                meta: { title: 'Logical Model Details', activeMenu: '/dm/model/create', lang: 'public.logicalModelDetail' }
            }
        ]
    },
    {
        path: '/dm/model/materializedModel/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/model/detail/index.vue'),
                name: 'materializedModelDetail',
                meta: { title: 'Published Model Details', activeMenu: '/dm/model/materializedModel', lang: 'public.publishedModelDetail' }
            }
        ]
    },
];
