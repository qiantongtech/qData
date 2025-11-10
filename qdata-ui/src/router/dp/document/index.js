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
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

/* Layout */
import Layout from '@/layout/index.vue';

// 标准
export default [
    {
        path: '/dp/document/national/detail',
        component: Layout,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/document/detail/index.vue'),
                name: 'national',
                meta: { title: '国家标准详情', activeMenu: '/dp/document/national' }
            },
        ]
    },
    {
        path: '/dp/document/industry/detail',
        component: Layout,
        children: [

            {
                path: '',
                component: () => import('@/views/dp/document/detail/index.vue'),
                name: 'industrylocal',
                meta: { title: '行业标准详情', activeMenu: '/dp/document/industry' }
            },

        ]
    },
    {
        path: '/dp/document/provincial/detail',
        component: Layout,
        children: [


            {
                path: '',
                component: () => import('@/views/dp/document/detail/index.vue'),
                name: 'provincial',
                meta: { title: '地方标准详情', activeMenu: '/dp/document/provincial' }
            },

        ]
    },
    {
        path: '/dp/document/group/detail',
        component: Layout,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/document/detail/index.vue'),
                name: 'groupDetail',
                meta: { title: '团体标准详情', activeMenu: '/dp/document/group' }
            },

        ]
    },
    {
        path: '/dp/document/search/detail',
        component: Layout,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/document/detail/index.vue'),
                name: 'search',
                meta: { title: '标准检索详情', activeMenu: '/dp/document/search' }
            },
        ]
    },
]
//     }
// ];
