<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
-->

<template>
    <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
        showCancelButton :show-close="false" destroy-on-close>
        <template #header>
            <div class="justify">
                <span class="el-dialog__title">{{ currentNode?.data?.name }}</span>
                <el-tooltip effect="light" :content="td('dpp.integration.fieldBuilderTooltip', '用于通过拼接多个字段值生成新字段')" placement="top">
                    <el-icon class="tip-icon">
                        <InfoFilled />
                    </el-icon>
                </el-tooltip>
            </div>
        </template>
        <el-form ref="dpModelRefs" :model="form" label-width="140px" @submit.prevent v-loading="loading"
            :disabled="info" :label-position="labelPosition">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.nodeName', '节点名称')" prop="name"
                        :rules="[{ required: true, message: td('dpp.integration.nodeNameRequired', '请输入节点名称'), trigger: 'change' }]" :label-position="labelPosition">
                        <el-input v-model="form.name" :placeholder="td('dpp.integration.nodeNamePlaceholder', '请输入节点名称')" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.type', '类型')" prop="typeName" :label-position="labelPosition">
                        <el-select v-model="form.taskParams.typeName" :placeholder="td('dpp.integration.typePlaceholder', '请输入类型')" filterable disabled>
                            <el-option v-for="dict in typeList" :key="dict.value" :label="dict.label"
                                :value="dict.value" />
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.operationType', '操作类型')" prop="taskParams.fieldDerivationType" :rules="[
                        { required: true, message: td('dpp.integration.operationTypeRequired', '请输入操作类型'), trigger: 'change' }
                    ]" :label-position="labelPosition">
                        <el-select v-model="form.taskParams.fieldDerivationType" :placeholder="td('dpp.integration.operationTypePlaceholder', '请选择操作类型')">
                            <el-option v-for="item in deriveFieldTypes" :key="item.value" :label="item.label"
                                :value="item.value" :disabled="item.value !== 'FIELD_DERIVE_CONCAT'" />
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.taskParams.fieldDerivationType == 'FIELD_DERIVE_CONCAT'">
                    <el-form-item :label="td('dpp.integration.newFieldName', '新增字段名称')" prop="taskParams.fieldDerivationName" :rules="[
                        { required: true, message: td('dpp.integration.newFieldNameRequired', '请输入新增字段名称'), trigger: 'change' }]" :label-position="labelPosition">
                        <template #label>
                            <div class="justify-center">
                                <span>{{ td('dpp.integration.newFieldName', '新增字段名称') }}</span>
                                <el-tooltip effect="light" :content="td('dpp.integration.newFieldNameTooltip', '生成结果将写入该字段，作为新列追加到数据中')" placement="top">
                                    <el-icon class="tip-icon">
                                        <InfoFilled />
                                    </el-icon>
                                </el-tooltip>
                            </div>
                        </template>
                        <el-input v-model="form.taskParams.fieldDerivationName" :placeholder="td('dpp.integration.newFieldNamePlaceholder', '请输入新增字段名称')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.prefix', '前缀')" prop="taskParams.fieldDerivationPrefix" :label-position="labelPosition">
                        <el-input v-model="form.taskParams.fieldDerivationPrefix" :placeholder="td('dpp.integration.prefixPlaceholder', '请输入后缀')" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.suffix', '后缀')" prop="taskParams.fieldDerivationSuffix" :label-position="labelPosition">
                        <el-input v-model="form.taskParams.fieldDerivationSuffix" :placeholder="td('dpp.integration.prefixPlaceholder', '请输入后缀')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.delimiter', '连接符')" prop="taskParams.delimiter" :label-position="labelPosition">
                        <el-input v-model="form.taskParams.delimiter" :placeholder="td('dpp.integration.delimiterPlaceholder', '请输入连接符')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
                        <el-input v-model="form.description" type="textarea" :placeholder="td('common.form.descriptionPlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <div class="mb10" v-if="tableFields.length > 0"
                style="display: flex; align-items: flex-start; margin-left: 38px;">
                <span style="font-weight: 500; white-space: nowrap; margin-right: 10px;">{{ td('dpp.integration.generationRule', '生成规则') }}</span>
                <div v-html="expressionPreviewHtml" style="flex: 1; white-space: pre-wrap;"></div>
            </div>
            <el-divider content-position="center">
                <span class="blue-text">{{ td('dpp.integration.fieldValues', '字段值') }}</span>
            </el-divider>
            <div class="justify-between mb15">
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button type="primary" plain @click="handleAddField">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
                        </el-button>
                    </el-col>
                </el-row>
            </div>
            <el-table stripe height="310px" :data="tableFields" v-loading="loadingList" ref="dragTable"
                row-key="columnName">
                <el-table-column :label="td('common.display.index', '序号')" width="80" align="left">
                    <template #default="{ $index }">
                        <div class="allowDrag"
                            style="cursor: move; display: flex; justify-content: center; align-items: center;">
                            <el-icon>
                                <Operation />
                            </el-icon>
                            <span style="margin-left: 4px;">{{ $index + 1 }}</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column :label="td('dpp.integration.fieldName', '字段名称')" align="left" prop="columnName">
                    <template #default="scope">

                        <el-select v-model="scope.row.columnName" :placeholder="td('dpp.integration.selectFieldPlaceholder', '请选择字段')" style="flex: 1">
                            <el-option v-for="item in inputFields" :key="item.value" :label="item.label"
                                :value="item.columnName" :disabled="isOptionDisabled(item.columnName, scope.row)" />
                        </el-select>

                    </template>
                </el-table-column>
                <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right"
                    width="150">
                    <template #default="scope">
                        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">
                            {{ td('common.button.delete') }}
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-form>

        <template #footer>
            <div style="text-align: right">
                <el-button @click="closeDialog">{{ td('common.button.close') }}</el-button>
                <el-button type="primary" @click="saveData" v-if="!info">{{ td('common.button.save') }}</el-button>
                <!--  <el-button type="warning" @click="handleFetchFields"  v-if="!info">获取字段</el-button> -->
            </div>
        </template>
    </el-dialog>

    <FieldConflictDialog v-model="showConflictDialog" :existingFields="tableFields" :newFields="inputFields"
        @resolve="onResolveFields" />
    <CreateEditModal :visibleDialogs="opens" @update:visibleDialogs="opens = $event" @confirm="submitForm" :row="row"
        :tableFields="tableFields" :inputFields="inputFields" />
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import CreateEditModal from "../fieldMergeModal.vue";
import FieldConflictDialog from "../fieldDetection.vue";
import { defineProps, defineEmits, ref, computed, watchEffect, getCurrentInstance } from "vue";
import { typeList } from "@/utils/graph.js";
import { getNodeUniqueKey } from "@/api/dpp/task/index.js";
import useUserStore from "@/store/system/user.js";
import { createNodeSelect, getParentNode } from "@/views/dpp/utils/opBase.js";
import draggable from "vuedraggable";
import Sortable from "sortablejs";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const userStore = useUserStore();
const expressionPreviewHtml = computed(() => {
    const prefix = form.value?.taskParams?.fieldDerivationPrefix || '';
    const suffix = form.value?.taskParams?.fieldDerivationSuffix || '';
    const delimiter = form.value?.taskParams?.delimiter || '';
    const fields = tableFields.value.map(f => f.columnName).filter(Boolean);

    if (fields.length === 0) return '';
    const parts = [];
    // 添加前缀（常量）
    if (prefix) {
        parts.push(`<span class="const">"${prefix}"</span>`);
        parts.push(`<span class="op"> + </span>`);
    }
    // 字段拼接
    fields.forEach((field, idx) => {
        if (idx > 0 && delimiter) {
            parts.push(`<span class="const">"${delimiter}"</span>`);
            parts.push(`<span class="op"> + </span>`);
        }
        parts.push(`<span class="var">\${${field}}</span>`);
        if (idx < fields.length - 1) {
            parts.push(`<span class="op"> + </span>`);
        }
    });
    // 添加后缀（常量）
    if (suffix) {
        parts.push(`<span class="op"> + </span>`);
        parts.push(`<span class="const">"${suffix}"</span>`);
    }

    return parts.join('');
});

