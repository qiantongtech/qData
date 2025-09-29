/* Layout */
import Layout from '@/layout/index.vue';

// 数据资研发模块公共路由
export default [

    {
        path: '/dpp/task/developTask/edit',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/task/developTask/detail/index.vue'),
                name: 'developTaskEdit',
                meta: { title: '数据开发配置转换', activeMenu: '/dpp/task/developTask' }
            },
        ]
    },
    {
        path: '/dpp/task/developTask/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/task/developTask/detail/index.vue'),
                name: 'developTaskDetail',
                meta: { title: '数据开发详情', activeMenu: '/dpp/task/developTask' }
            }
        ]
    },
];
