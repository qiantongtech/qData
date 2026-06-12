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
                meta: { title: '逻辑模型详情', activeMenu: '/dm/model/create', lang: 'public.logicalModelDetail' }
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
                meta: { title: '发布模型详情', activeMenu: '/dm/model/materializedModel', lang: 'public.publishedModelDetail' }
            }
        ]
    },
];