const props = defineProps({
    visible: { type: Boolean, default: true },
    title: { type: String, default: '' },
    currentNode: { type: Object, default: () => ({}) },
    info: { type: Boolean, default: false },
    graph: { type: Object, default: () => ({}) },
});
const deriveFieldTypes = [
    { value: 'FIELD_DERIVE_CONCAT', label: td('dpp.integration.concat', '拼接') },
    { value: 'FIELD_DERIVE_SUBSTRING', label: td('dpp.integration.substring', '截取') },
    { value: 'FIELD_DERIVE_REPLACE', label: td('dpp.integration.replaceLabel', '替换') },
    { value: 'FIELD_DERIVE_EXPRESSION', label: td('dpp.integration.expression', '表达式') },
    { value: 'FIELD_DERIVE_HASH', label: td('dpp.integration.hash', '哈希') },
    { value: 'FIELD_DERIVE_REGEX', label: td('dpp.integration.regexExtract', '正则提取') },
    { value: 'FIELD_DERIVE_CONSTANT', label: td('dpp.integration.constantAssignment', '常量赋值') }
]
let dragTable = ref(null);
let sortableInstance = null;
function setSort() {
    nextTick(() => {
        const tbody = dragTable.value?.$el.querySelector(
            ".el-table__body-wrapper tbody"
        );
        if (!tbody) {
            console.warn("tbody 找不到，拖拽初始化失败");
            return;
        }

        if (sortableInstance) {
            sortableInstance.destroy();
        }

        sortableInstance = Sortable.create(tbody, {
            handle: ".allowDrag",
            animation: 150,
            onEnd: (evt) => {

                const movedItem = tableFields.value.splice(evt.oldIndex, 1)[0];
                tableFields.value.splice(evt.newIndex, 0, movedItem);
                console.log("拖拽后顺序:", tableFields.value.map((f) => f.columnName));
            },
        });
    });
}

