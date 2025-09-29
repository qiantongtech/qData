/* Layout */
import Layout from '@/layout/index.vue';

// 示例模块动公共路由
export default [
    {
        path: '/dp/dataElem/dict/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '', // 使用动态路由参数
                component: () => import('@/views/dp/dataElem/detail/dict'),
                name: 'DataElemCodeDetail',
                meta: {
                    title: '数据元详情',
                    activeMenu: '/dp/dataElem'
                }
            },
        ]
    },
    {
        path: '/dp/dataElem/column/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/dataElem/detail/column'),
                name: 'DataElemDetail',
                meta: {
                    title: '数据元详情',
                    activeMenu: '/dp/dataElem'
                }
            }
        ]
    },
];
