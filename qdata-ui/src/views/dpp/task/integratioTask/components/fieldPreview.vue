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
    <!-- Apply for service pop-up box -->
    <el-dialog :title="td('dpp.integration.fieldSourceTitle', 'Fields in step and their source')" v-model="open" width="800px" :append-to="$refs['app-container']" draggable
        destroy-on-close>
        <div class="info-line">
            <span class="label">{{ td('dpp.integration.stepName', 'Step Name:') }}</span>
            <span class="value">{{ form.name || '-' }}</span>
        </div>
        <el-divider content-position="center">
            <span class="blue-text">{{ title }}</span>
        </el-divider>
        <el-table stripe height="420px" :data="tableFields">
            <el-table-column :label="td('common.display.index', 'Index')" type="index" width="80" align="left">
                <template #default="scope">
                    <span>{{ scope.$index + 1 }}</span>
                </template>
            </el-table-column>
            <el-table-column :label="td('dpp.integration.fieldName', 'Field Name')" align="left" prop="columnName" :show-overflow-tooltip="{ effect: 'light' }" />
            <!-- <el-table-column label="Field comment" align="left" prop="description" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="scope">
                    {{ scope.row.description || "-" }}
                </template>
            </el-table-column> -->
            <el-table-column :label="td('dpp.integration.fieldType', 'Field Type')" align="left" prop="columnType" :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="scope">
                    {{ scope.row.columnType || "-" }}
                </template>
            </el-table-column>
            <el-table-column :label="td('dpp.column.fieldLength', 'Field Length')" align="left" prop="length" width="70">
                <template #default="scope">
                    {{ scope.row.length || "-" }}
                </template>
            </el-table-column>
            <el-table-column :label="td('dpp.integration.fieldPrecision', 'Field Precision')" align="left" prop="precision" width="70">
                <template #default="scope">
                    {{ scope.row.precision || "-" }}
                </template>
            </el-table-column>
            <el-table-column :label="td('dpp.integration.stepSource', 'Step Source')" align="left" prop="source" :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="scope">
                    {{ scope.row.source || "-" }}
                </template>
            </el-table-column>
        </el-table>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="cancel">{{ td('common.button.close') }}</el-button>
                <!-- <el-button type="primary" @click="submitForm">{{ td('common.button.confirm') }}</el-button> -->
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="RpApplyDialog">
import useDefaultLang from "@/composables/useDefaultLang"

const { td } = useDefaultLang();
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