function handleAddField() {
    if (!Array.isArray(inputFields.value) || inputFields.value.length === 0) {
        proxy.$message.warning(td("dpp.integration.inputFieldEmptyCannotAdd", "输入字段为空，无法添加字段"));
        return;
    }
    // 已添加的字段名
    const usedNames = tableFields.value.map((item) => item.columnName);

    // 找到未使用的字段
    const nextField = inputFields.value.find(
        (item) => !usedNames.includes(item.columnName)
    );

    if (!nextField) {
        proxy.$message.warning(td("dpp.integration.noMoreFieldsToAdd", "新增失败，已无可添加的字段"));
        return;
    }

    tableFields.value.push({
        columnName: nextField.columnName,
        order: 'asc',
        caseSensitive: false,
        locale: true,
        collatorStrength: 0,
        presorted: false
    });
    setSort()

}
const showConflictDialog = ref(false);

const handleFetchFields = () => {
    const tableNames = tableFields.value.map(f => f.columnName).sort();
    const inputNames = inputFields.value.map(f => f.columnName).sort();

    if (
        tableNames.length === inputNames.length &&
        tableNames.every((name, idx) => name === inputNames[idx])
    ) {
        return proxy.$message.warning(td("dpp.integration.alreadyLatestFields", "新增失败，当前已是最新字段"));
    }
    showConflictDialog.value = true;
};
function onResolveFields(payload) {
    if (!payload || !payload.action) return;

    switch (payload.action) {
        case "addNewOnly": {
            console.log("父组件：只增加新字段");

            // 计算已有字段名称
            const existingNames = tableFields.value.map(f => f.columnName);
            // 找到新字段中不在已有字段中的字段
            const newUniqueFields = inputFields.value.filter(
                f => !existingNames.includes(f.columnName)
            );
            // 加入到 tableFields 中
            tableFields.value = tableFields.value.concat(deepCopy(newUniqueFields));
            break;
        }

        case "addAll": {
            console.log("🚀 ~ onResolveFields ~  tableFields.value =:", tableFields.value)

            console.log("父组件：增加所有字段");
            tableFields.value = []
            // 这里先清空，再加全部字段，避免重复
            tableFields.value = deepCopy(inputFields.value);

            break;
        }

        case "clearAndAddAll": {
            console.log("父组件：清空并增加所有字段");

            // 恢复原始备份字段
            tableFields.value = deepCopy(inputFields.value);

            break;
        }

        case "cancel": {
            console.log("父组件：取消操作");
            break;
        }

    }
}

const isOptionDisabled = (optionValue, currentRow) => {
    return tableFields.value.some(
        (row) => row !== currentRow && row.columnName === optionValue
    );
};

const emit = defineEmits(["update", "confirm"]);

const visibleDialog = computed({
    get() {
        return props.visible;
    },
    set(newValue) {
        emit("update", newValue);
    },
});

let tableFields = ref([]);
let originalTableFieldsBackup = ref([]);
let inputFields = ref([]);
let loading = ref(false);
let loadingList = ref(false);
let opens = ref(false);
let row = ref();
let dpModelRefs = ref();
let form = ref({});

function handleRule(data) {
    row.value = { ...data };
    opens.value = true;
}

