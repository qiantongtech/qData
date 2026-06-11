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
    <el-dialog v-model="visibleDialog" draggable width="500px" class="excelUploadDialog-2025-03-28-17-05" :title="title"
        destroy-on-close>
        <el-form ref="daDiscoveryTaskRef" :model="form" label-width="90px" @submit.prevent>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="字段名称" prop="columnName"
                        :rules="[{ required: true, message: '请输入字段名称', trigger: 'blur' }]">
                        <el-input v-model="form.columnName" placeholder="请输入字段名称" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="字段类型" prop="columnType"
                        :rules="[{ required: true, message: '请选择字段类型', trigger: 'change' }]">
                        <el-select v-model="form.columnType" placeholder="请选择字段类型">
                            <el-option v-for="dict in columntype" :key="dict.value" :label="dict.label"
                                :value="dict.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24" v-if="form.columnType == 'date'">
                    <el-form-item label="日期格式" prop="format"
                        :rules="[{ required: true, message: '请输入日期格式', trigger: 'change' }]">
                        <el-input v-model="form.format" placeholder="日期格式如yyyy/MM/dd" />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>

        <template #footer>
            <div style="text-align: right">
                <el-button @click="closeDialog">{{ t('common.button.close') }}</el-button>
                <el-button type="primary" @click="saveData">{{ t('common.button.save') }}</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { defineProps, defineEmits, ref, computed, watch } from 'vue';

const { t } = useI18n();
const { proxy } = getCurrentInstance();
const { column_type } = proxy.useDict('column_type');

const props = defineProps({
    visible: { type: Boolean, default: true },
    title: { type: String, default: '' },
    data: { type: Object, default: () => ({}) }
});

const emit = defineEmits(['update:visible', 'confirm']);
// 定义字段类型数组
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

// 计算属性处理 v-model
const visibleDialog = computed({
    get() {
        return props.visible;
    },
    set(newValue) {
        emit('update:visible', newValue);
    }
});

// 关闭对话框的方法
const closeDialog = () => {
    emit('update:visible', false);
};
let daDiscoveryTaskRef = ref();
// 保存数据的方法
const saveData = () => {
    daDiscoveryTaskRef.value.validate((valid) => {
        if (valid) {
            if (form.value.columnType !== 'date') {
                form.value.format = '';
            }
            emit('confirm', form.value);
            emit('update:visible', false);
        } else {

            console.log('表单校验未通过');
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
