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
    <el-dialog v-model="visibleDialog" draggable width="500px" class="excelUploadDialog-2025-03-28-17-05" :title="title"
        destroy-on-close>
        <el-form ref="daDiscoveryTaskRef" :model="form" label-width="90px" @submit.prevent :label-position="labelPosition">
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="td('dpp.integration.fieldName', 'Field Name')" prop="columnName"
                        :rules="[{ required: true, message: td('dpp.integration.fieldNameRequired', 'Please enter field name'), trigger: 'blur' }]" :label-position="labelPosition">
                        <el-input v-model="form.columnName" :placeholder="td('dpp.integration.fieldNamePlaceholder', 'Please enter field name')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="td('dpp.integration.fieldType', 'Field Type')" prop="columnType"
                        :rules="[{ required: true, message: td('dpp.integration.fieldTypeRequired', 'Please select field type'), trigger: 'change' }]" :label-position="labelPosition">
                        <el-select v-model="form.columnType" :placeholder="td('dpp.integration.fieldTypePlaceholder', 'Please select field type')">
                            <el-option v-for="dict in columntype" :key="dict.value" :label="dict.label"
                                :value="dict.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24" v-if="form.columnType == 'date'">
                    <el-form-item :label="td('dpp.integration.dateFormat', 'Date Format')" prop="format"
                        :rules="[{ required: true, message: td('dpp.integration.dateFormatRequired', 'Please enter date format'), trigger: 'change' }]" :label-position="labelPosition">
                        <el-input v-model="form.format" :placeholder="td('dpp.integration.dateFormatPlaceholder', 'Date format e.g. yyyy/MM/dd')" />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>

        <template #footer>
            <div style="text-align: right">
                <el-button @click="closeDialog">{{ td('common.button.close') }}</el-button>
                <el-button type="primary" @click="saveData">{{ td('common.button.save') }}</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { defineProps, defineEmits, ref, computed, watch } from 'vue';

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { column_type } = proxy.useDict('column_type');

const props = defineProps({
    visible: { type: Boolean, default: true },
    title: { type: String, default: '' },
    data: { type: Object, default: () => ({}) }
});

const emit = defineEmits(['update:visible', 'confirm']);
// Define field type array
const columntype = [
    { value: 'long', label: 'long' },
    { value: 'boolean', label: 'boolean' },
    { value: 'string', label: 'string' },
    { value: 'date', label: 'date' },
    { value: 'double', label: 'double' }
];
const form = ref({
    name: '',
    catCode: '',
    executionType: 'PARALLEL',
    crontab: '',
    releaseState: 0,
    description: '',
    contactNumber: '',
    catCode: '',
    personCharge: ''
});

watch(
    () => props.visible,
    (newVal) => {
        if (newVal) {
            form.value = JSON.parse(JSON.stringify(props.data || {}));
            console.log('🚀 ~ form.value:', props.data);
        } else {
            proxy.resetForm('daDiscoveryTaskRef');
        }
        console.log('🚀 ~ props.data:', props);
    }
);

// Computed property handling v-model
const visibleDialog = computed({
    get() {
        return props.visible;
    },
    set(newValue) {
        emit('update:visible', newValue);
    }
});

// How to close a dialog box
const closeDialog = () => {
    emit('update:visible', false);
};
let daDiscoveryTaskRef = ref();
// How to save data
const saveData = () => {
    daDiscoveryTaskRef.value.validate((valid) => {
        if (valid) {
            if (form.value.columnType !== 'date') {
                form.value.format = '';
            }
            emit('confirm', form.value);
            emit('update:visible', false);
        } else {

            console.log("Form validation failed");
        }
    });
};
</script>
<style lang="scss">
.excelUploadDialog-2025-03-28-17-05 {
    .el-dialog__body {
        overflow: auto;
        height: 250px !important;
        padding: 20px 40px !important;
    }
}
</style>
