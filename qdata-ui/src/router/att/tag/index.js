/* Layout */
import Layout from '@/layout/index.vue';

// 示例模块动态路由，基于用户权限动态去加载
export default [
    {
        path: '/att',
        component: Layout,
        hidden: true,
        children: [
            {
                path: 'tag/detail',
                component: () => import('@/views/att/tag/detail/index.vue'),
                name: 'tagDetail',
                meta: { title: '标签详情', activeMenu: '/att/tag' }
            }
        ]
    },
];
