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

/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 * See the LICENSE file in the project root for full license information.
 */

import Layout from '@/layout/index.vue';

export default [
    {
        path: '/dp/dataCodeTable/dict/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/dataCodeTable/detail/dict'),
                name: 'DataCodeTableDetail',
                meta: {
                    title: 'Data Code Table Details',
                    activeMenu: '/dp/dataCodeTable',
                    lang: 'public.dataElementDetail'
                }
            }
        ]
    },
    {
        path: '/dp/dataCodeTable/column/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/dataCodeTable/detail/column'),
                name: 'DataCodeTableColumnDetail',
                meta: {
                    title: 'Data Code Table Details',
                    activeMenu: '/dp/dataCodeTable',
                    lang: 'public.dataElementDetail'
                }
            }
        ]
    }
];
