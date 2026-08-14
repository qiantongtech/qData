<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
    <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
        showCancelButton :show-close="false" destroy-on-close>
        <template #header>
            <div class="justify">
                <span class="el-dialog__title">{{ currentNode?.data?.name }}</span>
                <el-tooltip effect="light" :content="td('dpp.integration.fieldBuilderTooltip', 'Used to generate new fields by concatenating multiple field values, supports prefix, suffix and delimiter settings, commonly used for constructing unique identifiers or business codes')" placement="top">
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
                    <el-form-item :label="td('dpp.integration.nodeName', 'Node Name')" prop="name"
                        :rules="[{ required: true, message: td('dpp.integration.nodeNameRequired', 'Please enter node name'), trigger: 'change' }]" :label-position="labelPosition">
                        <el-input v-model="form.name" :placeholder="td('dpp.integration.nodeNamePlaceholder', 'Please enter node name')" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.type', 'Type')" prop="typeName" :label-position="labelPosition">
                        <el-select v-model="form.taskParams.typeName" :placeholder="td('dpp.integration.typePlaceholder', 'Please enter type')" filterable disabled>
                            <el-option v-for="dict in typeList" :key="dict.value" :label="dict.label"
                                :value="dict.value" />
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.operationType', 'Operation Type')" prop="taskParams.fieldDerivationType" :rules="[
                        { required: true, message: td('dpp.integration.operationTypeRequired', 'Please enter operation type'), trigger: 'change' }
                    ]" :label-position="labelPosition">
                        <el-select v-model="form.taskParams.fieldDerivationType" :placeholder="td('dpp.integration.operationTypePlaceholder', 'Please select operation type')">
                            <el-option v-for="item in deriveFieldTypes" :key="item.value" :label="item.label"
                                :value="item.value" :disabled="item.value !== 'FIELD_DERIVE_CONCAT'" />
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.taskParams.fieldDerivationType == 'FIELD_DERIVE_CONCAT'">
                    <el-form-item :label="td('dpp.integration.newFieldName', 'New Field Name')" prop="taskParams.fieldDerivationName" :rules="[
                        { required: true, message: td('dpp.integration.newFieldNameRequired', 'Please enter new field name'), trigger: 'change' },
                        { validator: validateDerivedFieldName, trigger: ['change', 'blur'] }]" :label-position="labelPosition">
                        <template #label>
                            <div class="justify-center">
                                <span>{{ td('dpp.integration.newFieldName', 'New Field Name') }}</span>
                                <el-tooltip effect="light" :content="td('dpp.integration.newFieldNameTooltip', 'Generated result will be written to this field, appended as a new column')" placement="top">
                                    <el-icon class="tip-icon">
                                        <InfoFilled />
                                    </el-icon>
                                </el-tooltip>
                            </div>
                        </template>
                        <el-input v-model="form.taskParams.fieldDerivationName" :placeholder="td('dpp.integration.newFieldNamePlaceholder', 'Please enter new field name')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.prefix', 'Prefix')" prop="taskParams.fieldDerivationPrefix" :label-position="labelPosition">
                        <el-input v-model="form.taskParams.fieldDerivationPrefix" :placeholder="td('dpp.integration.prefixPlaceholder', 'Please enter prefix')" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.suffix', 'Suffix')" prop="taskParams.fieldDerivationSuffix" :label-position="labelPosition">
                        <el-input v-model="form.taskParams.fieldDerivationSuffix" :placeholder="td('dpp.integration.prefixPlaceholder', 'Please enter prefix')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.delimiter', 'Delimiter')" prop="taskParams.delimiter" :label-position="labelPosition">
                        <el-input v-model="form.taskParams.delimiter" :placeholder="td('dpp.integration.delimiterPlaceholder', 'Please enter delimiter')" />
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
                <span style="font-weight: 500; white-space: nowrap; margin-right: 10px;">{{ td('dpp.integration.generationRule', 'Generation Rule') }}</span>
                <div v-html="expressionPreviewHtml" style="flex: 1; white-space: pre-wrap;"></div>
            </div>
            <el-divider content-position="center">
                <span class="blue-text">{{ td('dpp.integration.fieldValues', 'Field Values') }}</span>
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
                <el-table-column :label="td('common.display.index', 'Index')" width="80" align="left">
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
                <el-table-column :label="td('dpp.integration.fieldName', 'Field Name')" align="left" prop="columnName">
                    <template #default="scope">

                        <el-select v-model="scope.row.columnName" :placeholder="td('dpp.integration.selectFieldPlaceholder', 'Please select field name')" style="flex: 1">
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
                <!--  <el-button type="warning" @click="handleFetchFields" v-if="!info">Get fields</el-button> -->
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
function validateDerivedFieldName(rule, value, callback) {
    const fieldName = String(value ?? '').trim();
    if (!fieldName) {
        callback();
        return;
    }
    const upstreamNames = new Set(
        (inputFields.value || []).map(item => String(item.columnName ?? '').trim().toLowerCase()).filter(Boolean)
    );
    if (upstreamNames.has(fieldName.toLowerCase())) {
        callback(new Error(td(
            'dpp.integration.derivedFieldNameDuplicate',
            'The new field name already exists. Please use another name.'
        )));
        return;
    }
    callback();
}
const expressionPreviewHtml = computed(() => {
    const prefix = form.value?.taskParams?.fieldDerivationPrefix || '';
    const suffix = form.value?.taskParams?.fieldDerivationSuffix || '';
    const delimiter = form.value?.taskParams?.delimiter || '';
    const fields = tableFields.value.map(f => f.columnName).filter(Boolean);

    if (fields.length === 0) return '';
    const parts = [];
    // Add prefix (constant)
    if (prefix) {
        parts.push(`<span class="const">"${prefix}"</span>`);
        parts.push(`<span class="op"> + </span>`);
    }
    // Field splicing
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
    // Add suffix (constant)
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
    taskType: { type: String, default: '' },
});
const deriveFieldTypes = [
    { value: 'FIELD_DERIVE_CONCAT', label: td('dpp.integration.concat', 'Concatenate') },
    { value: 'FIELD_DERIVE_SUBSTRING', label: td('dpp.integration.substring', 'Substring') },
    { value: 'FIELD_DERIVE_REPLACE', label: td('dpp.integration.replaceLabel', 'Replace') },
    { value: 'FIELD_DERIVE_EXPRESSION', label: td('dpp.integration.expression', 'Expression') },
    { value: 'FIELD_DERIVE_HASH', label: td('dpp.integration.hash', 'Hash') },
    { value: 'FIELD_DERIVE_REGEX', label: td('dpp.integration.regexExtract', 'Regex Extract') },
    { value: 'FIELD_DERIVE_CONSTANT', label: td('dpp.integration.constantAssignment', 'Constant Assignment') }
]
let dragTable = ref(null);
let sortableInstance = null;
function setSort() {
    nextTick(() => {
        const tbody = dragTable.value?.$el.querySelector(
            ".el-table__body-wrapper tbody"
        );
        if (!tbody) {
            console.warn("tbody not found; drag initialization failed");
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
                console.log("Order after drag:", tableFields.value.map((f) => f.columnName));
            },
        });
    });
}

