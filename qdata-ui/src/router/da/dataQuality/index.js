/* Layout */
import Layout from '@/layout/index.vue'

// 数据资产模块动公共路由
export default [
    {
        path: '/da/dataQuality/dataQualityTasks/components',
        component: Layout,
        redirect: '/da/dataQuality/dataQualityTasks/components/qualityTask',
        children: [
            {
                path: 'qualityTask',
                name: 'DataQualityTasksList',
                component: () => import('@/views/da/dataQuality/dataQualityTasks/components/qualityTask.vue'),
                meta: { title: '数据质量任务新增', activeMenu: '/da/dataQuality/dataQualityTasks' },
            },
            {
                path: 'qualityTaskEdit',
                name: 'DataQualityTasksEdit',
                component: () => import('@/views/da/dataQuality/dataQualityTasks/components/qualityTask.vue'),
                meta: { title: '数据质量任务编辑', activeMenu: '/da/dataQuality/dataQualityTasks' },
            },
        ],
    },
    {
        path: '/da/dataQuality/dataQualityTaskLogs/detail',
        component: Layout,
        redirect: '/da/dataQuality/dataQualityTaskLogs/detail/info',
        children: [
            {
                path: 'info',
                name: 'dataQualityTaskLogs',
                component: () => import('@/views/da/dataQuality/dataQualityTaskLogs/detail/info.vue'),
                meta: { title: '数据质量任务日志详情', activeMenu: '/da/dataQuality/dataQualityTaskLogs' },
            },
        ],
    },
   

]
