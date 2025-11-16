/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
