/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 */

/* Layout */
import Layout from '@/layout/index.vue';

// 数据源管理路由（DA 模块）
export default [
  {
    path: '/da/datasource',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/da/datasource/index.vue'),
        name: 'DaDatasource',
        meta: { title: '数据源管理', activeMenu: '/da/datasource' }
      }
    ]
  },
  {
    path: '/da/datasource/detail',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/da/datasource/detail.vue'),
        name: 'DaDatasourceDetail',
        meta: { title: '数据源详情', activeMenu: '/da/datasource' }
      }
    ]
  }
];

