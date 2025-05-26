/* Layout */
import Layout from '@/layout/index.vue';

// 数据资产模块公共路由
export default [
    {
        path: '/dpp/etl',
        component: Layout,
        redirect: '/dpp/etl/lntegratio',
        hidden: true,
        children: [
            {
                path: 'lntegratio',
                component: () => import('@/views/dpp/etl/lntegratio/index.vue'),
                name: 'editTask',
                meta: { title: '数据集成任务修改', activeMenu: '/dpp/tasker/dppEtlTask' }
            },
            {
                path: 'add',
                component: () => import('@/views/dpp/etl/lntegratio/index.vue'),
                name: 'addTask',
                meta: { title: '数据集成任务新增', activeMenu: '/dpp/tasker/dppEtlTask' }
            },
            {
                path: 'indodeev',
                component: () => import('@/views/dpp/etl/lntegratio/index.vue'),
                name: 'indoTask',
                meta: { title: '数据集成任务详情', activeMenu: '/dpp/tasker/dppEtlTask' }
            }
        ]

    },
    {
        path: '/dpp/tasker/ddv',
        component: Layout,
        redirect: '/dpp/tasker/ddv/lntegratio',
        hidden: true,
        children: [
            {
                path: 'lntegratiosdeev',
                component: () => import('@/views/dpp/tasker/ddv/components/index.vue'),
                name: 'taskereditTask',
                meta: { title: '数据开发修改', activeMenu: '/dpp/tasker/dpptaskerddv' }
            },
            {
                path: 'addsdeev',
                component: () => import('@/views/dpp/tasker/ddv/components/index.vue'),
                name: 'taskeraddTask',
                meta: { title: '数据开发新增', activeMenu: '/dpp/tasker/dpptaskerddv' }
            },
            {
                path: 'indodeev',
                component: () => import('@/views/dpp/tasker/ddv/components/index.vue'),
                name: 'indodeevTask',
                meta: { title: '数据开发详情', activeMenu: '/dpp/tasker/dpptaskerddv' }
            }
        ]
    }
];
