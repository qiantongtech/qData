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
    <!-- 稽查对象信息 新增修改弹窗 第二步 -->
    <el-dialog v-model="dialogVisible" draggable class="dialog" :title="dialogTitle" destroy-on-close width="800px"
        :append-to="$refs['app-container']">
        <el-form ref="formRef" :model="form" :rules="formRules" label-width="120px" @submit.prevent>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="稽查对象名称" prop="name">
                        <el-input v-model="form.name" placeholder="请输入稽查对象名称" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="源数据库连接" prop="datasourceId">
                        <el-select v-model="form.datasourceId" placeholder="请选择源数据库连接" filterable
                            @change="onDatasourceChange">
                            <el-option v-for="ds in datasourceOptions" :key="ds.id" :label="ds.datasourceName"
                                :value="ds.id" />
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="数据连接类型">
                        <el-input v-model="form.datasourceType" disabled placeholder="数据连接类型" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="数据连接实例">
                        <el-input v-model="form.dbname" disabled placeholder="数据连接实例" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">

                    <el-form-item label="选择表" prop="tableName">
                        <el-select v-model="form.tableName" filterable :loading="tableLoading" @change="onTableChange">
                            <el-option v-for="item in tableOptions" :key="item.tableName" :label="item.tableName"
                                :value="item.tableName" />
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="closeDialog">{{ t('common.button.cancel') }}</el-button>
                <el-button type="primary" @click="saveData">{{ t('common.button.confirm') }}</el-button>
            </div>
        </template>
    </el-dialog>

    <el-dialog title="Cron表达式生成器" v-model="cronDialogVisible" :append-to="$refs['app-container']" destroy-on-close>
        <crontab ref="crontabRef" :expression="expression" @hide="cronDialogVisible = false" @fill="crontabFill" />
    </el-dialog>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref, defineProps, defineEmits, getCurrentInstance } from 'vue'
import Crontab from '@/components/Crontab/index.vue'
import { getTablesByDataSourceId, getColumnByAssetId } from '@/api/dpp/task/index.js'
import { getDaDatasourceList } from '@/api/dp/model/model'

const { t } = useI18n();
const emit = defineEmits(['confirm'])
const { proxy } = getCurrentInstance()

const props = defineProps({
    title: { type: String, default: '表单标题' },
})

const dialogVisible = ref(false)
const cronDialogVisible = ref(false)

const formRef = ref()
const form = ref({})
const defaultForm = {
    name: "",
    datasourceId: "",
    datasourceType: "",
    datasourceName: "",
    datasourceConfig: "",
    ip: "",
    port: "",
    tableName: "",
    columnComment: "",
}

const resetForm = () => {
    Object.assign(form.value, JSON.parse(JSON.stringify(defaultForm)))
    tableOptions.value = []
    columnList.value = []
    formRef.value?.resetFields()
}

const formRules = {
    name: [{ required: true, message: '请输入稽查对象名称', trigger: 'change' }],
    datasourceId: [{ required: true, message: '请选择源数据库连接', trigger: 'change' }],
    tableName: [{ required: true, message: '请选择表', trigger: 'change' }]
}

function crontabFill(value) {
    form.value.cronExpression = value
}

const datasourceOptions = ref([])
const tableOptions = ref([])
const tableLoading = ref(false)
const columnList = ref([])

const loadDatasourceOptions = async () => {
    try {
        const res = await getDaDatasourceList()
        datasourceOptions.value = res.data
    } catch (error) {
        console.error('获取数据源失败:', error)
    }
}

const onDatasourceChange = async (id) => {
    const selected = datasourceOptions.value.find(item => item.id == id)
    if (selected) {
        form.value.datasourceType = selected.datasourceType
        form.value.datasourceName = selected.datasourceName
        form.value.datasourceConfig = selected.datasourceConfig
        let safeJson = JSON.parse(selected.datasourceConfig);
        form.value.dbname = safeJson.dbname

        console.log("🚀 ~ onDatasourceChange ~ selected:", selected)

    }
    form.value.tableName = ''
    tableOptions.value = []
    await loadTablesByDatasourceId(id)
}

const onTableChange = async (val) => {
    const selectedTable = tableOptions.value.find(item => item.tableName == val);
    if (selectedTable) {
        form.value.columnComment = selectedTable?.tableComment; // 保存中文名
        if (!form.value.name) {
            form.value.name = selectedTable?.tableComment;
        }
    } else {
        form.value.columnComment = '';
    }
};

const loadTablesByDatasourceId = async (id) => {
    tableLoading.value = true
    const res = await getTablesByDataSourceId({ datasourceId: id })
    if (res.code == '200') {
        tableOptions.value = res.data
    }
    tableLoading.value = false
}
let mode = ref()
const dialogTitle = ref('')
const openDialog = async (record, index) => {
    mode.value = index
    console.log("🚀 ~ openDialog ~ mode.value:", mode.value)
    dialogTitle.value = mode.value ? '修改稽查对象' : '新增稽查对象'
    await loadDatasourceOptions()
    resetForm()
    dialogVisible.value = true

    if (record && index) {
        const temp = JSON.parse(JSON.stringify(record))
        Object.assign(form.value, temp)
        if (temp.datasourceId) {
            await loadTablesByDatasourceId(temp.datasourceId)
        }
        if (temp.tableName) {
            await onTableChange()
        }
    }
}

const saveData = () => {
    formRef.value.validate((valid) => {
        if (valid) {
            emit('confirm', JSON.parse(JSON.stringify(form.value)), mode.value);
        }
    })
}

const closeDialog = () => {
    dialogVisible.value = false
    resetForm()
}

defineExpose({ openDialog, closeDialog })
</script>

<style scoped lang="less">
.dialog-footer {
    text-align: right;
}
</style>
