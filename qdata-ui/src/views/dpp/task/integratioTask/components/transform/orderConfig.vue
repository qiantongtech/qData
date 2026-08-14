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
                <el-tooltip effect="light" :content="td('dpp.integration.orderConfigTooltip', 'Used to configure field sorting rules, including field order, sort direction, case sensitivity options')" placement="top">
                    <el-icon class="tip-icon">
                        <InfoFilled />
                    </el-icon>
                </el-tooltip>
            </div>
        </template>
        <el-form ref="dpModelRefs" :model="form" label-width="110px" @submit.prevent v-loading="loading"
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
                <el-col :span="24">
                    <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
                        <el-input v-model="form.description" type="textarea" :placeholder="td('common.form.descriptionPlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-divider content-position="center">
                <span class="blue-text">{{ td('dpp.column.fieldTerm', 'Field Term') }}</span>
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
                    <template #header>
                        <div class="justify-center">
                            <span>{{ td('common.display.index', 'Index') }}</span>
                            <el-tooltip effect="light" :content="td('dpp.integration.sequenceTooltip', 'Smaller number means higher sort priority')" placement="top">
                                <el-icon class="tip-icon">
                                    <InfoFilled />
                                </el-icon>
                            </el-tooltip>
                        </div>
                    </template>
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

                <el-table-column :label="td('dpp.integration.sortOrder', 'Sort Rule')" align="left" prop="order">
                    <template #default="scope">
                        <el-select v-model="scope.row.order" :placeholder="td('common.form.statusPlaceholder', 'Please select status')">
                            <el-option :label="td('dpp.integration.descending', 'Descending')" value="desc" />
                            <el-option :label="td('dpp.integration.ascending', 'Ascending')" value="asc" />
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
                <el-button type="warning" @click="handleFetchFields" v-if="!info">{{ td('dpp.integration.fetchFields', 'Fetch Fields') }}</el-button>
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

const props = defineProps({
    visible: { type: Boolean, default: true },
    title: { type: String, default: '' },
    currentNode: { type: Object, default: () => ({}) },
    info: { type: Boolean, default: false },
    graph: { type: Object, default: () => ({}) },
    taskType: { type: String, default: '' },
});

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
        if (!form.value.code) {
            loading.value = true;
            const response = await getNodeUniqueKey({
                projectCode: userStore.projectCode || "133545087166112",
                projectId: userStore.projectId,
            });
            loading.value = false;
            form.value.code = response.data;
        }

        // Standardized tableFields
        tableFields.value = tableFields.value.map(item => ({
            ...item,
            order: item.order ?? 'asc',
        }));

        const taskParams = form.value?.taskParams || {};
        taskParams.tableFields = tableFields.value;
        // Construct outputFields = inputFields + enhanced value of tableFields
        taskParams.outputFields = inputFields.value.map(input => {
            const matched = tableFields.value.find(item => item.columnName === input.columnName);
            return matched ? { ...input, ...matched } : { ...input };
        });
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
    originalTableFieldsBackup.value = deepCopy(
        props.currentNode?.data?.taskParams?.inputFields || []
    );
    inputFields.value = props.currentNode?.data?.taskParams?.inputFields;
    tableFields.value = Array.isArray(props.currentNode?.data?.taskParams?.tableFields)
        ? props.currentNode?.data?.taskParams?.tableFields
            .filter(item => item && typeof item === 'object')
            .map(item => ({
                ...item,
                order: item.order ?? 'asc',
            }))
        : [];
    setSort()

});
</script>

<style scoped lang="less">
.blue-text {
    color: #2666fb;
}
</style>
