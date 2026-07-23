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
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button type="primary" plain @click="handleAdd" @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
                </el-button>
            </el-col>
        </el-row>
    </div>

    <!-- table part -->
    <el-table stripe height="300px" v-loading="loading" :data="dpCodeMapList">
        <el-table-column :label="td('dpp.integration.originalValue', 'Original Value')" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="originalValue">
            <template #default="scope">
                {{ scope.row.originalValue || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dpp.integration.dictName', 'Dictionary Name')" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="dictName">
            <template #default="scope">
                {{ scope.row.dictName || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('dpp.integration.dictValue', 'Dictionary Value')" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="dictValue">
            <template #default="scope">
                {{ scope.row.dictValue || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right">
            <template #default="scope">
                <!-- Pass the row index when modifying, for subsequent local editing -->
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row, scope.$index)">{{ td('common.button.update') }}</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.$index)">{{ td('common.button.delete') }}</el-button>
            </template>
        </el-table-column>
        <template #empty>
            <div class="emptyBg">
                <p>{{ td('common.message.noData', 'No data') }}</p>
            </div>
        </template>
    </el-table>

    <!-- Add/modify dialog box -->
    <el-dialog :title="title" v-model="open" :append-to="$refs['app-container']" draggable destroy-on-close>
        <el-form ref="dpCodeMapRef" :model="form" :rules="rules" label-width="80px" :label-position="labelPosition">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.originalValue', 'Original Value')" prop="originalValue" :label-position="labelPosition">
                        <el-input v-model="form.originalValue" :placeholder="td('dpp.integration.originalValuePlaceholder', 'Please enter original value')" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.dictName', 'Dictionary Name')" prop="dictName" :label-position="labelPosition">
                        <el-input v-model="form.dictName" :placeholder="td('dpp.integration.dictNamePlaceholder', 'Please enter dictionary name')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.dictValue', 'Dictionary Value')" prop="dictValue" :label-position="labelPosition">
                        <el-input v-model="form.dictValue" :placeholder="td('dpp.integration.dictValuePlaceholder', 'Code value')" />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="cancel">{{ td('common.button.cancel') }}</el-button>
                <el-button type="primary" size="mini" @click="submitForm">{{ td('common.button.confirm') }}</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const props = defineProps({
    row: { type: Object, default: () => ({}) },
});
function cancel() {
    open.value = false;
    openDetail.value = false;
    reset();
}

const dpCodeMapList = ref(props.row || []);

watch(
    () => props.row,
    (newValue) => {
        dpCodeMapList.value = newValue || []; // If the new value is undefined or null, assign an empty array
    },
    { deep: true }
);

// Other state variables
const loading = ref(false);
const total = ref(dpCodeMapList.value.length);
const open = ref(false);
const title = ref('');
// Forms and validation rules
const data = reactive({
    oldOriginalValue: null,
    form: {
        originalValue: null,
        dictName: null,
        dictValue: null,
    },
    rules: {
        originalValue: [{ required: true, message: td('dpp.integration.originalValueRequired', 'Original value is required'), trigger: 'change' }],
        dictName: [{ required: true, message: td('dpp.integration.dictNameRequired', 'Code name is required'), trigger: 'change' }],
        dictValue: [{ required: true, message: td('dpp.integration.dictValueRequired', 'Dictionary value is required'), trigger: 'change' }]
    }
});

const { oldOriginalValue, form, rules } = toRefs(data);
const emit = defineEmits(["dpCodeMapList",]);
/** form reset */
function reset() {
    form.value = { index: null, id: null, originalValue: null, dictName: null, dictValue: null };
    oldOriginalValue.value = null;
    // proxy.resetForm('dpCodeMapRef');
}

/** Add button operation */
function handleAdd() {
    reset();
    open.value = true;
    title.value = td('common.button.add');
}

/** Modify button actions */
function handleUpdate(row, index) {
    reset();
    form.value = { ...row, index };
    oldOriginalValue.value = row.originalValue;
    open.value = true;
    title.value = td('common.button.update');
}

/** Delete button action */
function handleDelete(index) {
    proxy.$modal.confirm(td('dpp.integration.confirmDeleteData', 'Are you sure to delete this data?'))
        .then(() => {
            dpCodeMapList.value.splice(index, 1);
            total.value = dpCodeMapList.value.length;
            proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
            emit('dpCodeMapList', dpCodeMapList.value);
        })
        .catch(() => { });
}

/** Submit button: add or modify */
function submitForm() {
    proxy.$refs['dpCodeMapRef'].validate((valid) => {
        if (valid) {
            // Check if originalValue already exists
            const isDuplicate = dpCodeMapList.value.some(item => item.originalValue === form.value.originalValue);
            if (!(oldOriginalValue.value !== null && oldOriginalValue.value === form.value.originalValue) && isDuplicate) {
                proxy.$modal.msgWarning(td('dpp.integration.originalValueDuplicate', 'Original value already exists, cannot add'));
                return; // Prevent execution from continuing
            }

            // If it is a modification operation
            if (form.value.index !== null && form.value.index !== undefined) {
                dpCodeMapList.value.splice(form.value.index, 1, { ...form.value });
                proxy.$modal.msgSuccess(td('common.message.editSuccess'));
            } else {
                dpCodeMapList.value.push({ ...form.value });
                proxy.$modal.msgSuccess(td('common.message.addSuccess'));
            }
            emit('dpCodeMapList', dpCodeMapList.value);

            open.value = false;
            total.value = dpCodeMapList.value.length;
        }
    });
}
</script>
