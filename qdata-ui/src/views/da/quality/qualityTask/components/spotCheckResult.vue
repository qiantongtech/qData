<template>
    <!-- 查看抽查结果弹窗 新增修改第三步 稽查规则信息  -->
    <el-dialog v-model="visible" title="查看抽查结果" width="1200px" :before-close="handleClose">
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
            <el-tab-pane label="问题数据" name="problem">
                <el-table :data="problemData" height="600px" stripe v-loading="loading">
                    <el-table-column v-for="col in tableColumns" :key="col.field" :prop="col.field" :min-width="'150px'"
                        :show-overflow-tooltip="{ effect: 'light' }" align="center">
                        <template #header>
                            <div class="column-header">
                                <div class="column-item">{{ col.en }}</div>
                                <div class="column-item">{{ col.cn }}</div>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="结果" prop="result" min-width="100px" align="center">
                        <template #default="{ row }">
                            <span style="color: red;">{{ row.result }}</span>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>

            <el-tab-pane label="正常数据" name="normal">
                <el-table :data="normalData" height="600px" stripe v-loading="loading">
                    <el-table-column v-for="col in tableColumns" :key="col.field" :prop="col.field" :min-width="'150px'"
                        :show-overflow-tooltip="{ effect: 'light' }" align="center">
                        <template #header>
                            <div class="column-header">
                                <div class="column-item">{{ col.en }}</div>
                                <div class="column-item">{{ col.cn }}</div>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="结果" prop="result" min-width="100px" align="center">
                        <template #default="{ row }">
                            <span style="color: green;">{{ row.result }}</span>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>
        </el-tabs>

        <template #footer>
            <el-button @click="visible = false">关闭</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { validationErrorDataSql, validationValidDataSql } from '@/api/da/quality/qualityTask'

const visible = ref(false)
const activeTab = ref('problem')
const loading = ref(false)

const tableColumns = ref([])
const problemData = ref([])
const normalData = ref([])
const currentForm = ref(null)

const dataLoaded = ref({
    problem: false,
    normal: false,
})

async function openDialog(form) {
    resetData()
    currentForm.value = form
    activeTab.value = 'problem'
    visible.value = true

    await loadData(form, 'problem')
}

async function handleTabClick(tab) {
    const tabName = tab.props.name
    if (!dataLoaded.value[tabName] && currentForm.value) {
        await loadData(currentForm.value, tabName)
    }
}

async function loadData(form, tabName = activeTab.value) {
    loading.value = true
    try {
        let res
        if (tabName == 'problem') {
            res = await validationErrorDataSql(form)
        } else {
            res = await validationValidDataSql({ ...form, pageNum: 1, pageSize: 999 })
        }

        if (
            res &&
            res.code == 200 &&
            res.data &&
            Array.isArray(res.data.showErrorColumns) &&
            res.data.showErrorColumns.length > 0
        ) {
            if (tableColumns.value.length == 0) {
                tableColumns.value = convertColumns(res.data.showErrorColumns)
            }

            const dataList = res.data.dataList || []
            const resultText = tabName == 'problem' ? '不通过' : '通过'

            if (tabName == 'problem') {
                problemData.value = convertData(dataList, resultText)
                dataLoaded.value.problem = true
            } else {
                normalData.value = convertData(dataList, resultText)
                dataLoaded.value.normal = true
            }
        } else {
            resetData()
        }
    } finally {
        loading.value = false
    }
}

function convertColumns(columns = []) {
    return (columns || [])
        .filter(col => typeof col?.colName == 'string' && col.colName.trim() != '')
        .map(col => {
            const colName = col.colName.trim()
            return {
                field: colName,
                en: colName,
                cn: col.colComment?.trim() || colName,
            }
        })
}
function convertData(data = [], resultText = '') {
    return data.map(row => ({
        ...row,
        result: resultText,
    }))
}

function resetData() {
    tableColumns.value = []
    problemData.value = []
    normalData.value = []
    dataLoaded.value.problem = false
    dataLoaded.value.normal = false
}

function handleClose() {
    visible.value = false
    resetData()
}

defineExpose({
    openDialog,
})
</script>

<style scoped>
.column-header {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.column-item {
    font-size: 12px;
    line-height: 18px;
    white-space: nowrap;
}

.el-table .cell {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    text-align: center;
}
</style>
