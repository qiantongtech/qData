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
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="70%" style="min-height:600px;"
        :close-on-click-modal="false" append-to-body @close="handleClose">
        <el-table :height="tableHeight" :data="tableData" ref="multipleTable" stripe tooltip-effect="dark"
            style="width: 100%; margin: 15px 0;" @selection-change="handleSelectionChange" show-selection>
            <el-table-column type="selection" width="55" :reserve-selection="true">
            </el-table-column>
            <el-table-column prop="sortOrder" label="序号" width="80" align="center" />
            <el-table-column prop="engName" label="列名" align="center" width="200" :show-overflow-tooltip="{effect: 'light'}" />
            <el-table-column prop="columnType" label="数据类型" align="center" width="120" :show-overflow-tooltip="{effect: 'light'}" />
            <el-table-column prop="columnLength" label="数据长度" width="90" align="center" :show-overflow-tooltip="{effect: 'light'}" />
            <!--            <el-table-column prop="dataPrecision" label="数据精度" align="center" :show-overflow-tooltip="{effect: 'light'}" />-->
            <el-table-column prop="columnScale" label="数据小数位" width="100" align="center" :show-overflow-tooltip="{effect: 'light'}" />
            <el-table-column prop="pkFlag" label="主键" align="center" width="100" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="scope">
                    <span v-if="scope.row.pkFlag === '1'">是</span>
                    <span v-if="scope.row.pkFlag === '0'">否</span>
                </template>
            </el-table-column>
            <el-table-column prop="nullableFlag" label="允许为空" align="center" width="100" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="scope">
                    <span v-if="scope.row.nullableFlag === '1'">是</span>
                    <span v-if="scope.row.nullableFlag === '0'">否</span>
                </template>
            </el-table-column>
            <el-table-column prop="defaultValue" label="列默认值" width="100" align="center" :show-overflow-tooltip="{effect: 'light'}" >
                <template #default="scope">
                    {{scope.row.defaultValue || "-"}}
                </template>
            </el-table-column>
            <el-table-column prop="cnName" label="列备注" align="center" :show-overflow-tooltip="{effect: 'light'}" />
        </el-table>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="handleClose">取消</el-button>
                <el-button type="primary" @click="confirm">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="AddList">
const { proxy } = getCurrentInstance();

const props = defineProps({
    dialogTitle: {
        type: String,
        default: "表格数据",
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
