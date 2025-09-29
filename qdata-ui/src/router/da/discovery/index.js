/* Layout */
import Layout from '@/layout/index.vue'

// 数据资产模块动公共路由
export default [
    {
        path: '/da/discovery/detail',
        component: Layout,
        redirect: 'detail',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/da/discovery/detail/index.vue'),
                name: 'detail',
                meta: { title: '数据发现详情', activeMenu: '/da/discovery' }
            }
        ]
    },
]
