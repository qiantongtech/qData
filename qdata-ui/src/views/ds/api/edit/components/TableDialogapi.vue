<template>
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="70%" style="min-height:600px;"
        :close-on-click-modal="false" append-to-body @close="handleClose">
        <el-table v-if="apiServiceType == 3" :height="tableHeight" :data="tableData" row-key="id" ref="multipleTable"
                  stripe tooltip-effect="dark" default-expand-all style="width: 100%; margin: 15px 0;"
            :tree-props="{ children: 'daAssetApiParamList', hasChildren: 'hasChildren' }" :check-strictly="false"
            :selectable="checkSelectable" @selection-change="handleSelectionChange">
            <!-- Selection Column with Condition -->
            <el-table-column type="selection" width="55" :reserve-selection="true" :selectable="checkSelectable" />
            <el-table-column label="序号" width="80" align="center" fixed="left">
                <template #default="{ $index }">
                    {{ $index + 1 }}
                </template>
            </el-table-column>
            <el-table-column prop="name" label="参数名称" align="center" :show-overflow-tooltip="{effect: 'light'}" fixed="left" />
            <el-table-column prop="remark" label="描述" align="center" :show-overflow-tooltip="{effect: 'light'}" fixed="left" />
            <el-table-column label="数据类型" fixed="left" align="center" prop="columnType" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="{ row }">
                    {{ row.columnType || '-' }}
                </template>
            </el-table-column>
            <el-table-column prop="exampleValue" label="示例值" width="120" align="center" :show-overflow-tooltip="{effect: 'light'}" fixed="left" />
            <el-table-column prop="requestFlag" label="是否允许为空" align="center" :show-overflow-tooltip="{effect: 'light'}" fixed="left">
                <template #default="scope">
                    <span>{{ scope.row.requestFlag == '1' ? '是' : '否' }}</span>
                </template>
            </el-table-column>
        </el-table>

        <span slot="footer" class="dialog-footer">
            <el-button @click="handleClose">取消</el-button>
            <el-button type="primary" @click="confirm">确定</el-button>
        </span>
    </el-dialog>
</template>

<script setup name="AddList">
const { proxy } = getCurrentInstance();

const props = defineProps({
    dialogTitle: String,
    visible: Boolean,
    tableData: Array,
    list: Array,
    apiServiceType: String,
    inputList: Array
});

const data = reactive({
    isInitialized: false,
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
    AddListRows: [],
    firstDialogVisible: false,
    secondDialogVisible: false,
    sortDialogVisible: false,
    isShowTooltip: false,
    filteredTableOptions: [],
});

const {
    queryParams, AddListRows, tableHeight, loading, isInitialized,
    checkedTableColumns, total, firstDialogVisible, secondDialogVisible,
    sortDialogVisible, isShowTooltip, filteredTableOptions
} = toRefs(data);

const dialogVisible = computed({
    get: () => props.visible,
    set: (newValue) => {
        if (!newValue) {
            handleClose();
        }
    }
});

function handleClose() {
    proxy.$emit("close");
    isInitialized.value = false;
    AddListRows.value = [];
}

function confirm() {
    // Emit selected rows to parent component
    console.log("🚀 ~ confirm ~ AddListRows.value:", AddListRows.value);
    proxy.$emit("confirm", AddListRows.value);
}

// 处理选择变化事件，更新 AddListRows
function handleSelectionChange(selectedRows) {
    AddListRows.value = selectedRows;
}

// 回显选中行
function echoSelected() {
    proxy.$nextTick(() => {
        proxy.$refs.multipleTable.clearSelection();
        props.tableData.forEach(item => {
            if (AddListRows.value.some(row => row.id == item.id)) {
                proxy.$refs.multipleTable.toggleRowSelection(item, true);
            }
        });
        isInitialized.value = true;
    });
}

function checkSelectable(row) {
    return row.parentId === null; // 仅第一级可以选择
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
