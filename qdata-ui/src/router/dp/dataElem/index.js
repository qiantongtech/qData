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

// 示例模块动公共路由
export default [
    {
        path: '/dp/dataElem/dict/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '', // 使用动态路由参数
                component: () => import('@/views/dp/dataElem/detail/dict'),
                name: 'DataElemCodeDetail',
                meta: {
                    title: '数据元详情',
                    activeMenu: '/dp/dataElem'
                }
            },
        ]
    },
    {
        path: '/dp/dataElem/column/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/dataElem/detail/column'),
                name: 'DataElemDetail',
                meta: {
                    title: '数据元详情',
                    activeMenu: '/dp/dataElem'
                }
            }
        ]
    },
];
