/* Layout */
import Layout from '@/layout/index.vue';

// 数据资产模块动公共路由
export default [
    {
        path: '/da/asset/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dpp/asset/detail/index.vue'),
                name: 'daDaAssetDetail',
                meta: { title: '资产地图详情', activeMenu: '/da/asset' }
            }
        ]
    },
];
