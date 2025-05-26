/* Layout */
import Layout from '@/layout/index.vue';

// 示例模块动态路由，基于用户权限动态去加载
export default [
    {
        path: '/dp/dataElem',
        component: Layout,
        hidden: true,
        children: [
            {
                path: 'dataElemCodeDetail', // 使用动态路由参数
                component: () => import('@/views/dp/dataElem/detail/dataElemCodeDetailDialog.vue'),
                name: 'DataElemCodeDetail',
                meta: {
                    title: '数据元详情',
                    activeMenu: '/dp/dpDataElem'
                }
            },
            {
                path: 'dataElemDetail', // 使用动态路由参数
                component: () => import('@/views/dp/dataElem/detail/dataElemDetailDialog.vue'),
                name: 'DataElemDetail',
                meta: {
                    title: '数据元详情',
                    activeMenu: '/dp/dpDataElem'
                }
            }
        ]
    },
    {
        path: '/dp/model',
        component: Layout,
        redirect: 'model',
        hidden: true,
        children: [
            {
                path: 'dpModelDetail',
                component: () => import('@/views/dp/model/detail/index.vue'),
                name: 'dpModelDetail',
                meta: { title: '逻辑模型详情', activeMenu: '/dp/dpModel' }
            }
        ]
    },

];
