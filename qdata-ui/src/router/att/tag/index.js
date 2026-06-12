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

// 示例模块动态路由，基于用户权限动态去加载
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
                meta: { title: '标签详情', activeMenu: '/da/tag', lang: 'public.tagDetail' }
            }
        ]
    },
];
