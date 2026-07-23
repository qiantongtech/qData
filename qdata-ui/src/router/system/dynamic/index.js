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

// System module dynamic routing, dynamic loading based on user permissions
export default [
  {
    path: '/system/user-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:user:edit'],
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/views/sys/system/user/authRole.vue'),
        name: 'AuthRole',
        meta: { title: 'Assign Roles', activeMenu: '/system/user', lang: 'public.assignRole' }
      }
    ]
  },
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: () => import('@/views/sys/system/role/authUser.vue'),
        name: 'AuthUser',
        meta: { title: 'Assign Users', activeMenu: '/system/role', lang: 'public.assignUser' }
      }
    ]
  },
  {
    path: '/system/dict-data',
    component: Layout,
    hidden: true,
    permissions: ['system:dict:list'],
    children: [
      {
        path: 'index/:dictId(\\d+)',
        component: () => import('@/views/sys/system/dict/data.vue'),
        name: 'Data',
        meta: { title: 'Dictionary Data', activeMenu: '/system/dict', lang: 'public.dictionaryData' }
      }
    ]
  },
  {
    path: '/monitor/job-log',
    component: Layout,
    hidden: true,
    permissions: ['monitor:job:list'],
    children: [
      {
        path: 'index/:jobId(\\d+)',
        component: () => import('@/views/sys/monitor/job/log.vue'),
        name: 'JobLog',
        meta: { title: 'Schedule Log', activeMenu: '/monitor/job', lang: 'public.scheduleLog' }
      }
    ]
  },
  {
    path: '/tool/gen-edit',
    component: Layout,
    hidden: true,
    permissions: ['tool:gen:edit'],
    children: [
      {
        path: 'index/:tableId(\\d+)',
        component: () => import('@/views/sys/tool/gen/editTable.vue'),
        name: 'GenEdit',
        meta: { title: 'Edit Generation Configuration', activeMenu: '/tool/gen', lang: 'public.editGenerationConfig' }
      }
    ]
  }
]
