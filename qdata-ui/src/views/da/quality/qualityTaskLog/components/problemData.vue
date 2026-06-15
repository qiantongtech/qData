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

<!-- problemData.vue -->
<template>
    <el-dialog v-model="visible" :title="td('da.qualityTaskLog.problemData.title')" class="medium-dialog" destroy-on-close>
        <div class="dialog-header">
            <el-form :inline="true" @selection-change="handleSelectionChange"
                style="display: inline-block; text-align: left;">
                <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
                    @submit.prevent>
                    <el-form-item v-for="(fieldName, index) in visibleFields" :key="index"
                        :label="getFieldLabel(fieldName)" :prop="'keyWordData.' + fieldName">
                        <el-input v-model="queryParams.keyWordData[fieldName]"
                            :placeholder="`${td('da.qualityTaskLog.inputPlaceholder')} ${getFieldLabel(fieldName)}`" clearable class="el-form-input-width" />
                    </el-form-item>

                    <template v-if="showMoreFields">
                        <el-form-item v-for="(fieldName, index) in moreFields" :key="'more-' + index"
                            :label="getFieldLabel(fieldName)" :prop="'keyWordData.' + fieldName">
                            <el-input v-model="queryParams.keyWordData[fieldName]"
                                :placeholder="`${td('da.qualityTaskLog.inputPlaceholder')} ${getFieldLabel(fieldName)}`" clearable
                                class="el-form-input-width" />
                        </el-form-item>
                    </template>
                    <!-- 更多/收起按钮 -->
                    <el-form-item v-if="columnFields.length > 3">
                        <el-button type="text" @click="toggleMore">
                            {{ showMoreFields ? td('da.qualityTaskLog.problemData.collapse') : td('da.qualityTaskLog.problemData.more') }}
                            <el-icon>
                                <ArrowUp v-if="showMoreFields" />
                                <ArrowDown v-else />
                            </el-icon>
                        </el-button>
                    </el-form-item>

                    <!-- 查询/重置按钮 -->
                    <el-form-item>
                        <el-button plain type="primary" @click="handleQuery" @mousedown.prevent>
                            <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
                        </el-button>
                        <el-button @click="resetQuery" @mousedown.prevent>
                            <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
                        </el-button>
                    </el-form-item>
                </el-form>
            </el-form>
            <el-row align="middle" justify="space-between" style="width: 100%">
                <el-col :span="12">
                    <el-space>
                        <el-button link type="danger" :icon="SemiSelect" :disabled="selectedRows.length === 0"
                            @click="addIgnore">
                            {{ td('da.qualityTaskLog.problemData.batchIgnore') }}
                        </el-button>
                        <el-button link type="info" :icon="SemiSelect">{{ td('da.qualityTaskLog.problemData.batchReFix') }}</el-button>
                        <el-button link type="info" :icon="RefreshLeft">{{ td('da.qualityTaskLog.problemData.batchRevokeIgnored') }}</el-button>
                        <el-button link type="info" :icon="RefreshLeft">{{ td('da.qualityTaskLog.problemData.batchRevokeDeferred') }}</el-button>
                        <el-button link type="info" :icon="RefreshLeft">{{ td('da.qualityTaskLog.problemData.batchReturn') }}</el-button>
                    </el-space>
                </el-col>
            </el-row>
        </div>

        <el-table :data="filteredData" border style="width: 100%; " @selection-change="handleSelectionChange"
            height="55vh" v-loading="loading">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column :label="td('da.qualityTaskLog.problemData.problemData')" align="center" width="300" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="scope">
                    <div class="data-field" :title="td('da.qualityTaskLog.problemData.viewAll')" @click="openDetails(scope.row)">
                        <div v-for="(item, index) in getLabelAndValueFromStr(obj.columnName, obj.rule, scope.row.dataJsonStr)"
                            :key="index">
                            {{ item }}
                        </div>
                    </div>
                </template>
            </el-table-column>
            <!-- 评测字段列 -->
            <el-table-column :label="td('da.qualityTaskLog.problemData.evaluationField')" align="center" :show-overflow-tooltip="{effect: 'light'}" width="300">
                <template #default="scope">
                    <div v-for="(item, index) in getLabelAndValueFromStr(obj.columnName, obj.rule, scope.row.dataJsonStr)"
                        :key="index">
                        {{ item }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column :label="td('da.qualityTaskLog.problemData.severity')" align="center" prop="warningLevel" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="scope">
                    <dict-tag :options="quality_warning_status" :value="obj?.rule?.warningLevel" />
                </template>

            </el-table-column>
            <el-table-column :label="td('da.qualityTaskLog.problemData.isFixed')" align="center" prop="repair" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="scope">
                    <dict-tag :options="quality_log_data_repair" :value="scope.row.repair" /></template>
            </el-table-column>
            <el-table-column :label="td('da.qualityTaskLog.problemData.errorDesc')" align="center" prop="errDescription" :show-overflow-tooltip="{effect: 'light'}" width="300">
                <template #default="scope">

                    {{ obj.rule.errDescription || '-' }}
                </template>
            </el-table-column>

            <el-table-column :label="td('da.qualityTaskLog.problemData.fixSuggestion')" align="center" prop="suggestion" :show-overflow-tooltip="{effect: 'light'}" width="300">
                <template #default="scope">
                    {{ obj.rule?.suggestion || '-' }}
                </template>
            </el-table-column>

            <el-table-column :label="td('da.qualityTaskLog.problemData.evaluationDate')" align="center" prop="time" width="100">
                <template #default="scope">
                    {{ scope.row.time ? moment(scope.row.time).format('YYYY-MM-DD') : '-' }}
                </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.remark')" align="center" prop="remark" :show-overflow-tooltip="{effect: 'light'}" width="300">
                <template #default="scope">
                    {{ scope.row.remark || '-' }}
                </template>
            </el-table-column>
            <el-table-column :label="td('common.texts.operation')" align="center" width="240" fixed="right">
                <template #default="scope">
                    <el-button link type="primary" v-if="scope.row.repair != 2" @click="addIgnore(scope.row)"
                        icon="CircleClose">
                        {{ td('da.qualityTaskLog.problemData.ignore') }}
                    </el-button>

                    <el-button link type="primary" @click="addComment(scope.row)" icon="Edit">
                        {{ td('da.qualityTaskLog.problemData.addRemark') }}
                    </el-button>

                    <el-button link type="primary" @click="openupDetails(scope.row)" icon="Tools"
                        v-if="scope.row.repair == 0">
                        {{ td('da.qualityTaskLog.problemData.handle') }}
                    </el-button>
                </template>

            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        <template #footer>
            <el-button @click="close">{{ td('common.button.close') }}</el-button>
        </template>
        <ProblemDataDetailDialog ref="detailDialogRef" />
        <updateDataDialog ref="updateDialogRef" @ok="onSave" />
        <el-dialog v-model="commentDialogVisible" :title="td('da.qualityTaskLog.problemData.remarkTitle')" width="800px" @close="resetComment">
            <el-form :model="commentForm" label-width="80px">
                <el-form-item :label="td('common.texts.remark')">
                    <el-input type="textarea" v-model="commentForm.comment" rows="4" :placeholder="td('da.qualityTaskLog.problemData.remarkPlaceholder')" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="commentDialogVisible = false">{{ td('common.button.cancel') }}</el-button>
                <el-button type="primary" @click="submitComment">{{ td('common.button.save') }}</el-button>
            </template>
        </el-dialog>

    </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
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
import updateDataDialog from './problemDataEdit.vue'
import ProblemDataDetailDialog from './problemDataDetail.vue'
import { pageErrorData, updateErrorData } from "@/api/da/quality/qualityTaskLog"

const { td } = useDefaultLang();
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

const showMoreFields = ref(false)

const visibleFields = computed(() => columnFields.value.slice(0, 3))

const moreFields = computed(() => columnFields.value.slice(3))

function toggleMore() {
    showMoreFields.value = !showMoreFields.value
}
function getFieldLabel(fieldName) {
    const evaCols = obj.value.rule?.evaColumns
    if (!evaCols) return fieldName

    const match = Array.isArray(evaCols)
        ? evaCols.find(col => col.name?.toLowerCase?.() === fieldName.toLowerCase())
        : Object.values(evaCols).find(col => col.name?.toLowerCase?.() === fieldName.toLowerCase())

    return match?.name || fieldName
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
        ? td('da.qualityTaskLog.problemData.confirmBatchIgnore', { count: errorDataId.length })
        : td('da.qualityTaskLog.problemData.confirmSingleIgnore')

    proxy.$modal.confirm(message, td('common.message.prompt'), {
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
        ElMessage.warning(td('da.qualityTaskLog.problemData.remarkRequired'))
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
