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

// 业务分类模块公共路由
export default [
    {
        path: '/dm/businessCategory/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dm/businessCategory/detail/index.vue'),
                name: 'BusinessLayerDetail',
                meta: {
                    title: '业务分类详情',
                    activeMenu: '/dm/businessCategory',
                    lang: 'public.businessCategoryDetail'
                }
            },
        ]
    },
];
