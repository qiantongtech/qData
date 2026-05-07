// 测试数据...
const tableData = [
    {
        shape: 'TableNode',
        name: '站点基础信息表/ODS_HYD_DISCHARGE',
        id: 'ODS_HYD_DISCHARGE',
        fields: [
            {
                enName: 'BGMD',
                cnName: '开始月日',
                type: 'VARCHAR2'
            },
            {
                enName: 'EDMD',
                cnName: '结束月日',
                type: 'VARCHAR2'
            },
            {
                enName: 'FSLTDZ',
                cnName: '汛限水位',
                type: 'VARCHAR2'
            },
            {
                enName: 'FSLTDW',
                cnName: '汛限库容',
                type: 'VARCHAR2'
            },
            {
                enName: 'FSTP',
                cnName: '汛期类别',
                type: 'VARCHAR2'
            }
        ]
    },
    {
        shape: 'TableNode',
        name: '站点水位明细表/DWD_WATER_LEVEL_CLEAN_OUTLIER',
        id: 'DWD_WATER_LEVEL_CLEAN_OUTLIER',
        fields: [
            {
                enName: 'ID',
                cnName: '记录主键（源）',
                type: 'VARCHAR'
            },
            {
                enName: 'STATION_CODE',
                cnName: '站点编码',
                type: 'VARCHAR'
            },
            {
                enName: 'OBS_TIME',
                cnName: '观测时间',
                type: 'VARCHAR'
            },
            {
                enName: 'WATER_LEVEL',
                cnName: '水位（米，保留2位小数）',
                type: 'VARCHAR'
            },
            {
                enName: 'QUALITY_FLAG',
                cnName: '源端质控标识',
                type: 'VARCHAR'
            },
            {
                enName: 'TS',
                cnName: '源端变更时间',
                type: 'VARCHAR'
            }
        ]
    },
    {
        shape: 'TableNode',
        name: '站点基础信息表/ODS_HYD_DISCHARGE',
        id: 'ODS_HYD_DISCHARGE1',
        fields: [
            {
                enName: 'BGMD',
                cnName: '开始月日',
                type: 'VARCHAR2'
            },
            {
                enName: 'EDMD',
                cnName: '结束月日',
                type: 'VARCHAR2'
            },
            {
                enName: 'FSLTDZ',
                cnName: '汛限水位',
                type: 'VARCHAR2'
            },
            {
                enName: 'FSLTDW',
                cnName: '汛限库容',
                type: 'VARCHAR2'
            },
            {
                enName: 'FSTP',
                cnName: '汛期类别',
                type: 'VARCHAR2'
            }
        ]
    },
    {
        shape: 'TableNode',
        name: '站点基础信息表/ODS_HYD_DISCHARGE',
        id: 'ODS_HYD_DISCHARGE12',
        fields: [
            {
                enName: 'BGMD',
                cnName: '开始月日',
                type: 'VARCHAR2'
            },
            {
                enName: 'EDMD',
                cnName: '结束月日',
                type: 'VARCHAR2'
            },
            {
                enName: 'FSLTDZ',
                cnName: '汛限水位',
                type: 'VARCHAR2'
            },
            {
                enName: 'FSLTDW',
                cnName: '汛限库容',
                type: 'VARCHAR2'
            },
            {
                enName: 'FSTP',
                cnName: '汛期类别',
                type: 'VARCHAR2'
            }
        ]
    }
];

export const tableDataRes = {
    origin: {
        shape: 'DatabaseNode',
        name: '数据中台原始库',
        id: 'database'
    },
    targets: tableData.slice(0, 2)
};

const fieldData = [
    {
        shape: 'FieldNode',
        id: 'ID',
        name: '记录主键（源）',
        type: 'VARCHAR'
    },
    {
        shape: 'FieldNode',
        id: 'STATION_CODE',
        name: '站点编码',
        type: 'VARCHAR'
    },
    {
        shape: 'FieldNode',
        id: 'OBS_TIME',
        name: '观测时间',
        type: 'VARCHAR'
    },
    {
        shape: 'FieldNode',
        id: 'WATER_LEVEL',
        name: '水位（米，保留2位小数）',
        type: 'VARCHAR'
    },
    {
        shape: 'FieldNode',
        id: 'QUALITY_FLAG',
        name: '源端质控标识',
        type: 'VARCHAR'
    },
    {
        shape: 'FieldNode',
        id: 'TS',
        name: '源端变更时间',
        type: 'VARCHAR'
    }
];

export const fieldsDataRes = {
    origin: {
        shape: 'TableInfoNode',
        name: '站点基础信息表',
        id: 'TableInfo'
    },
    targets: fieldData
};
