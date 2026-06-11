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
    <!-- 申请服务弹框 -->
    <el-dialog title="步骤里的字段和其来源" v-model="open" width="800px" :append-to="$refs['app-container']" draggable
        destroy-on-close>
        <div class="info-line">
            <span class="label">步骤名称：</span>
            <span class="value">{{ form.name || '-' }}</span>
        </div>
        <el-divider content-position="center">
            <span class="blue-text">{{ title }}</span>
        </el-divider>
        <el-table stripe height="420px" :data="tableFields">
            <el-table-column label="序号" type="index" width="80" align="left">
                <template #default="scope">
                    <span>{{ scope.$index + 1 }}</span>
                </template>
            </el-table-column>
            <el-table-column label="字段名称" align="left" prop="columnName" :show-overflow-tooltip="{ effect: 'light' }" />
            <!-- <el-table-column label="字段注释"  align="left" prop="description" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="scope">
                    {{ scope.row.description || "-" }}
                </template>
            </el-table-column> -->
            <el-table-column label="字段类型" align="left" prop="columnType" :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="scope">
                    {{ scope.row.columnType || "-" }}
                </template>
            </el-table-column>
            <el-table-column label="字段长度" align="left" prop="length" width="70">
                <template #default="scope">
                    {{ scope.row.length || "-" }}
                </template>
            </el-table-column>
            <el-table-column label="字段精度" align="left" prop="precision" width="70">
                <template #default="scope">
                    {{ scope.row.precision || "-" }}
                </template>
            </el-table-column>
            <el-table-column label="步骤来源" align="left" prop="source" :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="scope">
                    {{ scope.row.source || "-" }}
                </template>
            </el-table-column>
        </el-table>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="cancel">{{ t('common.button.close') }}</el-button>
                <!-- <el-button type="primary" @click="submitForm">{{ t('common.button.confirm') }}</el-button> -->
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="RpApplyDialog">
import { useI18n } from 'vue-i18n'

const { t } = useI18n();
const emit = defineEmits(['setLoading']);
const open = ref(false);
const cancel = () => {
    open.value = false;
    reset();
};
const tableFields = ref({});

let title = ref()
let form = ref({});
const show = async (data, node, tit) => {
    console.log("🚀 ~ show ~ node:", node.data.name)
    form.value.name = node.data.name
    console.log("🚀 ~ show ~     form.value.name :", form.value.name)
    tableFields.value = data;
    title.value = tit
    open.value = true;

};
defineExpose({ show });
// #endregion
</script>
<style lang="scss" scoped>
.blue-text {
    color: #2666fb;
}

.info-line {
    // padding: 8px 12px;
    font-size: 14px;
}

.label {
    color: #909399;
    font-weight: bold;
    margin-right: 8px;
}

.value {
    color: #303133;
}
</style>
