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
