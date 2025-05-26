/* Layout */
import Layout from '@/layout/index.vue'

// 示例模块动公共路由
export default [
    {
        path: '/example/genStudent',
        component: Layout,
        redirect: 'genStudent',
        hidden: true,
        children: [
            {
                path: 'studentDetail',
                component: () => import('@/views/example/genStudent/detail/index.vue'),
                name: 'studentDetail',
                meta: { title: '学生详情', activeMenu: '/example/student' }
            }
        ]
    },

]
