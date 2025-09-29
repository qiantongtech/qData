/* Layout */
import Layout from '@/layout/index.vue';

// 数据资产模块动公共路由
export default [
    {
        path: '/dpp/asset/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/asset/detail/index.vue'),
                name: 'dppDaAssetDetail',
                meta: { title: '数据资产详情', activeMenu: '/dpp/asset' }
            }
        ]
    },

];
