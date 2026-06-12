/* Layout */
import Layout from '@/layout/index.vue';

export default [
    {
        path: '/dg/meta/task/detail',
        component: Layout,
        hidden: true,
        permissions: ['mc:task:structured:detail'],
        children: [
            {
                path: '',
                component: () => import('@/views/mc/task/structured/detail/index.vue'),
                name: 'McTaskStructuredDetail',
                meta: { title: '元数据采集详情', activeMenu: '/dg/meta/task', lang: 'public.metadataCollectionDetail' }
            }
        ]
    }
];
