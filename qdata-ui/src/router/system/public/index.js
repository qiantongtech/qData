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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

/* Layout */
import Layout from '@/layout/index.vue';

// 系统模块公共路由
export default [
    {
        path: '/redirect',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('@/views/sys/redirect/index.vue')
            }
        ]
    },
    {
        path: '/sso',
        component: () => import('@/views/sys/sso'),
        hidden: true
    },
    {
        path: '/login',
        component: () => import('@/views/sys/login.vue'),
        hidden: true
    },
    {
        path: '/register',
        component: () => import('@/views/sys/register.vue'),
        hidden: true
    },
    {
        path: '/:pathMatch(.*)*',
        component: () => import('@/views/sys/error/404.vue'),
        hidden: true
    },
    {
        path: '/401',
        component: () => import('@/views/sys/error/401.vue'),
        hidden: true
    },
    {
        path: '',
        component: Layout,
        redirect: '/index',
        children: [
            {
                path: '/index',
                component: () => import('@/views/sys/index.vue'),
                name: 'Index',
                meta: { title: '首页', icon: 'dashboard', affix: true }
            }
        ]
    },
    {
        path: '/sys/system/bases/message',
        component: Layout,
        redirect: 'message',
        children: [
            {
                path: '',
                component: () => import('@/views/sys/system/message/index.vue'),
                name: 'Message',
                meta: { title: '我的消息', icon: 'message' },
                hidden: true
            }
        ]
    },
    {
        path: '/user',
        component: Layout,
        hidden: true,
        redirect: 'noredirect',
        children: [
            {
                path: 'profile',
                component: () => import('@/views/sys/system/user/profile/index.vue'),
                name: 'Profile',
                meta: { title: '个人中心', icon: 'user' }
            }
        ]
    },
    {
        path: '/system',
        component: Layout,
        hidden: true,
        redirect: 'noredirect',
        children: [
            {
                path: 'user',
                component: () => import('@/views/sys/system/user/index.vue'),
                name: 'User',
                meta: { title: '用户管理', icon: 'user' }
            }
        ]
    },
    {
        path: '/sys/system/notice/detail',
        component: Layout,
        redirect: 'notice',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/sys/system/notice/detail.vue'),
                name: 'noticeDetail',
                meta: { title: '公告详情', activeMenu: '/system/notice' }
            }
        ]
    },
];
