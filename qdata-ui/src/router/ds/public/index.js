/* Layout */
import Layout from '@/layout/index.vue';

// 数据资产模块动公共路由
export default [
    {
        path: '/ds/api',
        component: Layout,
        redirect: 'api',
        hidden: true,
        children: [
            {
                path: 'dsApiDetail',
                component: () => import('@/views/ds/api/detail/index.vue'),
                name: 'dsApiDetail',
                meta: { title: 'api服务详情', activeMenu: '/ds/api' }
            }
        ]
    },

    {
        path: '/ds/api',
        component: Layout,
        redirect: 'api',
        hidden: true,
        children: [
            {
                path: 'dsApiEdit',
                component: () => import('@/views/ds/api/edit/index.vue'),
                name: 'dsApiEdit',
                meta: { title: 'api服务新增', activeMenu: '/ds/api' }
            }
        ]
    },

    {
        path: '/ds/logDetail',
        component: Layout,
        redirect: 'apiLog',
        hidden: true,
        children: [
            {
                path: 'dsApiLogDetail',
                component: () => import('@/views/ds/apiLog/detail/index.vue'),
                name: 'dsApiLogDetail',
                meta: { title: 'api日志详情', activeMenu: '/ds/apiLog' }
            }
        ]
    }
];
