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
import Layout from '@/layout/index.vue'

// Data quality module activates public routing
export default [
    {
        path: '/da/quality/qualityTask/add',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/da/quality/qualityTask/add/add.vue'),
                name: 'qualityTaskAdd',
                meta: { title: '数据质量新增', activeMenu: '/da/quality/qualityTask', lang: 'public.dataQualityAdd' }
            }
        ]
    },
    {
        path: '/da/quality/qualityTask/edit',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/da/quality/qualityTask/add/add.vue'),
                name: 'qualityTaskEdit',
                meta: { title: '数据质量配置', activeMenu: '/da/quality/qualityTask', lang: 'public.dataQualityConfig' }
            },
        ]
    },
    {
        path: '/da/quality/qualityTask/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/da/quality/qualityTask/add/add.vue'),
                name: 'qualityTaskDetail',
                meta: { title: '数据质量详情', activeMenu: '/da/quality/qualityTask', lang: 'public.dataQualityDetail' }
            }
        ]
    },

    {
        path: '/da/quality/qualityTaskLog/detail',
        component: Layout,
        redirect: 'detail',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/da/quality/qualityTaskLog/detail/index.vue'),
                name: 'qualityTaskLogDetail',
                meta: { title: '质量任务日志详情', activeMenu: '/da/quality/qualityTaskLog', lang: 'public.qualityTaskLogDetail' }
            }
        ]
    }



]
