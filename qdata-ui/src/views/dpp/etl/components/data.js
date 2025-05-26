// treeData.js
export const treeData = [
    {
        label: '输入',
        level: 1,
        type: 1,
        children: [
            {
                label: '表输入组件',
                key: 'input-table',
                type: 1,
                level: 2,
                taskType: 'DATAX',
                componentType: '1',
                icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
                icons: '@/assets/system/images/dpp/sr.png'
            },
            {
                label: 'Excel文件输入组件',
                key: 'input-excel',
                type: 1,
                level: 2,
                componentType: '2',
                taskType: 'DATAX',
                icon: new URL('@/assets/system/images/dpp/excel.png', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/excel.png'
            },
            {
                label: 'csv输入组件',
                key: 'input-csv',
                type: 1,
                level: 2,
                componentType: '4',
                taskType: 'DATAX',
                icon: new URL('@/assets/system/images/dpp/csv.png', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/csv.png'
            },
            {
                label: 'Kafka输入组件',
                key: 'input-Kafka',
                type: 1,
                level: 2,
                componentType: '3',
                taskType: 'DATAX',
                icon: new URL('@/assets/system/images/dpp/kafka.png', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/kafka.png'
            },

            // {
            //     label: 'Sql输入组件',
            //     key: 'input-sql',
            //     type: 1,
            //     level: 2,
            //     taskType: 'DATAX'
            // },
            {
                label: '数据库输入组件',
                key: 'input-database',
                type: 1,
                level: 2,
                taskType: 'DATAX'
            },
            {
                label: 'MongoDB输入组件',
                key: 'input-mongodb',
                type: 1,
                level: 2,
                taskType: 'DATAX'
            },
            {
                label: 'Redis输入组件',
                key: 'input-redis',
                type: 1,
                level: 2,
                taskType: 'DATAX'
            },
            {
                label: 'Api输入组件',
                key: 'input-api',
                type: 1,
                level: 2,
                taskType: 'DATAX'
            }
        ]
    },
    {
        label: '转换',
        type: 3,
        level: 1,
        children: [
            {
                label: '转换组件',
                key: 'transform-desensitization',
                type: 3,
                level: 2,
                taskType: 'SPARK',
                componentType: '31',
                icon: new URL('@/assets/system/images/dpp/zh.png', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/zh.png'
            },
            {
                label: '清洗规则组件',
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'SPARK'
            },
            {
                label: '稽查规则组件',
                key: 'transform-audit',
                type: 3,
                level: 2,
                taskType: 'SPARK'
            },
            {
                label: '连接组件',
                key: 'transform-join',
                type: 3,
                level: 2,
                taskType: 'SPARK'
            },
            {
                label: '排序组件',
                key: 'transform-sort',
                type: 3,
                level: 2,
                taskType: 'SPARK'
            },
            {
                label: '过滤组件',
                key: 'transform-filter',
                type: 3,
                level: 2,
                taskType: 'SPARK'
            },
            {
                label: '唯一标识组件',
                key: 'transform-unique',
                type: 3,
                level: 2,
                taskType: 'SPARK'
            },
            {
                label: '聚合组件',
                key: 'transform-aggregate',
                type: 3,
                level: 2,
                taskType: 'SPARK'
            },
            {
                label: '集合组件',
                key: 'transform-collection',
                type: 3,
                level: 2,
                taskType: 'SPARK'
            },
            {
                label: '表头式组件',
                key: 'transform-header',
                type: 3,
                level: 2,
                taskType: 'SPARK'
            }
        ]
    },
    {
        label: '输出',
        type: 2,
        level: 1,
        children: [
            {
                label: '表输出组件',
                key: 'output-table',
                type: 2,
                level: 2,
                taskType: 'DATAX',
                componentType: '91',
                icon: new URL('@/assets/system/images/dpp/sc.png', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/sc.png'
            },
            {
                label: '数据库输出组件',
                key: 'output-database',
                type: 2,
                level: 2,
                taskType: 'DATAX'
            },
            {
                label: 'Api输出组件',
                key: 'output-api',
                type: 2,
                level: 2,
                taskType: 'DATAX'
            },
            {
                label: 'Excel输出组件',
                key: 'output-excel',
                type: 2,
                level: 2,
                taskType: 'DATAX'
            }
        ]
    }
];
