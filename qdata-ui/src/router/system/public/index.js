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
                component: () => import('@/views/sys/redirect/index.vue')
            }
        ]
    },
    {
        path: '/sso',
        component: () => import('@/views/sys/sso'),
        hidden: true
    },
    {
        path: '/login',
        component: () => import('@/views/sys/login.vue'),
        hidden: true
    },
    {
        path: '/register',
        component: () => import('@/views/sys/register.vue'),
        hidden: true
    },
    {
        path: '/:pathMatch(.*)*',
        component: () => import('@/views/sys/error/404.vue'),
        hidden: true
    },
    {
        path: '/401',
        component: () => import('@/views/sys/error/401.vue'),
        hidden: true
    },
    {
        path: '',
        component: Layout,
        redirect: '/index',
        children: [
            {
                path: '/index',
                component: () => import('@/views/sys/index.vue'),
                name: 'Index',
                meta: { title: '首页', icon: 'dashboard', affix: true }
            }
        ]
    },
    {
        path: '/sys/system/bases/message',
        component: Layout,
        redirect: 'message',
        children: [
            {
                path: '',
                component: () => import('@/views/sys/system/message/index.vue'),
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
                component: () => import('@/views/sys/system/user/profile/index.vue'),
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
                component: () => import('@/views/sys/system/user/index.vue'),
                name: 'User',
                meta: { title: '用户管理', icon: 'user' }
            }
        ]
    },
    {
        path: '/sys/system/notice/detail',
        component: Layout,
        redirect: 'notice',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/sys/system/notice/detail.vue'),
                name: 'noticeDetail',
                meta: { title: '公告详情', activeMenu: '/system/notice' }
            }
        ]
    },
];