function handleAddField() {
    if (!Array.isArray(inputFields.value) || inputFields.value.length === 0) {
        proxy.$message.warning(td("dpp.integration.inputFieldEmptyCannotAdd", "Input field is empty, cannot add fields"));
        return;
    }
    // Added field name
    const usedNames = tableFields.value.map((item) => item.columnName);

    // Unused fields found
    const nextField = inputFields.value.find(
        (item) => !usedNames.includes(item.columnName)
    );

    if (!nextField) {
        proxy.$message.warning(td("dpp.integration.noMoreFieldsToAdd", "Add failed, no more fields to add"));
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
        return proxy.$message.warning(td("dpp.integration.alreadyLatestFields", "Add failed, already at latest fields"));
    }
    showConflictDialog.value = true;
};
function onResolveFields(payload) {
    if (!payload || !payload.action) return;

    switch (payload.action) {
        case "addNewOnly": {
            console.log("Parent component: add new fields only");

            // Calculate existing field names
            const existingNames = tableFields.value.map(f => f.columnName);
            // Find fields in the new field that are not among the existing fields
            const newUniqueFields = inputFields.value.filter(
                f => !existingNames.includes(f.columnName)
            );
            // Add to tableFields
            tableFields.value = tableFields.value.concat(deepCopy(newUniqueFields));
            break;
        }

        case "addAll": {
            console.log("🚀 ~ onResolveFields ~  tableFields.value =:", tableFields.value)

            console.log("Parent component: add all fields");
            tableFields.value = []
            // Clear it here first and then add all the fields to avoid duplication.
            tableFields.value = deepCopy(inputFields.value);

            break;
        }

        case "clearAndAddAll": {
            console.log("Parent component: clear and add all fields");

            // Restore original backup fields
            tableFields.value = deepCopy(inputFields.value);

            break;
        }

        case "cancel": {
            console.log("Parent component: cancel operation");
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
    // 1. Delete the corresponding item from tableFields
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

// Submit pop-up rule data
const submitForm = (value) => {
    if (!value || !Array.isArray(value)) return;

    value.forEach((ruleItem) => {
        if (!ruleItem?.ruleConfig) return;

        let parsedConfig;
        try {
            parsedConfig = JSON.parse(ruleItem.ruleConfig);
        } catch (e) {
            console.warn("Unable to parse ruleConfig:", ruleItem.ruleConfig);
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
        // Determine whether the table is empty
        if (!tableFields.value || tableFields.value.length === 0) {
            proxy.$message.warning(td("dpp.integration.validateFailedAddAtLeastOne", "Validation failed, please add at least one field"));
            return;
        }
        form.value.taskParams.fieldDerivationName = String(
            form.value.taskParams.fieldDerivationName ?? ''
        ).trim();
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
        // Output field splicing target field
        taskParams.outputFields = [
            ...inputFields.value,
            {
                columnName: form.value.taskParams.fieldDerivationName,
                source: form.value.name
            }
        ];
        console.log("Save data - outputFields:", taskParams.outputFields);
        emit("confirm", form.value);

    } catch (error) {
        console.error("Failed to save data:", error);
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
    // Back up initial table fields to avoid tampering
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