function handleDelete(row) {
    // 1. 从 tableFields 中删除对应项
    const idxTable = tableFields.value.findIndex(
        (item) => item.columnName === row.columnName
    );
    if (idxTable !== -1) {
        tableFields.value.splice(idxTable, 1);
    }
    const originalField = originalTableFieldsBackup.value.find(
        (item) => item.columnName === row.columnName
    );
    if (originalField) {
        const idxField = inputFields.value.findIndex(
            (item) => item.columnName === row.columnName
        );
        if (idxField !== -1) {
            inputFields.value[idxField] = JSON.parse(JSON.stringify(originalField));
        } else {
            inputFields.value.push(JSON.parse(JSON.stringify(originalField)));
        }
    }
    setSort()
}

// 提交弹窗规则数据
const submitForm = (value) => {
    if (!value || !Array.isArray(value)) return;

    value.forEach((ruleItem) => {
        if (!ruleItem?.ruleConfig) return;

        let parsedConfig;
        try {
            parsedConfig = JSON.parse(ruleItem.ruleConfig);
        } catch (e) {
            console.warn("无法解析 ruleConfig:", ruleItem.ruleConfig);
            return;
        }
        const sourceField = parsedConfig?.fieldMerge?.sourceField;
        if (!sourceField) return;

        const tableIndex = tableFields.value.findIndex(
            (item) => item.columnName == sourceField
        );
        if (tableIndex !== -1) {
            const updatedItem = {
                ...tableFields.value[tableIndex],
                cleanRuleList: [ruleItem],
                elementId: [ruleItem.ruleId],
            };
            tableFields.value[tableIndex] = updatedItem;

            const fieldIndex = inputFields.value.findIndex(
                (item) => item.columnName == sourceField
            );
            if (fieldIndex !== -1) {
                inputFields.value[fieldIndex] = updatedItem;
            } else {
                inputFields.value.push(updatedItem);
            }
        }
    });
    opens.value = false;
};

const off = () => {
    proxy.resetForm("dpModelRefs");
    tableFields.value = [];
    inputFields.value = [];
    originalTableFieldsBackup.value = [];
};

const saveData = async () => {
    try {
        const valid = await dpModelRefs.value.validate();
        if (!valid) return;
        // 判断表格是否为空
        if (!tableFields.value || tableFields.value.length === 0) {
            proxy.$message.warning(td("dpp.integration.validateFailedAddAtLeastOne", "校验未通过，请至少添加一个字段"));
            return;
        }
        if (!form.value.code) {
            loading.value = true;
            const response = await getNodeUniqueKey({
                projectCode: userStore.projectCode || "133545087166112",
                projectId: userStore.projectId,
            });
            loading.value = false;
            form.value.code = response.data;
        }
        const taskParams = form.value?.taskParams || {};
        taskParams.tableFields = tableFields.value;
        console.log("🚀 ~ saveData ~  form.value.taskParams.fieldDerivationName:", form.value.taskParams.fieldDerivationName)
        // 输出字段拼接目标字段
        taskParams.outputFields = [
            ...inputFields.value,
            {
                columnName: form.value.taskParams.fieldDerivationName,
                source: form.value.name
            }
        ];
        console.log("保存数据 - outputFields:", taskParams.outputFields);
        emit("confirm", form.value);

    } catch (error) {
        console.error("保存数据失败:", error);
        loading.value = false;
    }
};

const closeDialog = () => {
    off();
    emit("update", false);
};

function deepCopy(data) {
    if (data === undefined || data === null) {
        return {};
    }
    try {
        return JSON.parse(JSON.stringify(data));
    } catch (e) {
        return {};
    }
}

let nodeOptions = ref([]);
watchEffect(() => {
    if (!props.visible) {
        off();
        return;
    }
    form.value = deepCopy(props.currentNode?.data || {});
    nodeOptions.value = createNodeSelect(props.graph, props.currentNode.id);
    // 备份初始表字段，避免被篡改
    originalTableFieldsBackup.value = deepCopy(
        props.currentNode?.data?.taskParams?.inputFields || []
    );
    inputFields.value = props.currentNode?.data?.taskParams?.inputFields;
    tableFields.value = props.currentNode?.data?.taskParams?.tableFields || [];
    setSort()

});
</script>

<style scoped lang="less">
.blue-text {
    color: #2666fb;
}

::v-deep(.var),
::v-deep(.const),
::v-deep(.op) {
    color: #000;
    font-weight: 700;
}
</style>
