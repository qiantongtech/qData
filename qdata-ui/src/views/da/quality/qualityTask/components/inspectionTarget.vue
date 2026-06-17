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
        <el-form ref="formRef" :model="form" :rules="formRules" label-width="120px" @submit.prevent :label-position="labelPosition">
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="td('da.qualityTask.targetComponent.targetName')" prop="name" :label-position="labelPosition">
                        <el-input v-model="form.name" :placeholder="td('da.qualityTask.targetComponent.targetNamePlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="td('da.qualityTask.targetComponent.sourceDbConnection')" prop="datasourceId" :label-position="labelPosition">
                        <el-select v-model="form.datasourceId" :placeholder="td('da.qualityTask.targetComponent.sourceDbPlaceholder')" filterable
                            @change="onDatasourceChange">
                            <el-option v-for="ds in datasourceOptions" :key="ds.id" :label="ds.datasourceName"
                                :value="ds.id" />
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="td('da.qualityTask.targetComponent.datasourceType')" :label-position="labelPosition">
                        <el-input v-model="form.datasourceType" disabled :placeholder="td('da.qualityTask.targetComponent.datasourceTypePlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="td('da.qualityTask.targetComponent.datasourceInstance')" :label-position="labelPosition">
                        <el-input v-model="form.dbname" disabled :placeholder="td('da.qualityTask.targetComponent.datasourceInstancePlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">

                    <el-form-item :label="td('da.qualityTask.targetComponent.selectTable')" prop="tableName" :label-position="labelPosition">
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
                <el-button @click="closeDialog">{{ td('common.button.cancel') }}</el-button>
                <el-button type="primary" @click="saveData">{{ td('common.button.confirm') }}</el-button>
            </div>
        </template>
    </el-dialog>

    <el-dialog :title="td('da.qualityTask.targetComponent.cronTitle')" v-model="cronDialogVisible" :append-to="$refs['app-container']" destroy-on-close>
        <crontab ref="crontabRef" :expression="expression" @hide="cronDialogVisible = false" @fill="crontabFill" />
    </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, defineProps, defineEmits, getCurrentInstance } from 'vue'
import Crontab from '@/components/Crontab/index.vue'
import { getTablesByDataSourceId, getColumnByAssetId } from '@/api/dpp/task/index.js'
import { getDaDatasourceList } from '@/api/dp/model/model'

const { td } = useDefaultLang();
const emit = defineEmits(['confirm'])
const { proxy } = getCurrentInstance()

const props = defineProps({
    title: { type: String, default: '' },
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
    name: [{ required: true, message: td('da.qualityTask.targetComponent.targetNameRequired'), trigger: 'change' }],
    datasourceId: [{ required: true, message: td('da.qualityTask.targetComponent.sourceDbRequired'), trigger: 'change' }],
    tableName: [{ required: true, message: td('da.qualityTask.targetComponent.tableRequired'), trigger: 'change' }]
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
    dialogTitle.value = mode.value ? td('da.qualityTask.targetComponent.editTitle') : td('da.qualityTask.targetComponent.addTitle')
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
