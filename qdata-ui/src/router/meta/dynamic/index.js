/* Layout */
import Layout from '@/layout/index.vue';

export default [
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
                meta: { title: 'Add Table Metadata', activeMenu: '/dg/meta/management', lang: 'public.addTableMetadata' }
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
                meta: { title: 'Edit Table Metadata', activeMenu: '/dg/meta/management', lang: 'public.editTableMetadata' }
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
                meta: { title: 'Table Metadata Details', activeMenu: '/dg/meta/management', lang: 'public.tableMetadataDetail' }
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
                meta: { title: 'Table Metadata Details', activeMenu: '/dg/meta/comparison', lang: 'public.tableMetadataDetail' }
            }
        ]
    },

];
