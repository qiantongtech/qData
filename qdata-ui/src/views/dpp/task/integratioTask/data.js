// treeData.js
export const treeData = [
    {
        label: '输入',
        level: 1,
        type: 1,
        engine: ['SPARK', 'FLINK'],
        children: [
            {
                label: '表输入组件',
                key: 'input-table',
                type: 1,
                level: 2,
                taskType: 'DATAX',
                componentType: '1',
                engine: ['SPARK', 'FLINK'],
                icon: new URL('@/assets/system/images/dpp/bsr.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/sr.png'
            },
            {
                label: 'Excel文件输入组件',
                key: 'input-excel',
                type: 1,
                level: 2,
                componentType: '2',
                taskType: 'DATAX',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/wxl.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/excel.png'
            },
            {
                label: 'CSV输入组件',
                key: 'input-csv',
                type: 1,
                level: 2,
                componentType: '4',
                taskType: 'DATAX',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/scv.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/csv.png'
            },
        ]
    },
    {
        label: '转换',
        type: 3,
        level: 1,
        engine: ['SPARK'],
        children: [
            {
                label: '转换组件',
                key: 'transform-desensitization',
                type: 3,
                level: 2,
                taskType: 'SPARK',
                componentType: '31',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/zh.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/zh.png'
            },
            {
                label: '排序记录',
                key: 'transform-cleaning',
                type: 4,
                level: 2,
                taskType: 'SORT_RECORD',
                componentType: '34',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/pxjl.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: '字段派生器',
                key: 'transform-cleaning',
                type: 4,
                level: 2,
                taskType: 'FIELD_DERIVATION',
                componentType: '39',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/zdpf.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
        ]
    },
    {
        label: '输出',
        type: 2,
        level: 1,
        engine: ['SPARK', 'FLINK'],
        children: [
            {
                label: '表输出组件',
                key: 'output-table',
                type: 2,
                level: 2,
                taskType: 'DATAX',
                componentType: '91',
                engine: ['SPARK', 'FLINK'],
                icon: new URL('@/assets/system/images/dpp/bsc.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/sc.png'
            },
        ]
    }
];

// 返回知道数据
export const getTreeData = (taskType) => {
    var data = [...treeData];
    data.map(item => {
        if (item.children) {
            item.children.map(child => {
                if (!child.engine.includes(taskType)) {
                    child.disabled = true;
                } else {
                    child.disabled = false;
                }
            })
        }
        if (!item.engine.includes(taskType)) {
            item.disabled = true;
        } else {
            item.disabled = false;
        }
    })
    return data;
}
