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
    <el-dialog :title="computedTitle" v-model="dialogVisible" width="70%" style="min-height:600px;"
        :close-on-click-modal="false" append-to-body @close="handleClose">
        <el-table :height="tableHeight" :data="tableData" ref="multipleTable" stripe tooltip-effect="dark"
            style="width: 100%; margin: 15px 0;" @selection-change="handleSelectionChange" show-selection>
            <el-table-column type="selection" width="55" :reserve-selection="true">
            </el-table-column>
            <el-table-column prop="sortOrder" :label="td('ds.apiEdit.parameter.tableDialogColumn.serialNumber')" width="80" align="center" />
            <el-table-column prop="engName" :label="td('ds.apiEdit.parameter.tableDialogColumn.columnName')" align="center" width="200" :show-overflow-tooltip="{effect: 'light'}" />
            <el-table-column prop="columnType" :label="td('ds.apiEdit.parameter.tableDialogColumn.dataType')" align="center" width="120" :show-overflow-tooltip="{effect: 'light'}" />
            <el-table-column prop="columnLength" :label="td('ds.apiEdit.parameter.tableDialogColumn.dataLength')" width="90" align="center" :show-overflow-tooltip="{effect: 'light'}" />
            <!--            <el-table-column prop="dataPrecision" label="数据精度" align="center" :show-overflow-tooltip="{effect: 'light'}" />-->
            <el-table-column prop="columnScale" :label="td('ds.apiEdit.parameter.tableDialogColumn.dataDecimal')" width="100" align="center" :show-overflow-tooltip="{effect: 'light'}" />
            <el-table-column prop="pkFlag" :label="td('ds.apiEdit.parameter.tableDialogColumn.primaryKey')" align="center" width="100" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="scope">
                    <span v-if="scope.row.pkFlag === '1'">{{td('ds.apiEdit.parameter.tableDialogColumn.yes')}}</span>
                    <span v-if="scope.row.pkFlag === '0'">{{td('ds.apiEdit.parameter.tableDialogColumn.no')}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="nullableFlag" :label="td('ds.apiEdit.parameter.tableDialogColumn.allowNull')" align="center" width="100" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="scope">
                    <span v-if="scope.row.nullableFlag === '1'">{{td('ds.apiEdit.parameter.tableDialogColumn.yes')}}</span>
                    <span v-if="scope.row.nullableFlag === '0'">{{td('ds.apiEdit.parameter.tableDialogColumn.no')}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="defaultValue" :label="td('ds.apiEdit.parameter.tableDialogColumn.columnDefault')" width="100" align="center" :show-overflow-tooltip="{effect: 'light'}" >
                <template #default="scope">
                    {{scope.row.defaultValue || "-"}}
                </template>
            </el-table-column>
            <el-table-column prop="cnName" :label="td('ds.apiEdit.parameter.tableDialogColumn.columnComment')" align="center" :show-overflow-tooltip="{effect: 'light'}" />
        </el-table>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="handleClose">{{ td('common.button.cancel') }}</el-button>
                <el-button type="primary" @click="confirm">{{ td('common.button.confirm') }}</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="AddList">
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

const props = defineProps({
    dialogTitle: {
        type: String,
        default: '',
    },
    visible: {
        type: Boolean,
        default: false,
    },
    tableData: {
        type: Array,
        default: () => [],
    },
    list: {
        type: Array,
        default: () => [],
    },
})

const data = reactive({
    isInitialized: false, // 标识是否已初始化选中项
    checkedTableColumns: [],
    total: 0,
    queryParams: {
        pageNum: 1,
        pageSize: 20,
        tableAlias: '',
        documentId: '',
        tableName: ""
    },
    loading: true,
    tableHeight: document.body.offsetHeight - 400 + 'px',
    AddListRows: [], lastSqlText: '', // 存储上次的 SQL 文本，用于检测是否发生变化
    firstDialogVisible: false,
    secondDialogVisible: false,
    sortDialogVisible: false,
    isShowTooltip: false,
    filteredTableOptions: [],
});
const { queryParams, AddListRows, tableHeight, loading, isInitialized,
    checkedTableColumns, total, firstDialogVisible, secondDialogVisible,
    sortDialogVisible, isShowTooltip, filteredTableOptions } = toRefs(data);

const computedTitle = computed(() => props.dialogTitle || td('ds.apiEdit.tableDialog.tableData'));

//添加计算属性
const dialogVisible = computed({
    get: () => props.visible,
    set: (newValue) => {
        console.log("dialogVisible", newValue)
        if (!newValue) {
            handleClose();
        }
    },

});
function handleClose() {
    proxy.$emit("close");
    isInitialized.value = false;
    AddListRows.value = [];
}
function echoSelected() { // 回显选中
    this.$nextTick(() => {
        proxy.$refs.multipleTable.clearSelection();
        tableData.forEach(item => {
            if (AddListRows.some(user => user.id === item.id)) {
                proxy.$refs.multipleTable.toggleRowSelection(item, true);
            }

        });

        isInitialized.value = true;
    });
}

function confirm() {
    proxy.$emit("confirm", AddListRows.value);
}
function handleSelectionChange(selectedRows) {
    if (isInitialized) {
        AddListRows.value = selectedRows;
    }
}

function resetQuery() {

}
</script>

<style scoped lang="scss">
.button-style-right {
    margin: -15px 15px 15px 15px;
    background-color: white;
    text-align: right;
    padding: 20px 0;
}
</style>