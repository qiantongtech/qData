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
        path: '/dg/safety/desensitizationRules/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '', // 使用动态路由参数
                component: () => import('@/views/dg/safety/desensitizationRules/detail/index.vue'),
                name: 'DataElemCodeDetail',
                meta: {
                    title: '脱敏规则详情',
                    activeMenu: '/dg/safety/desensitizationRules',
                    lang: 'public.desensitizationRuleDetail'
                }
            },
        ]
    },

];
