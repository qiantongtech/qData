/* Layout */
import Layout from '@/layout/index.vue';
export default [

    {
        path: '/dp/model/detail',
        component: Layout,
        redirect: 'detail',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/dp/model/detail/index.vue'),
                name: 'modelDetail',
                meta: { title: '逻辑模型详情', activeMenu: '/dp/model' }
            }
        ]
    },
];
