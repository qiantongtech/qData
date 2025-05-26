/* Layout */
import Layout from '@/layout/index.vue';

// 系统模块公共路由
export default [
    {
        path: '/redirect',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('@/views/system/redirect/index.vue')
            }
        ]
    },
    {
        path: '/sso',
        component: () => import('@/views/system/sso'),
        hidden: true
    },
    {
        path: '/login',
        component: () => import('@/views/system/login.vue'),
        hidden: true
    },
    {
        path: '/register',
        component: () => import('@/views/system/register.vue'),
        hidden: true
    },
    {
        path: '/:pathMatch(.*)*',
        component: () => import('@/views/system/error/404.vue'),
        hidden: true
    },
    {
        path: '/401',
        component: () => import('@/views/system/error/401.vue'),
        hidden: true
    },
    {
        path: '',
        component: Layout,
        redirect: '/index',
        children: [
            {
                path: '/index',
                component: () => import('@/views/system/index.vue'),
                name: 'Index',
                meta: { title: '首页', icon: 'dashboard', affix: true }
            }
        ]
    },
    {
        path: '/bases',
        component: Layout,
        redirect: 'message',
        children: [
            {
                path: 'message',
                component: () => import('@/views/system/system/message/index.vue'),
                name: 'Message',
                meta: { title: '我的消息', icon: 'message' },
                hidden: true
            }
        ]
    },
    {
        path: '/user',
        component: Layout,
        hidden: true,
        redirect: 'noredirect',
        children: [
            {
                path: 'profile',
                component: () => import('@/views/system/system/user/profile/index.vue'),
                name: 'Profile',
                meta: { title: '个人中心', icon: 'user' }
            }
        ]
    },
    {
        path: '/system',
        component: Layout,
        hidden: true,
        redirect: 'noredirect',
        children: [
            {
                path: 'user',
                component: () => import('@/views/system/system/user/index.vue'),
                name: 'User',
                meta: { title: '用户管理', icon: 'user' }
            }
        ]
    }
];
