/* Layout */
import Layout from '@/layout/index.vue';

// 数据资研发模块公共路由
export default [


    {
        path: '/dpp/job/add',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/job/add/index.vue'),
                name: 'jobAdd',
                meta: { title: '作业管理新增', activeMenu: '/dpp/job' }
            }
        ]
    },
    {
        path: '/dpp/job/edit',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/job/add/index.vue'),
                name: 'jobEdit',
                meta: { title: '作业管理配置作业', activeMenu: '/dpp/job' }
            },
        ]
    },
    {
        path: '/dpp/job/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/job/add/index.vue'),
                name: 'jobDetail',
                meta: { title: '作业管理详情', activeMenu: '/dpp/job' }
            }
        ]
    },



];
