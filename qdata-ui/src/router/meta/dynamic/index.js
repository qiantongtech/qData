/* Layout */
import Layout from '@/layout/index.vue';

export default [
    {
        path: '/meta/unreleased/structured/db/detail',
        component: Layout,
        hidden: true,
        permissions: ['meta:unreleased:structured:db:detail'],
        children: [
            {
                path: '',
                component: () =>
                    import('@/views/meta/unreleased/structured/database/detail/index.vue'),
                name: 'UnreleasedStructuredDatabaseDetail',
                meta: { title: '库元数据详情', activeMenu: '/meta/unreleased/structured/db' }
            }
        ]
    },
    {
        path: '/meta/released/structured/db/detail',
        component: Layout,
        hidden: true,
        permissions: ['meta:released:structured:db:detail'],
        children: [
            {
                path: '',
                component: () =>
                    import('@/views/meta/unreleased/structured/database/detail/index.vue'),
                name: 'ReleasedStructuredDatabaseDetail',
                meta: { title: '库元数据详情', activeMenu: '/meta/released/structured/db' }
            }
        ]
    },
    {
        path: '/dg/meta/management/add',
        component: Layout,
        hidden: true,
        permissions: ['meta:unreleased:structured:table:add'],
        children: [
            {
                path: '',
                component: () =>
                    import('@/views/meta/unreleased/structured/table/handle/index.vue'),
                name: 'UnreleasedStructuredTableAdd',
                meta: { title: '新增表元数据', activeMenu: '/dg/meta/management' }
            }
        ]
    },
    {
        path: '/dg/meta/management/edit',
        component: Layout,
        hidden: true,
        permissions: ['meta:unreleased:structured:table:edit'],
        children: [
            {
                path: '',
                component: () =>
                    import('@/views/meta/unreleased/structured/table/handle/index.vue'),
                name: 'UnreleasedStructuredTableEdit',
                meta: { title: '修改表元数据', activeMenu: '/dg/meta/management' }
            }
        ]
    },

    {
        path: '/dg/meta/management/detail',
        component: Layout,
        hidden: true,
        permissions: ['meta:unreleased:structured:table:detail'],
        children: [
            {
                path: '',
                component: () =>
                    import('@/views/meta/unreleased/structured/table/detail/index.vue'),
                name: 'UnreleasedStructuredTableDetail',
                meta: { title: '表元数据详情', activeMenu: '/dg/meta/management' }
            }
        ]
    },

    {
        path: '/dg/meta/comparison/detail',
        component: Layout,
        hidden: true,
        permissions: ['meta:released:structured:table:detail'],
        children: [
            {
                path: '',
                component: () =>
                    import('@/views/meta/unreleased/structured/table/detail/index.vue'),
                name: 'ReleasedStructuredTableDetail',
                meta: { title: '表元数据详情', activeMenu: '/dg/meta/comparison' }
            }
        ]
    },

    {
        path: '/meta/unreleased/structured/column/add',
        component: Layout,
        hidden: true,
        permissions: ['meta:unreleased:structured:column:add'],
        children: [
            {
                path: '',
                component: () => import('@/views/meta/unreleased/structured/column/add/index.vue'),
                name: 'UnreleasedStructuredColumnAdd',
                meta: { title: '新增字段元数据', activeMenu: '/meta/unreleased/structured/column' }
            }
        ]
    },

    {
        path: '/meta/unreleased/structured/column/detail',
        component: Layout,
        hidden: true,
        permissions: ['meta:unreleased:structured:column:detail'],
        children: [
            {
                path: '',
                component: () =>
                    import('@/views/meta/unreleased/structured/column/detail/index.vue'),
                name: 'UnreleasedStructuredColumnDetail',
                meta: { title: '字段元数据详情', activeMenu: '/meta/unreleased/structured/column' }
            }
        ]
    },

    {
        path: '/meta/released/structured/column/detail',
        component: Layout,
        hidden: true,
        permissions: ['meta:released:structured:column:detail'],
        children: [
            {
                path: '',
                component: () =>
                    import('@/views/meta/unreleased/structured/column/detail/index.vue'),
                name: 'ReleasedStructuredColumnDetail',
                meta: { title: '字段元数据详情', activeMenu: '/meta/released/structured/column' }
            }
        ]
    }
];
