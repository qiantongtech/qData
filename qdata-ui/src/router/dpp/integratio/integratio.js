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

// 数据资研发模块公共路由
export default [
    {
        path: '/dpp/instance/integratio/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/instance/integratioTask/detail/index.vue'),
                name: 'integratioDetail',
                meta: { title: '数据集成任务实例', activeMenu: '/dpp/instance/integratioTask' }
            }
        ]
    }

];
