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
    <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
        showCancelButton :show-close="false" destroy-on-close :close-on-click-modal="false">
        <el-form ref="dpModelRefs" :model="form" label-width="110px" @submit.prevent v-loading="loading"
            :disabled="info" :label-position="labelPosition">

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.nodeName', 'Node Name')" prop="name"
                        :rules="[{ required: true, message: td('dpp.integration.nodeNameRequired', 'Please enter node name'), trigger: 'change' }]" :label-position="labelPosition">
                        <el-input v-if="!info" v-model="form.name" :placeholder="td('dpp.integration.nodeNamePlaceholder', 'Please enter node name')" />
                        <div v-else class="form-readonly">{{ form.name }}</div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.type', 'Type')" prop="typeName" :label-position="labelPosition">
                        <el-input v-if="!info" v-model="form.taskParams.typeName" :placeholder="td('dpp.integration.typePlaceholder', 'Please enter type')" disabled />
                        <div v-else class="form-readonly">{{ form.taskParams.typeName }}</div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
                        <el-input v-if="!info" v-model="form.description" type="textarea" :placeholder="td('common.form.descriptionPlaceholder')" />
                        <div v-else class="form-readonly">{{ form.description || '-' }}</div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.connectionMethod', 'Connection Method')" prop="clmt" :label-position="labelPosition">
                        <el-radio-group v-if="!info" @change="handleReleaseStateChange" v-model="form.taskParams.clmt">
                            <template v-for="dict in dpp_connection" :key="dict.value">
                                <el-radio :value="dict.value">
                                    {{ dict.label }}
                                </el-radio>
                            </template>
                        </el-radio-group>
                        <div class="form-readonly" v-else>{{dpp_connection.find((item) => item.value ==
                            form.taskParams.clmt)?.label || '-'}}</div>
                    </el-form-item>
                </el-col>
                <template v-if="form.taskParams.clmt == '1'">
                    <el-col :span="12">
                        <el-form-item :label="td('dpp.integration.assetTable', 'Asset Table')" prop="taskParams.asset_id_cpoy" :rules="[
                            { required: true, message: td('dpp.integration.assetTableRequired', 'Please select asset table'), trigger: 'blur' }
                        ]" :label-position="labelPosition">
                            <el-select v-if="!info" v-model="form.taskParams.asset_id_cpoy" filterable
                                @change="handleAssetTableChange" :loading="dppLoading">
                                <el-option v-for="item in dppNoPageListList" :key="item.id" :label="item.name"
                                    :value="item.id" />
                            </el-select>
                            <div class="form-readonly" v-else>{{dppNoPageListList.find((item) => item.id ==
                                form.taskParams.asset_id_cpoy)?.name || '-'}}</div>
                        </el-form-item>
                    </el-col>
                </template>
            </el-row>
            <!-- -->
            <template v-if="form.taskParams.clmt == '0' || form.taskParams.clmt == '2'">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('dpp.integration.sourceDbConnection', 'Source DB Connection')" prop="taskParams.readerDatasource.datasourceId" :rules="[
                            {
                                required: true,
                                message: td('dpp.integration.sourceDbConnectionRequired', 'Please select source DB connection'),
                                trigger: 'change'
                            }
                        ]" :label-position="labelPosition">
                            <el-select v-if="!info" v-model="form.taskParams.readerDatasource.datasourceId"
                                :placeholder="td('dpp.integration.sourceDbConnectionPlaceholder', 'Please select source DB connection')" @change="handleDatasourceChange" filterable>
                                <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName"
                                    :value="dict.id"></el-option>
                            </el-select>
                            <div class="form-readonly" v-else>{{createTypeList.find((item) => item.id ==
                                form.taskParams.readerDatasource.datasourceId)?.datasourceName || '-'}}</div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('dpp.integration.dbConnectionType', 'Data Connection Type')" prop="taskParams.readerDatasource.datasourceType" :label-position="labelPosition">
                            <el-input v-if="!info" v-model="form.taskParams.readerDatasource.datasourceType"
                                :placeholder="td('dpp.integration.dbConnectionTypePlaceholder', 'Please enter data connection type')" disabled />
                            <div class="form-readonly" v-else>{{ form.taskParams.readerDatasource.datasourceType || '-'
                                }}</div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('dpp.integration.dbConnectionInstance', 'Data Connection Instance')" prop="taskParams.readerDatasource.dbname" :label-position="labelPosition">
                            <el-input v-if="!info" v-model="form.taskParams.readerDatasource.dbname"
                                :placeholder="td('dpp.integration.dbConnectionInstancePlaceholder', 'Please enter data connection instance')" disabled />
                            <div class="form-readonly" v-else>{{ form.taskParams.readerDatasource.dbname || '-' }}</div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" v-if="form.taskParams.clmt == '0'">
                        <el-form-item :label="td('dpp.integration.selectTable', 'Select Table')" prop="taskParams.asset_id"
                            :rules="[{ required: true, message: td('dpp.integration.selectTableRequired', 'Please select table'), trigger: 'change' }]" :label-position="labelPosition">
                            <el-select v-if="!info" v-model="form.taskParams.asset_id" filterable @change="handleChange"
                                :loading="loadingTables">
                                <el-option v-for="item in TablesByDataSource" :key="item.tableName"
                                    :label="item.tableName" :value="item.tableName" />
                            </el-select>
                            <div class="form-readonly" v-else>{{ form.taskParams.asset_id }}</div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" v-if="form.taskParams.clmt == '2'">
                        <el-form-item :label-position="labelPosition">
                            <div style="text-align: right; width: 100%">
                                <el-button size="small" type="primary" @click="sqlParseFunction"
                                    class="sql-parse-btn">{{ td('dpp.integration.sqlParse', 'SQL Parse') }}</el-button>
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20" v-if="form.taskParams.clmt == '2'">
                    <el-col :span="24">
                        <el-form-item :label="td('dpp.integration.sqlStatement', 'SQL Statement')" prop="taskParams.querySql"
                            :rules="[{ required: true, message: td('dpp.integration.sqlStatementRequired', 'Please enter SQL statement'), trigger: 'blur' }]" :label-position="labelPosition">
                            <sql-editor ref="editorRef" :value="form.taskParams.querySql" class="sql-editor"
                                :height="'140px'" @changeTextarea="changeTextarea($event)" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <el-row :gutter="20" v-if="form.taskParams.clmt != '2'">
                <el-col :span="12">
                    <el-form-item :label="td('dpp.integration.readMode', 'Read Mode')" prop="taskParams.readModeType" :rules="[
                        {
                            required: true,
                            message: td('dpp.integration.readModeRequired', 'Please select read mode'),
                            trigger: 'change'
                        }
                    ]" :label-position="labelPosition">
                        <el-radio-group v-if="!info" v-model="form.taskParams.readModeType"
                            @change="handlereadModeTypeChange">
                            <el-radio value="1">{{ td('dpp.integration.fullVolume', 'Full Volume') }}</el-radio>
                            <el-radio value="2">{{ td('dpp.integration.idIncrement', 'ID Increment') }}</el-radio>
                            <el-radio value="3">{{ td('dpp.integration.timeRangeIncrement', 'Time Range Increment') }}</el-radio>
                        </el-radio-group>
                        <div class="form-readonly" v-else>{{ form.taskParams.readModeType == 1 ? td('dpp.integration.fullVolume', 'Full Volume') :
                            form.taskParams.readModeType == 2 ?
                                td('dpp.integration.idIncrement', 'ID Increment') : td('dpp.integration.timeRangeIncrement', 'Time Range Increment') }}</div>
                    </el-form-item>
                </el-col>
            </el-row>
            <template v-if="form.taskParams.readModeType == 2">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('dpp.integration.idField', 'ID Field')" prop="taskParams.idIncrementConfig.incrementColumn" :rules="[
                            {
                                required: true,
                                message: td('dpp.integration.idFieldRequired', 'Please select ID field'),
                                trigger: 'blur'
                            }
                        ]" :label-position="labelPosition">
                            <el-select v-if="!info" v-model="form.taskParams.idIncrementConfig.incrementColumn"
                                collapse-tags collapse-tags-tooltip filterable :placeholder="td('dpp.integration.idFieldPlaceholder', 'Please select ID field')">
                                <el-option v-for="item in ColumnByAssettab" :key="item.columnName"
                                    :label="item.columnName" :value="item.columnName" />
                            </el-select>
                            <div class="form-readonly" v-else>{{ form.taskParams.idIncrementConfig.incrementColumn ||
                                '-' }}</div>
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item :label="td('dpp.integration.startValue', 'Start Value')" prop="taskParams.idIncrementConfig.incrementStart" :rules="[
                            { required: true, message: td('dpp.integration.startValueRequired', 'Please enter start value'), trigger: 'change' },
                            { validator: checkInteger, trigger: 'change' }
                        ]" :label-position="labelPosition">
                            <el-input v-if="!info" v-model="form.taskParams.idIncrementConfig.incrementStart"
                                :placeholder="td('dpp.integration.startValuePlaceholder', 'Please enter start value')" type="number">
                            </el-input>
                            <div class="form-readonly" v-else>{{ form.taskParams.idIncrementConfig.incrementStart || '-'
                                }}</div>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-if="form.taskParams.readModeType == 3">
                <el-row :gutter="20">
