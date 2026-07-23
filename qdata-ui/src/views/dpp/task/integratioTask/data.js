/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

// treeData.js
import { i18n } from '@/plugins/vueI18n';

const td = (key, def) => {
    if (!i18n.global.te(key)) return def;
    return i18n.global.t(key);
};

export const treeData = [
    {
        label: td('dpp.integration.input', 'Input'),
        level: 1,
        type: 1,
        engine: ['SPARK', 'FLINK', 'DATAX'],
        children: [
            {
                label: td('dpp.integration.inputTable', 'Table Input Component'),
                key: 'input-table',
                type: 1,
                level: 2,
                taskType: 'DATAX',
                componentType: '1',
                engine: ['SPARK', 'FLINK', 'DATAX'],
                icon: new URL('@/assets/images/common/dpp/icon-bsr.svg', import.meta.url).href, // Dynamically obtain path
                icons: '@/assets/images/common/dpp/img-sr.png'
            },
            {
                label: td('dpp.integration.inputExcel', 'Excel File Input Component'),
                key: 'input-excel',
                type: 1,
                level: 2,
                componentType: '2',
                taskType: 'DATAX',
                engine: ['SPARK'],
                icon: new URL('@/assets/images/common/dpp/icon-wxl.svg', import.meta.url).href, // Dynamically obtain path
                icons: '@/assets/images/common/dpp/file-excel.png'
            },
            {
                label: td('dpp.integration.inputCsv', 'CSV Input Component'),
                key: 'input-csv',
                type: 1,
                level: 2,
                componentType: '4',
                taskType: 'DATAX',
                engine: ['SPARK'],
                icon: new URL('@/assets/images/common/dpp/icon-scv.svg', import.meta.url).href, // Dynamically obtain path
                icons: '@/assets/images/common/dpp/file-csv.png'
            },

        ]
    },
    {
        label: td('dpp.integration.transform', 'Transform'),
        type: 3,
        level: 1,
        engine: ['SPARK','DATAX'],
        children: [
            {
                label: td('dpp.integration.transformComponent', 'Transform Component'),
                key: 'transform-desensitization',
                type: 3,
                level: 2,
                taskType: 'SPARK',
                componentType: '31',
                engine: ['SPARK', 'DATAX'],
                icon: new URL('@/assets/images/common/dpp/icon-zh.svg', import.meta.url).href, // Dynamically obtain path
                icons: '@/assets/images/common/dpp/img-zh.png'
            },
            {
                label: td('dpp.integration.sortRecords', 'Sort Records'),
                key: 'transform-cleaning',
                type: 4,
                level: 2,
                taskType: 'SORT_RECORD',
                componentType: '34',
                engine: ['SPARK', 'DATAX'],
                icon: new URL('@/assets/images/common/dpp/icon-pxjl.svg', import.meta.url).href, // Dynamically obtain path
                icons: '@/assets/images/common/dpp/img-shell-one.png'
            },
            {
                label: td('dpp.integration.fieldDerivation', 'Field Derivation'),
                key: 'transform-cleaning',
                type: 4,
                level: 2,
                taskType: 'FIELD_DERIVATION',
                componentType: '39',
                engine: ['SPARK', 'DATAX'],
                icon: new URL('@/assets/images/common/dpp/icon-zdpf.svg', import.meta.url).href, // Dynamically obtain path
                icons: '@/assets/images/common/dpp/img-shell-one.png'
            },
            {
                label: td('dpp.integration.removeDuplicates', 'Remove Duplicates'),
                key: 'transform-cleaning',
                type: 4,
                level: 2,
                taskType: 'DATA_DEDUPLICATION',
                componentType: '40',
                engine: ['SPARK','DATAX'],
                icon: new URL('@/assets/images/common/dpp/icon-dedu.svg', import.meta.url).href, // Dynamically obtain path
                form: 'transform/dedupFilter.vue',
            },
            {
                label: td('dpp.integration.addConstant', 'Add Constant'),
                key: 'transform-cleaning',
                type: 48,
                level: 2,
                taskType: 'ADD_CONSTANT',
                componentType: '48',
                engine: ['SPARK', 'DATAX'],
                icon: new URL('@/assets/images/common/dpp/icon-action-add.svg', import.meta.url).href, // Dynamically obtain path
                form: 'transform/addConstants.vue',
            },
            {
                label: td('dpp.integration.fieldSelectModify', 'Field Select/Modify'),
                key: 'transform-cleaning',
                type: 3,
                level: 2,
                taskType: 'SELECT_FIELDS',
                componentType: '22',
                engine: ['SPARK', 'DATAX'],
                icon: new URL('@/assets/images/common/dpp/icon-action-set.svg', import.meta.url).href, // Dynamically obtain path
                form: 'transform/fieldSelectAndmodificat.vue',
            },
            {
                label: td('dpp.integration.valueMapping', 'Value Mapping'),
                key: 'transform-cleaning',
                type: 6,
                level: 2,
                taskType: 'VALUE_MAP',
                componentType: '47',
                engine: ['SPARK', 'DATAX'],
                icon: new URL('@/assets/images/common/dpp/icon-map.svg', import.meta.url).href, // Dynamically obtain path
                form: 'transform/valueMapping.vue',
            },
        ]
    },
    {
        label: td('dpp.integration.output', 'Output'),
        type: 2,
        level: 1,
        engine: ['SPARK', 'FLINK', 'DATAX'],
        children: [
            {
                label: td('dpp.integration.outputTable', 'Table Output Component'),
                key: 'output-table',
                type: 2,
                level: 2,
                taskType: 'DATAX',
                componentType: '91',
                engine: ['SPARK', 'FLINK', 'DATAX'],
                icon: new URL('@/assets/images/common/dpp/icon-bsc.svg', import.meta.url).href, // Dynamically obtain path
                icons: '@/assets/images/common/dpp/img-sc.png'
            },
        ]
    }
];

const DATAX_ENABLED_COMPONENT_TYPES = new Set(['1', '22', '31', '34', '39', '40', '47', '48', '91']);
const DATAX_HIDDEN_COMPONENT_TYPES = new Set(['2', '4']);

// Return known data
export const getTreeData = (taskType) => {
    return treeData.map(item => {
        const children = item.children
            ?.filter(child => taskType !== 'DATAX' || !DATAX_HIDDEN_COMPONENT_TYPES.has(child.componentType))
            .map(child => ({
                ...child,
                disabled: !child.engine.includes(taskType)
                    || (taskType === 'DATAX' && !DATAX_ENABLED_COMPONENT_TYPES.has(child.componentType)),
            }));

        return {
            ...item,
            children,
            disabled: !item.engine.includes(taskType),
        };
    });
}
