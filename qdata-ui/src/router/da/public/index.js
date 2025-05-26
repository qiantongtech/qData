/* Layout */
import Layout from '@/layout/index.vue';

// 数据资产模块动公共路由
export default [
    {
        path: '/da/asset',
        component: Layout,
        redirect: 'asset',
        hidden: true,
        children: [
            {
                path: 'daAssetDetail',
                component: () => import('@/views/da/asset/detail/index.vue'),
                name: 'daDaAssetDetail',
                meta: { title: '资产地图详情', activeMenu: '/da/daAsset' }
            }
        ]
    },
    {
        path: '/dpp/asset',
        component: Layout,
        redirect: 'asset',
        hidden: true,
        children: [
            {
                path: 'daAssetDetail',
                component: () => import('@/views/dpp/asset/detail/index.vue'),
                name: 'dppDaAssetDetail',
                meta: { title: '数据资产详情', activeMenu: '/dpp/dppAsset' }
            }
        ]
    }
];