<!--                    <el-col :span="12">-->
<!--                        <el-form-item label="Logical Connector" prop="taskParams.dateIncrementConfig.logic" :rules="[-- :label-position="labelPosition">
<!--                            {-->
<!--                                required: true,-->
<!--                                message: 'Please select a logical connector',-->
<!--                                trigger: 'change'-->
<!--                            }-->
<!--                        ]">-->
<!--                            <el-select v-if="!info" v-model="form.taskParams.dateIncrementConfig.logic" filterable-->
<!--                                placeholder="drop-down selection and/or, default and" @change="handleChange">-->
<!--                                <el-option label="and" value="and" />-->
<!--                                <el-option label="or" value="or" />-->
<!--                            </el-select>-->
<!--                            <div class="form-readonly" v-else>{{ form.taskParams.dateIncrementConfig.logic }}</div>-->
<!--                        </el-form-item>-->
<!--                    </el-col>-->
                    <el-col :span="12">
                        <el-form-item :label="td('dpp.integration.timeFormat', 'Time Format')" prop="taskParams.dateIncrementConfig.dateFormat" :label-position="labelPosition">
                            <el-select v-if="!info" v-model="form.taskParams.dateIncrementConfig.dateFormat"
                                :placeholder="td('dpp.integration.timeFormatPlaceholder', 'Please select time format')">
                                <el-option v-for="item in dateFormatOptions" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
                            <div class="form-readonly" v-else>{{dateFormatOptions.find(item => item.value ==
                                form.taskParams.dateIncrementConfig.dateFormat)?.label || '-'}}</div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <div class="justify-between mb15">
                    <el-row :gutter="15" class="btn-style">
                        <el-col :span="1.5">
                            <el-button type="primary" plain @click="openDialog()">
                                <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
                            </el-button>
                        </el-col>
                    </el-row>
                </div>
                <el-table stripe height="310px" v-loading="loadingList"
                    :data="form.taskParams.dateIncrementConfig.column" style="margin-bottom: 10px;">
                    <el-table-column :label="td('dpp.integration.fieldName', 'Field Name')" align="left" prop="incrementColumn"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="scope">
                            {{ scope.row.incrementColumn || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column :label="td('dpp.integration.operator', 'Operator')" align="left" prop="operator">
                        <template #default="scope">
                            {{ scope.row.operator || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column :label="td('dpp.integration.benchmarkType', 'Benchmark Type')" align="left" prop="type">
                        <template #default="scope">
                            <span v-if="scope.row.type === 1">{{ td('dpp.integration.fixedValue', 'Fixed Value') }}</span>
                            <span v-else-if="scope.row.type === 3">{{ td('dpp.integration.sqlExpression', 'SQL Expression') }}</span>
                            <span v-else>{{ td('dpp.integration.autoCurrentTime', 'Auto-get current time, no need to fill') }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column :label="td('dpp.integration.benchmarkValue', 'Benchmark Value')" align="left" prop="data">
                        <template #default="scope">
                            {{ scope.row.data || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column :label="td('dpp.integration.cursorTime', 'Cursor Time')" align="left" prop="data">
                      <template #default="scope">
                        {{ scope.row.cursorTime || "-" }}
                      </template>
                    </el-table-column>
                    <el-table-column :label="td('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right"
                        width="240">
                        <template #default="scope">
                            <!-- <el-button link type="primary" icon="Edit"
                @click="openDialog({ ...scope.row, index: scope.$index + 1 })">{{ td('common.button.update') }}</el-button> -->
                            <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)">{{ td('common.button.delete') }}</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </template>

            <inputEditModel :visible="open" :title="td('dpp.integration.attributeFieldEdit', 'Attribute Field Edit')" @update:visible="open = $event" @confirm="handletaskConfig"
                :data="row" :ColumnByAssettab="ColumnByAssettab"
                :dateIncrementConfig_dateFormat="form.taskParams.dateIncrementConfig.dateFormat" />
            <el-row :gutter="20" v-if="form.taskParams.clmt != '2'">
                <el-col :span="24">
                    <el-form-item :label="td('dpp.integration.whereCondition', 'Where Condition')" prop="where" :label-position="labelPosition">
                        <el-input v-if="!info" v-model="form.taskParams.where" type="textarea"
                            :placeholder="td('dpp.integration.whereConditionPlaceholder', 'e.g. id > 10 and id < 1000, do not end with semicolon')" />
                        <div class="form-readonly" v-else>{{ form.taskParams.where || '-' }}</div>
                    </el-form-item>
                </el-col>
            </el-row>
            <template v-if="form.taskParams.readModeType == 1">
                <div class="h2-title">{{ td('dpp.integration.attributeFields', 'Attribute Fields') }}</div>
                <el-table stripe height="310px" v-loading="loadingList" :data="ColumnByAssettab">
                    <el-table-column :label="td('common.display.index', 'Index')" type="index" width="80" align="left">
                        <template #default="scope">
                            <span>{{ scope.$index + 1 }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column :label="td('dpp.column.englishName', 'English Name')" align="left" prop="columnName"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="scope">
                            {{ scope.row.columnName || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column :label="td('dpp.integration.chineseName', 'Chinese Name')" align="left" prop="columnComment"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="scope">
                            {{ scope.row.columnComment || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column :label="td('dpp.integration.fieldType', 'Field Type')" align="left" prop="columnType">
                        <template #default="scope">
                            {{ scope.row.columnType || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column :label="td('dpp.integration.isPrimaryKey', 'Primary Key')" align="left" prop="pkFlag" v-if="form?.taskParams.type == '1'">
                        <template #default="scope">
                            <el-switch v-model="scope.row.pkFlag" :active-value="'1'" :inactive-value="'0'" disabled />
                        </template>
                    </el-table-column>
                    <el-table-column :label="td('dpp.column.fieldLength', 'Field Length')" align="left" prop="columnLength" v-if="form?.taskParams.type == '1'">
                        <template #default="scope">
                            {{ scope.row.columnLength || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column :label="td('dpp.integration.decimalPrecision', 'Decimal Precision')" align="left" prop="columnScale" v-if="form?.taskParams.type == '1'">
                        <template #default="scope">
                            {{ scope.row.columnScale || '-' }}
                        </template>
                    </el-table-column>
                </el-table>
            </template>

        </el-form>
        <template #footer>
            <div style="text-align: right">
                <el-button @click="closeDialog">{{ td('common.button.close') }}</el-button>
                <el-button type="primary" @click="saveData" v-if="!info">{{ td('common.button.save') }}</el-button>
            </div>
        </template>
    </el-dialog>
</template>
<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import inputEditModel from './inputColumnEdit.vue';
import SqlEditor from '@/components/SqlEditor/index1.vue';
import {
    getTablesByDataSourceId,
    getColumnByAssetId,
    getNodeUniqueKey
} from '@/api/dpp/task/index.js';
import {
    listDaDatasource,
    getDaDatasource,
    sqlParse
} from '@/api/da/dataSource/dataSource.js';
import { listDppAsset } from '@/api/da/asset/asset.js';
import { isDateColumnType, isNumericColumnType, validateWhereCondition } from '../../utils/foolproof.js';
const { proxy } = getCurrentInstance();
import useUserStore from '@/store/system/user.js';

const { td } = useDefaultLang();
const userStore = useUserStore();
const { dpp_connection } = proxy.useDict('dpp_connection');
const props = defineProps({
    visible: { type: Boolean, default: true },
    title: { type: String, default: '' },
    currentNode: { type: Object, default: () => ({}) },
    info: { type: Boolean, default: false },
});

const emit = defineEmits(['update', 'confirm']);
const visibleDialog = computed({
    get() {
        return props.visible;
    },
    set(newValue) {
        emit('update', newValue);
    }
});

// variable definition
let loading = ref(false);
let loadingList = ref(false);
let TablesByDataSource = ref([]);
let ColumnByAssettab = ref([]);
let dpModelRefs = ref();
let form = ref({});
const tableFields = ref([]); // Source form
const createTypeList = ref([]); // Data source list
// Modify the time range to increase
const open = ref(false);
let row = ref({});
const openDialog = (obj) => {
    row.value = obj;
    open.value = true;
};
const dateFormatOptions = [
    { label: 'yyyy-MM-dd', value: 'yyyy-MM-dd' },
    { label: 'yyyy-MM-dd HH:mm:ss', value: 'yyyy-MM-dd HH:mm:ss' },
]

const handlereadModeTypeChange = (val) => {
    form.value.taskParams.idIncrementConfig = {
        incrementColumn: "", // Increment field
        incrementStart: "", // start value
    };
    form.value.taskParams.dateIncrementConfig = {
        logic: "and",
        dateFormat: "yyyy-MM-dd",
        column: [

        ],
    }
}

const checkInteger = (rule, value, callback) => {
    if (value === '' || value === null || value === undefined) {
        callback(new Error(td('dpp.integration.startValueRequired', 'Please enter start value')))
        return
    }
    const numValue = Number(value)
    if (isNaN(numValue)) {
        callback(new Error(td('dpp.integration.invalidNumber', 'Please enter a valid number')))
        return
    }
    if (!Number.isInteger(numValue)) {
        callback(new Error(td('dpp.integration.startValueMustBeInteger', 'Start value must be an integer')))
        return
    }

    callback()
}
// Get a list of data sources
const getDatasourceList = async () => {
    try {
        loading.value = true;
        const response = await listDaDatasource({
            pageSize: 9999,
            projectCode: userStore.projectCode,
            projectId: userStore.projectId,
            validFlag: true,
            datasourceType: "DM8,Oracle11,MySql,Oracle,Kingbase8,Doris,SQL_Server,SQL_Server2008,PostgreSQL",
        });
        createTypeList.value = response.data.rows;
    } finally {
        loading.value = false;
    }
};
let loadingTables = ref(false);
// Get table list
const getTablesByDatasourceId = async (id) => {
    TablesByDataSource.value = await fetchData(
        getTablesByDataSourceId,
        { datasourceId: id },
        loadingTables
    );
};
// Get column data
const getColumnByAssetIdList = async (id, data) => {
    ColumnByAssettab.value = await fetchData(
        getColumnByAssetId,
        {
            withRule: 2,
            id: form.value.taskParams.readerDatasource.datasourceId,
            tableName: form.value.taskParams.asset_id
        },
        loadingList
    );
    form.value.taskParams.dateIncrementConfig.column = [];
    form.value.taskParams.idIncrementConfig.incrementColumn = null;
    form.value.taskParams.inputFields = ColumnByAssettab.value;
};
// General functions for obtaining data
const fetchData = async (requestFn, params, loadingState) => {
    try {
        loadingState.value = true;
        const response = await requestFn(params);
        return response.data;
    } finally {
        loadingState.value = false;
    }
};

// Handle data source changes
const resetAndFetchTables = async (selectedDatasource) => {
    TablesByDataSource.value = [];
    ColumnByAssettab.value = [];
    let { datasourceType, datasourceConfig, ip, port, id } = selectedDatasource;
    let code = JSON.parse(datasourceConfig);
    form.value.taskParams.readerDatasource = {
        datasourceType,
        datasourceConfig,
        ip,
        port,
        dbname: code.dbname,
        datasource_id: id,
        datasourceId: id
    };
    form.value.taskParams.dateIncrementConfig.column = [];
    form.value.taskParams.idIncrementConfig.incrementColumn = null;

    await getTablesByDatasourceId(id);
};

// Handle data source changes
const handleDatasourceChange = (value) => {
    const selectedDatasource = createTypeList.value.find((item) => item.id == value);
    if (selectedDatasource) {
        resetAndFetchTables(selectedDatasource);
    }
};

// Handle table changes
const setTableName = (selectedDatasource) => {
    form.value.taskParams.table_name = selectedDatasource.tableName;
};
const handletaskConfig = (obj) => {
    if (row.value?.index) {
        form.value.taskParams.dateIncrementConfig.column[row.value.index - 1] = {
            ...form.value.taskParams.dateIncrementConfig.column[row.value.index - 1],
            ...obj
        };
    } else {
        form.value.taskParams.dateIncrementConfig.column.push({ ...obj });
    }
};
const handleChange = (value) => {
    const selectedDatasource = TablesByDataSource.value.find((item) => item.tableName == value);
    if (selectedDatasource) {
        setTableName(selectedDatasource);
        ColumnByAssettab.value = [];
        getColumnByAssetIdList(selectedDatasource.id, selectedDatasource);
    }
};
let dppNoPageListList = ref([]);
const dppLoading = ref(false);
const getdppNoPageListList = async (id) => {
    dppLoading.value = true;
    listDppAsset({
        pageNum: 1,
        pageSize: 9999,
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
        params: {
            sourceType: [0, 1],
        },
        orderByColumn: "create_time",
        isAsc: "desc",
    }).then((response) => {
        dppNoPageListList.value = response.data.rows;
        loading.value = false;
    }).finally(() => {
        dppLoading.value = false
    });
};

// Connection mode switch
const handleReleaseStateChange = (value) => {
    if (value == 1) {
        getdppNoPageListList();
        form.value.taskParams.asset_id_cpoy = '';
    } else {
        getDatasourceList();
    }
    form.value.taskParams.asset_id = '';
    form.value.taskParams.querySql = ''
    ColumnByAssettab.value = [];
    TablesByDataSource.value = [];
    form.value.taskParams.readerDatasource = {};
    form.value.taskParams.dateIncrementConfig.column = [];
    form.value.taskParams.idIncrementConfig.incrementColumn = null;
    form.value.taskParams.readModeType = 1;
};
const handleDelete = (row) => {
    ElMessageBox.confirm(td('dpp.integration.deleteConfirm', 'Are you sure to delete this data?'), td('common.message.prompt'), {
        confirmButtonText: td('common.button.confirm'),
        cancelButtonText: td('common.button.cancel'),
        type: 'warning'
    })
        .then(() => {
            // Delete operation
            const index = form.value.taskParams.dateIncrementConfig.column.indexOf(row);
            if (index !== -1) {
                form.value.taskParams.dateIncrementConfig.column.splice(index, 1);
            }
            ElMessage.success(td('common.message.deleteSuccess'));
        })
        .catch(() => {
            ElMessage.info(td('dpp.integration.cancelDelete', 'Cancel Delete'));
        });
};
const handleAssetTableChange = (value) => {
    // Find the corresponding selected item
    const selectedItem = dppNoPageListList.value.find((item) => item.id == value);

    form.value.taskParams.asset_id = selectedItem.tableName;
    form.value.taskParams.table_name = selectedItem.tableName;

    // Call API to obtain data source information
    getDaDatasource(selectedItem.datasourceId).then((response) => {
        let { datasourceType, datasourceConfig, ip, port, id } = response.data;
        let code = JSON.parse(datasourceConfig);
        // Update readerDatasource
        form.value.taskParams.readerDatasource = {
            datasourceType,
            datasourceConfig,
            ip,
            port,
            dbname: code.dbname,
            datasource_id: id,
            datasourceId: id
        };
        // setTableName(response.data);
        // Get column data
        ColumnByAssettab.value = [];
        getColumnByAssetIdList(id, value);
    });
};

const off = () => {
    proxy.resetForm('dpModelRefs');
    // Clear table field data
    ColumnByAssettab.value = [];
    TablesByDataSource.value = [];
    tableFields.value = [];
};
// save data
const saveData = async () => {
    try {
        // Asynchronous validation form
        const valid = await dpModelRefs.value.validate();
        if (!valid) return;
        if (
            form.value?.taskParams.type == '1' &&
            (!ColumnByAssettab.value || ColumnByAssettab.value.length == 0)
        ) {
            return proxy.$message.warning(td('dpp.integration.validateFailedSelectFields', 'Validation failed, please select attribute fields'));
        }
        const taskParams = form.value?.taskParams;
        const whereResult = validateWhereCondition(taskParams.where);
        if (!whereResult.valid) {
            return proxy.$message.warning(whereResult.message);
        }
        if (taskParams.readModeType == 2) {
            const incrementField = ColumnByAssettab.value.find(
                (field) => field.columnName === taskParams.idIncrementConfig?.incrementColumn
            );
            if (!incrementField || !isNumericColumnType(incrementField.columnType)) {
                return proxy.$message.warning('请选择增量字段，且字段类型需与读取模式匹配。');
            }
        }
        if (taskParams.readModeType == 3) {
            const dateColumns = taskParams.dateIncrementConfig?.column || [];
            if (dateColumns.length === 0 || dateColumns.some((item) => {
                const field = ColumnByAssettab.value.find((column) => column.columnName === item.incrementColumn);
                return !field || !isDateColumnType(field.columnType);
            })) {
                return proxy.$message.warning('请选择增量字段，且字段类型需与读取模式匹配。');
            }
        }
        // If there is no code, call the interface to get the unique code
        if (!form.value.code) {
            loading.value = true;
            const response = await getNodeUniqueKey({
                projectCode: userStore.projectCode || '133545087166112',
                projectId: userStore.projectId
            });
            loading.value = false;
            form.value.code = response.data;
        }
        taskParams.tableFields = ColumnByAssettab.value;
        taskParams.columnsList = ColumnByAssettab.value.map(({ columnName, columnType }) => ({
            colName: columnName,
            dataType: columnType,
        }));
        taskParams.columns = taskParams.tableFields.map(({ columnName }) => columnName);
        taskParams.inputFields = form.value.taskParams.inputFields;
        console.log("🚀 ~ saveData ~ askParams.inputFields:", form.value)

        emit("confirm", form.value);

    } catch (error) {
        console.error("Failed to save data:", error);
        loading.value = false;
    }
};
const closeDialog = () => {
    off();
    // Close dialog
    emit('update', false);
};

// Listen for property changes
function deepCopy(data) {
    if (data === undefined || data === null) {
        return {}; // Or return a default value
    }
    try {
        return JSON.parse(JSON.stringify(data));
    } catch (e) {
        return {}; // Or return a default value
    }
}
function sqlParseFunction() {
    ColumnByAssettab.value = [];
    loadingList.value = true;
    sqlParse({
        sourceId: form.value.taskParams.readerDatasource.datasourceId,
        sql: form.value.taskParams.querySql
    }).then((res) => {
        ColumnByAssettab.value = res.data;
        form.value.taskParams.inputFields = res.data;
        loadingList.value = false;
    });
}
function changeTextarea(val) {
    form.value.taskParams.querySql = val;
}
// Listen for property changes
watchEffect(() => {
    if (props.visible) {
        // data source
        if (props.currentNode.data.taskParams.clmt == 1) {
            getdppNoPageListList();
        } else {
            getDatasourceList();
        }
        form.value = deepCopy(props.currentNode.data);
        const taskParams = form.value?.taskParams;
        ColumnByAssettab.value = deepCopy(props.currentNode.data.taskParams.tableFields) || [];

    } else {
        off();
    }
});
if (props.currentNode?.data?.taskParams?.readerDatasource?.datasourceId) {
    getTablesByDatasourceId(props.currentNode.data.taskParams.readerDatasource.datasourceId);
}
</script>
<style scoped lang="less">
.blue-text {
    color: #2666fb;
}
</style>
