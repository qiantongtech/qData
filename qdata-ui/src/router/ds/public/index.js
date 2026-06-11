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

// 数据资产模块动公共路由
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
                meta: { title: 'API服务详情', activeMenu: '/ds/api' }
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
                meta: { title: 'API服务修改', activeMenu: '/ds/api' }
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
                meta: { title: 'API服务新增', activeMenu: '/ds/api' }
            }
        ]
    },

];
