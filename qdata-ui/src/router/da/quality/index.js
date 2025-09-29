/* Layout */
import Layout from '@/layout/index.vue'

// 数据质量模块动公共路由
export default [
    {
        path: '/da/quality/qualityTask/add',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/da/quality/qualityTask/add/add.vue'),
                name: 'qualityTaskAdd',
                meta: { title: '数据质量新增', activeMenu: '/da/quality/qualityTask' }
            }
        ]
    },
    {
        path: '/da/quality/qualityTask/edit',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/da/quality/qualityTask/add/add.vue'),
                name: 'qualityTaskEdit',
                meta: { title: '数据质量配置', activeMenu: '/da/quality/qualityTask' }
            },
        ]
    },
    {
        path: '/da/quality/qualityTask/detail',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/da/quality/qualityTask/add/add.vue'),
                name: 'qualityTaskDetail',
                meta: { title: '数据质量详情', activeMenu: '/da/quality/qualityTask' }
            }
        ]
    },

    {
        path: '/da/quality/qualityTaskLog/detail',
        component: Layout,
        redirect: 'detail',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/da/quality/qualityTaskLog/detail/index.vue'),
                name: 'qualityTaskLogDetail',
                meta: { title: '质量任务日志详情', activeMenu: '/da/quality/qualityTaskLog' }
            }
        ]
    }



]
