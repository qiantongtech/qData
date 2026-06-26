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
