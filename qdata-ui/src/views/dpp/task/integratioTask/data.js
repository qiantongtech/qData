// treeData.js
export const treeData = [
    {
        label: '输入',
        level: 1,
        type: 1,
        engine: ['SPARK','FLINK'],
        children: [
            {
                label: '表输入组件',
                key: 'input-table',
                type: 1,
                level: 2,
                taskType: 'DATAX',
                componentType: '1',
                engine: ['SPARK','FLINK'],
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
            {
                label: 'Kafka输入组件',
                key: 'input-Kafka',
                type: 1,
                level: 2,
                componentType: '3',
                taskType: 'DATAX',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/Kafka.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/kafka.png'
            },
            {
                label: 'Hive输入组件',
                key: 'input-hive',
                type: 1,
                level: 2,
                componentType: '5',
                taskType: 'DATAX',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/hive.svg', import.meta.url).href,
                icons: '@/assets/system/images/dpp/kafka.png'
            },
            {
                label: 'HDFS输入组件',
                key: 'input-hdfs',
                type: 1,
                level: 2,
                componentType: '6',
                taskType: 'DATAX',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/hdfs.svg', import.meta.url).href,
                //

            },
            {
                label: 'API输入组件',
                key: 'input-api',
                type: 1,
                level: 2,
                componentType: '7',
                taskType: 'DATAX',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/api.svg', import.meta.url).href,
            }
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
                label: '字段拆分',
                key: 'transform-cleaning',
                type: 4,
                level: 2,
                taskType: 'FIELD_SPLIT',
                componentType: '35',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/zdcf.svg', import.meta.url).href, // 动态获取路径
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
            {
                label: '去除重复记录',
                key: 'transform-cleaning',
                type: 4,
                level: 2,
                taskType: 'DATA_DEDUPLICATION',
                componentType: '40',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/qccfjl.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: '行转列',
                key: 'transform-cleaning',
                type: 4,
                level: 2,
                taskType: 'PIVOT',
                componentType: '42',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/lzh.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: '列转行',
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'UNPIVOT',
                componentType: '43',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/hzl.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: '字段加密',
                key: 'transform-cleaning',
                type: 4,
                level: 2,
                taskType: 'FIELD_ENCRYPTION',
                componentType: '44',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/lzh.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: '字段解密',
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'FIELD_DECRYPTION',
                componentType: '45',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/hzl.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: 'ID生成器',
                key: 'transform-cleaning',
                type: 41,
                level: 2,
                taskType: 'ADD_SEQUENCE',
                componentType: '41',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/zjxl.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: '计算器',
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'FIELD_CALCULATOR',
                componentType: '46',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/jsq.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: '值映射',
                key: 'transform-cleaning',
                type: 6,
                level: 2,
                taskType: 'VALUE_MAP',
                componentType: '47',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/zys.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: '增加常量',
                key: 'transform-cleaning',
                type: 48,
                level: 2,
                taskType: 'ADD_CONSTANT',
                componentType: '48',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/zjcl.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: '数值范围',
                key: 'transform-cleaning',
                type: 49,
                level: 2,
                taskType: 'VALUE_RANGE',
                componentType: '49',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/szfw.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: '字符串操作',
                key: 'transform-cleaning',
                type: 50,
                level: 2,
                taskType: 'STRING_OPERATION',
                componentType: '50',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/zfccz.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            }, {
                label: '字符串替换',
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'STRING_REPLACE',
                componentType: '21',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/zfcth.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: '字段选择、修改',
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'SELECT_FIELDS',
                componentType: '22',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/ztxg.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            // {
            //     label: '字段修改',
            //     key: 'transform-cleaning',
            //     type: 3,
            //     level: 2,
            //     taskType: 'SPARK',
            //     componentType: '84',
            //     icon: new URL('@/assets/system/images/dpp/ztxg.svg', import.meta.url).href, // 动态获取路径
            //     icons: '@/assets/system/images/dpp/SHELL.png'
            // },
            {
                label: '设置字段值',
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'SET_FIELD_VALUE',
                componentType: '23',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/szzdz.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
            {
                label: '列拆分为多行',
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'SPARK',
                componentType: '',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/lcfh.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            }, {
                label: '剪切字符串',
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'SPARK',
                componentType: '',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/jqzfc.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            }, {
                label: '将字段值设为常量',
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'SPARK',
                componentType: '',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/zd.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            }, {
                label: '根据字段值改变序列',
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'SPARK',
                componentType: '',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/gbl.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            }, {
                label: '行扁平化',
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'SPARK',
                componentType: '',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/xbph.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/SHELL.png'
            },
        ]
    },
    {
        label: '输出',
        type: 2,
        level: 1,
        engine: ['SPARK','FLINK'],
        children: [
            {
                label: '表输出组件',
                key: 'output-table',
                type: 2,
                level: 2,
                taskType: 'DATAX',
                componentType: '91',
                engine: ['SPARK','FLINK'],
                icon: new URL('@/assets/system/images/dpp/bsc.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/sc.png'
            },
            {
                label: 'Hive输出组件',
                key: 'output-table',
                type: 2,
                level: 2,
                taskType: 'DATAX',
                componentType: '92',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/hivesc.svg', import.meta.url).href, // 动态获取路径
                icons: '@/assets/system/images/dpp/sc.png'
            },
            {
                label: 'HDFS输出组件',
                key: 'output-hdfs',
                type: 2,
                level: 2,
                componentType: '93',
                taskType: 'DATAX',
                engine: ['SPARK'],
                icon: new URL('@/assets/system/images/dpp/hdfssc.svg', import.meta.url).href, // 动态获取路径
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
                }else{
                    child.disabled = false;
                }
            })
        }
        if (!item.engine.includes(taskType)) {
            item.disabled = true;
        }else{
            item.disabled = false;
        }
    })
    return data;
}
