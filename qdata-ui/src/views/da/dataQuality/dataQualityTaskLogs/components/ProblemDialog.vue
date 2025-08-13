<!-- ProblemDialog.vue -->
<template>
    <el-dialog v-model="visible" title="问题数据处理" class="medium-dialog" destroy-on-close>
        <div class="dialog-header">
            <el-form :inline="true" label-width="120px" @selection-change="handleSelectionChange"
                style="display: inline-block; text-align: left;">
                <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
                    @submit.prevent>
                    <el-form-item v-for="(fieldName, index) in columnFields" :key="index"
                        :label="getFieldLabel(fieldName)" :prop="'keyWordData.' + fieldName">
                        <el-input v-model="queryParams.keyWordData[fieldName]"
                            :placeholder="`请输入 ${getFieldLabel(fieldName)}`" clearable class="el-form-input-width" />
                    </el-form-item>
                    <el-form-item>
                        <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                        </el-button>
                        <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
                        </el-button>
                    </el-form-item>
                </el-form>
            </el-form>
            <el-row align="middle" justify="space-between" style="width: 100%">
                <el-col :span="12">
                    <el-space>
                        <el-button link type="danger" :icon="SemiSelect" :disabled="selectedRows.length === 0"
                            @click="addIgnore">
                            批量忽略问题
                        </el-button>
                        <el-button link type="info" :icon="SemiSelect">批量重新修复</el-button>
                        <el-button link type="info" :icon="RefreshLeft">批量撤销已忽略</el-button>
                        <el-button link type="info" :icon="RefreshLeft">批量撤销暂缓修复</el-button>
                        <el-button link type="info" :icon="RefreshLeft">批量退回问题处理</el-button>
                    </el-space>
                </el-col>
            </el-row>
        </div>

        <el-table :data="filteredData" border style="width: 100%; " @selection-change="handleSelectionChange"
            height="55vh" v-loading="loading">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="问题数据" align="center" width="300" show-overflow-tooltip>
                <template #default="scope">
                    <div class="data-field" title="点击查看全部" @click="openDetails(scope.row)">
                        <div v-for="(item, index) in getLabelAndValueFromStr(obj.columnName, obj.rule, scope.row.dataJsonStr)"
                            :key="index">
                            {{ item }}
                        </div>
                    </div>
                </template>
            </el-table-column>
            <!-- 评测字段列 -->
            <el-table-column label="评测字段" align="center" show-overflow-tooltip width="300">
                <template #default="scope">
                    <div v-for="(item, index) in getLabelAndValueFromStr(obj.columnName, obj.rule, scope.row.dataJsonStr)"
                        :key="index">
                        {{ item }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="严重程度" align="center" prop="warningLevel" show-overflow-tooltip>
                <template #default="scope">
                    <dict-tag :options="quality_warning_status" :value="obj?.rule?.warningLevel" />
                </template>

            </el-table-column>
            <el-table-column label="是否已修复" align="center" prop="repair" show-overflow-tooltip>
                <template #default="scope">
                    <dict-tag :options="quality_log_data_repair" :value="scope.row.repair" /></template>
            </el-table-column>
            <el-table-column label="错误描述" align="center" prop="errDescription" show-overflow-tooltip width="300">
                <template #default="scope">

                    {{ obj.rule.errDescription || '-' }}
                </template>
            </el-table-column>

            <el-table-column label="修复建议" align="center" prop="suggestion" show-overflow-tooltip width="300">
                <template #default="scope">
                    {{ obj.rule?.suggestion || '-' }}
                </template>
            </el-table-column>

            <el-table-column label="评测日期" align="center" prop="time" width="100">
                <template #default="scope">
                    {{ scope.row.time ? moment(scope.row.time).format('YYYY-MM-DD') : '-' }}
                </template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip width="300">
                <template #default="scope">
                    {{ scope.row.remark || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="240" fixed="right">
                <template #default="scope">
                    <el-button link type="primary" v-if="scope.row.repair != 2" @click="addIgnore(scope.row)"
                        icon="CircleClose">
                        忽略
                    </el-button>

                    <el-button link type="primary" @click="addComment(scope.row)" icon="Edit">
                        添加备注
                    </el-button>

                    <el-button link type="primary" @click="openupDetails(scope.row)" v-if="scope.row.repair == 0"
                        icon="Tools">
                        处理
                    </el-button>
                </template>

            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        <template #footer>
            <el-button @click="close">关闭</el-button>
        </template>
        <ProblemDataDetailDialog ref="detailDialogRef" />
        <UpdateDataDaiol ref="updateDialogRef" @ok="onSave" />
        <el-dialog v-model="commentDialogVisible" title="添加备注" width="800px" @close="resetComment">
            <el-form :model="commentForm" label-width="80px">
                <el-form-item label="备注">
                    <el-input type="textarea" v-model="commentForm.comment" rows="4" placeholder="请输入备注内容" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="commentDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitComment">保存</el-button>
            </template>
        </el-dialog>

    </el-dialog>
</template>

<script setup>
import {
    SemiSelect,
    RefreshLeft,
    ArrowUp,
    ArrowDown,
    Search,
    InfoFilled,
} from '@element-plus/icons-vue'
import moment from 'moment'
import { ref, computed, getCurrentInstance } from 'vue'
import UpdateDataDaiol from './UpdateDataDaiol.vue'
import ProblemDataDetailDialog from './ProblemDataDetailDialog.vue'
import { pageErrorData, updateErrorData } from "@/api/da/dppQuality/DppQualityLog"

const { proxy } = getCurrentInstance()
const { quality_warning_status, quality_log_data_repair } = proxy.useDict(
    'quality_warning_status',
    'quality_log_data_repair'
)
// 解析 columnName 为数组
const columnFields = computed(() => {
    return obj.value.columnName
        ? obj.value.columnName.split(',').map(name => name.trim())
        : []
})

function getFieldLabel(fieldName) {
    const evaCols = obj.value.rule?.evaColumns
    if (!evaCols) return fieldName

    const match = Array.isArray(evaCols)
        ? evaCols.find(col => col.name?.toLowerCase?.() === fieldName.toLowerCase())
        : Object.values(evaCols).find(col => col.name?.toLowerCase?.() === fieldName.toLowerCase())

    return match?.label || fieldName
}
async function handleUpdateErrorData(params) {
    loading.value = true
    try {
        const res = await updateErrorData(params)
        if (res?.data) {
            await getList()
        }
    } finally {
        loading.value = false
    }
}


let row = ref(null)
let obj = ref({})
const detailDialogRef = ref(null)
const updateDialogRef = ref(null)
const commentDialogVisible = ref(false)
const commentForm = ref({ comment: '' })
let commentTarget = ref(null)
let total = ref(0)
let loading = ref(false)
const visible = ref(false)
const search = ref('')
const selectedRows = ref([])
const showDetailsDialog = ref(false)
const detailData = ref({})
const advancedVisible = ref(false)
const filters = ref({
    position: '',
    severity: '',
})
const tableData = ref([])

// 查询参数
let queryParams = ref({
    pageNum: 1,
    pageSize: 10,
    id: '',
    keyWordData: '',
})

// 搜索和重置
function handleQuery() {
    queryParams.value.pageNum = 1
    getList()
}

function resetQuery() {
    proxy.resetForm('queryRef')
    handleQuery()
}

// 获取列表数据
async function getList() {
    loading.value = true
    try {
        const res = await pageErrorData(queryParams.value)
        console.log("🚀 ~ getList ~ queryParams.value:", queryParams.value)
        if (res.code == '200') {
            tableData.value = res.data.content || []
            total.value = res.data?.totalElements || 0
        }
    } finally {
        loading.value = false
    }
}

// selection 变化
function handleSelectionChange(val) {
    selectedRows.value = val.map(item => item.id)
}

// 忽略数据
async function addIgnore(row) {
    let errorDataId = row?.id ? [row.id] : selectedRows.value
    const isBatch = errorDataId.length > 1
    const message = isBatch
        ? `确认要忽略这 ${errorDataId.length} 条问题数据吗？`
        : '确认要忽略该条问题数据吗？'

    proxy.$modal.confirm(message, '提示', {
        dangerouslyUseHTMLString: true,
    }).then(() => {
        handleUpdateErrorData({
            errorDataId,
            reportId: obj.value.id,
            updateType: '3',
        })
    })
}

// 添加备注
function addComment(obj) {
    row.value = obj
    commentForm.value.comment = obj.remark
    commentDialogVisible.value = true
}

async function submitComment() {
    if (!commentForm.value.comment.trim()) {
        ElMessage.warning('请输入备注内容')
        return
    }

    await handleUpdateErrorData({
        id: row.value.id,
        reportId: obj.value.id,
        remark: commentForm.value.comment,
        updateType: '2',
    })

    commentDialogVisible.value = false
}

// 保存数据
async function onSave(updatedData, oldData, keyWordData) {
    await handleUpdateErrorData({
        keyWordData,
        updatedData,
        oldData,
        tableName: obj.value.tableName,
        datasourceId: obj.value.datasourceId,
        reportId: obj.value.id,
        updateType: '1',
    })
}

// 打开详情弹窗
async function openDetails(row) {
    detailDialogRef.value?.open(row, obj.value.rule)
}

async function openupDetails(row) {
    updateDialogRef.value?.open(row, obj.value.rule, obj.value.columnName)
}

// 高级筛选
const toggleAdvanced = () => {
    advancedVisible.value = !advancedVisible.value
}

const applyAdvancedFilters = () => {
    console.log('应用高级筛选条件：', filters.value)
}

const resetFilters = () => {
    filters.value = {
        position: '',
        severity: '',
    }
}

// 展示字段格式化
function getLabelAndValueFromStr(columnName, rule, dataJsonStr) {
    if (!columnName || !rule || !dataJsonStr) return ['-']

    let jsonData
    try {
        jsonData = JSON.parse(dataJsonStr)
    } catch (e) {
        console.warn('dataJsonStr 解析失败：', e)
        return ['-']
    }

    let evaColumns = []
    try {
        evaColumns = Array.isArray(rule.evaColumns)
            ? rule.evaColumns
            : Object.values(rule.evaColumns || {})
    } catch (err) {
        console.warn('rule.evaColumns 解析失败', err)
        return ['-']
    }

    const fieldNames = columnName.split(',').map(n => n.trim())

    return fieldNames.map(name => {
        const lowerName = name.toLowerCase()
        const match = evaColumns.find(col => col.name?.toLowerCase?.() === lowerName)
        const label = match?.label || name
        const value = Object.keys(jsonData).find(key => key.toLowerCase() === lowerName)
        const finalValue = value ? jsonData[value] : '-'
        return `${label}：${finalValue}`
    })
}

// 展开主弹窗
async function open(row) {
    obj.value = {
        ...row,
        rule: typeof row.rule === 'string' ? JSON.parse(row.rule) : row.rule,
    }
    // 初始化 keyWordData 中字段
    const fieldNames = obj.value.columnName?.split(',') || []
    const keyWordData = {}
    fieldNames.forEach(name => {
        keyWordData[name.trim()] = ''
    })
    queryParams.value.keyWordData = keyWordData
    queryParams.value.reportId = row.id
    getList()
    visible.value = true
}

const close = () => (visible.value = false)
const closeDetails = () => (showDetailsDialog.value = false)
const handleProcess = row => (row.processing = true)

const filteredData = computed(() => {
    return tableData.value.filter(item => {
        const matchesSearch =
            !search.value ||
            item.employeeName.includes(search.value) ||
            item.employeeId.includes(search.value)
        const matchesPosition = !filters.value.position || item.position.includes(filters.value.position)
        const matchesSeverity = !filters.value.severity || item.severity === filters.value.severity
        return matchesSearch && matchesPosition && matchesSeverity
    })
})

// 对外暴露方法
defineExpose({ open, close })
</script>

<style scoped>
.data-field {
    display: flex;
    flex-direction: column;
    align-items: center;
    color: #333;
    cursor: pointer;
    max-height: 80px;
    overflow-y: scroll;
    scrollbar-width: none;
    -ms-overflow-style: none;
    background-color: #c3dcfd;
    border-radius: 5px;
    padding: 8px;
    box-sizing: border-box;
    text-align: center;
}

.data-field::-webkit-scrollbar {
    display: none;
}


.popover-content {
    padding: 10px;
}

.popover-multi-line {
    max-width: 260px;
}

.popover-line {
    line-height: 24px;
    border-bottom: 1px solid #eee;
    padding: 4px 0;
    color: #333;
}

.popover-line:last-child {
    border-bottom: none;
}

.dialog-header {
    padding-bottom: 12px;
    border-bottom: 1px solid #ebeef5;
}

.advanced-filters {
    margin-top: 8px;
}

.filter-form {
    width: 100%;
}
</style>
