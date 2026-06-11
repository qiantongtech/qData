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
    <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
        showCancelButton :show-close="false" destroy-on-close :close-on-click-modal="false">
        <el-form ref="dpModelRefs" :model="form" label-width="110px" @submit.prevent v-loading="loading"
            :disabled="info">

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="节点名称" prop="name"
                        :rules="[{ required: true, message: '请输入节点名称', trigger: 'change' }]">
                        <el-input v-if="!info" v-model="form.name" placeholder="请输入节点名称" />
                        <div v-else class="form-readonly">{{ form.name }}</div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="类型" prop="typeName">
                        <el-input v-if="!info" v-model="form.taskParams.typeName" placeholder="请输入类型" disabled />
                        <div v-else class="form-readonly">{{ form.taskParams.typeName }}</div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="t('common.texts.description')" prop="description">
                        <el-input v-if="!info" v-model="form.description" type="textarea" :placeholder="t('common.form.descriptionPlaceholder')" />
                        <div v-else class="form-readonly">{{ form.description || '-' }}</div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="连接方式" prop="clmt">
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
                        <el-form-item label="资产表" prop="taskParams.asset_id_cpoy" :rules="[
                            { required: true, message: '请选择资产表', trigger: 'blur' }
                        ]">
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
                        <el-form-item label="源数据库连接" prop="taskParams.readerDatasource.datasourceId" :rules="[
                            {
                                required: true,
                                message: '请选择源数据库连接',
                                trigger: 'change'
                            }
                        ]">
                            <el-select v-if="!info" v-model="form.taskParams.readerDatasource.datasourceId"
                                placeholder="请选择源数据库连接" @change="handleDatasourceChange" filterable>
                                <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName"
                                    :value="dict.id"></el-option>
                            </el-select>
                            <div class="form-readonly" v-else>{{createTypeList.find((item) => item.id ==
                                form.taskParams.readerDatasource.datasourceId)?.datasourceName || '-'}}</div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="数据连接类型" prop="taskParams.readerDatasource.datasourceType">
                            <el-input v-if="!info" v-model="form.taskParams.readerDatasource.datasourceType"
                                placeholder="请输入数据连接类型" disabled />
                            <div class="form-readonly" v-else>{{ form.taskParams.readerDatasource.datasourceType || '-'
                                }}</div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="数据连接实例" prop="taskParams.readerDatasource.dbname">
                            <el-input v-if="!info" v-model="form.taskParams.readerDatasource.dbname"
                                placeholder="请输入数据连接实例" disabled />
                            <div class="form-readonly" v-else>{{ form.taskParams.readerDatasource.dbname || '-' }}</div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" v-if="form.taskParams.clmt == '0'">
                        <el-form-item label="选择表" prop="taskParams.asset_id"
                            :rules="[{ required: true, message: '请选择表', trigger: 'change' }]">
                            <el-select v-if="!info" v-model="form.taskParams.asset_id" filterable @change="handleChange"
                                :loading="loadingTables">
                                <el-option v-for="item in TablesByDataSource" :key="item.tableName"
                                    :label="item.tableName" :value="item.tableName" />
                            </el-select>
                            <div class="form-readonly" v-else>{{ form.taskParams.asset_id }}</div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" v-if="form.taskParams.clmt == '2'">
                        <el-form-item>
                            <div style="text-align: right; width: 100%">
                                <el-button size="small" type="primary" @click="sqlParseFunction"
                                    class="sql-parse-btn">SQL解析</el-button>
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20" v-if="form.taskParams.clmt == '2'">
                    <el-col :span="24">
                        <el-form-item label="SQL语句" prop="taskParams.querySql"
                            :rules="[{ required: true, message: '请输入SQL语句', trigger: 'blur' }]">
                            <sql-editor ref="editorRef" :value="form.taskParams.querySql" class="sql-editor"
                                :height="'140px'" @changeTextarea="changeTextarea($event)" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <el-row :gutter="20" v-if="form.taskParams.clmt != '2'">
                <el-col :span="12">
                    <el-form-item label="读取模式" prop="taskParams.readModeType" :rules="[
                        {
                            required: true,
                            message: '请选择读取模式',
                            trigger: 'change'
                        }
                    ]">
                        <el-radio-group v-if="!info" v-model="form.taskParams.readModeType"
                            @change="handlereadModeTypeChange">
                            <el-radio value="1">全量</el-radio>
                            <el-radio value="2">id增量</el-radio>
                            <el-radio value="3">时间范围增</el-radio>
                        </el-radio-group>
                        <div class="form-readonly" v-else>{{ form.taskParams.readModeType == 1 ? '全量' :
                            form.taskParams.readModeType == 2 ?
                                'id增量' : '时间范围增' }}</div>
                    </el-form-item>
                </el-col>
            </el-row>
            <template v-if="form.taskParams.readModeType == 2">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="id字段" prop="taskParams.idIncrementConfig.incrementColumn" :rules="[
                            {
                                required: true,
                                message: '请选择id字段',
                                trigger: 'blur'
                            }
                        ]">
                            <el-select v-if="!info" v-model="form.taskParams.idIncrementConfig.incrementColumn"
                                collapse-tags collapse-tags-tooltip filterable placeholder="请选择id字段">
                                <el-option v-for="item in ColumnByAssettab" :key="item.columnName"
                                    :label="item.columnName" :value="item.columnName" />
                            </el-select>
                            <div class="form-readonly" v-else>{{ form.taskParams.idIncrementConfig.incrementColumn ||
                                '-' }}</div>
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item label="开始值" prop="taskParams.idIncrementConfig.incrementStart" :rules="[
                            { required: true, message: '请输入开始值', trigger: 'change' },
                            { validator: checkInteger, trigger: 'change' }
                        ]">
                            <el-input v-if="!info" v-model="form.taskParams.idIncrementConfig.incrementStart"
                                placeholder="请输入开始值" type="number">
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
<!--                        <el-form-item label="逻辑连接符" prop="taskParams.dateIncrementConfig.logic" :rules="[-->
<!--                            {-->
<!--                                required: true,-->
<!--                                message: '请选择逻辑连接符',-->
<!--                                trigger: 'change'-->
<!--                            }-->
<!--                        ]">-->
<!--                            <el-select v-if="!info" v-model="form.taskParams.dateIncrementConfig.logic" filterable-->
<!--                                placeholder="下拉选择 and/or，默认 and" @change="handleChange">-->
<!--                                <el-option label="and" value="and" />-->
<!--                                <el-option label="or" value="or" />-->
<!--                            </el-select>-->
<!--                            <div class="form-readonly" v-else>{{ form.taskParams.dateIncrementConfig.logic }}</div>-->
<!--                        </el-form-item>-->
<!--                    </el-col>-->
                    <el-col :span="12">
                        <el-form-item label="时间格式" prop="taskParams.dateIncrementConfig.dateFormat">
                            <el-select v-if="!info" v-model="form.taskParams.dateIncrementConfig.dateFormat"
                                placeholder="请选择时间格式">
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
                                <i class="iconfont-mini icon-xinzeng mr5"></i>{{ t('common.button.add') }}
                            </el-button>
                        </el-col>
                    </el-row>
                </div>
                <el-table stripe height="310px" v-loading="loadingList"
                    :data="form.taskParams.dateIncrementConfig.column" style="margin-bottom: 10px;">
                    <el-table-column label="字段名称" align="left" prop="incrementColumn"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="scope">
                            {{ scope.row.incrementColumn || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="运算符" align="left" prop="operator">
                        <template #default="scope">
                            {{ scope.row.operator || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="基准类型" align="left" prop="type">
                        <template #default="scope">
                            <span v-if="scope.row.type === 1">固定值</span>
                            <span v-else-if="scope.row.type === 3">SQL表达式</span>
                            <span v-else>自动(当前时间)</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="基准值" align="left" prop="data">
                        <template #default="scope">
                            {{ scope.row.data || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="游标时间" align="left" prop="data">
                      <template #default="scope">
                        {{ scope.row.cursorTime || "-" }}
                      </template>
                    </el-table-column>
                    <el-table-column :label="t('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right"
                        width="240">
                        <template #default="scope">
                            <!-- <el-button link type="primary" icon="Edit"
                @click="openDialog({ ...scope.row, index: scope.$index + 1 })">{{ t('common.button.update') }}</el-button> -->
                            <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)">{{ t('common.button.delete') }}</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </template>

            <inputEditModel :visible="open" title="属性字段编辑" @update:visible="open = $event" @confirm="handletaskConfig"
                :data="row" :ColumnByAssettab="ColumnByAssettab"
                :dateIncrementConfig_dateFormat="form.taskParams.dateIncrementConfig.dateFormat" />
            <el-row :gutter="20" v-if="form.taskParams.clmt != '2'">
                <el-col :span="24">
                    <el-form-item label="where条件" prop="where">
                        <el-input v-if="!info" v-model="form.taskParams.where" type="textarea"
                            :placeholder="'例如 id > 10 and id < 1000，请不要以分号;结尾'" />
                        <div class="form-readonly" v-else>{{ form.taskParams.where || '-' }}</div>
                    </el-form-item>
                </el-col>
            </el-row>
            <template v-if="form.taskParams.readModeType == 1">
                <div class="h2-title">属性字段</div>
                <el-table stripe height="310px" v-loading="loadingList" :data="ColumnByAssettab">
                    <el-table-column label="序号" type="index" width="80" align="left">
                        <template #default="scope">
                            <span>{{ scope.$index + 1 }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="英文名称" align="left" prop="columnName"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="scope">
                            {{ scope.row.columnName || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="中文名称" align="left" prop="columnComment"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="scope">
                            {{ scope.row.columnComment || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="字段类型" align="left" prop="columnType">
                        <template #default="scope">
                            {{ scope.row.columnType || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="是否主键" align="left" prop="pkFlag" v-if="form?.taskParams.type == '1'">
                        <template #default="scope">
                            <el-switch v-model="scope.row.pkFlag" :active-value="'1'" :inactive-value="'0'" disabled />
                        </template>
                    </el-table-column>
                    <el-table-column label="字段长度" align="left" prop="columnLength" v-if="form?.taskParams.type == '1'">
                        <template #default="scope">
                            {{ scope.row.columnLength || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="小数经度" align="left" prop="columnScale" v-if="form?.taskParams.type == '1'">
                        <template #default="scope">
                            {{ scope.row.columnScale || '-' }}
                        </template>
                    </el-table-column>
                </el-table>
            </template>

        </el-form>
        <template #footer>
            <div style="text-align: right">
                <el-button @click="closeDialog">{{ t('common.button.close') }}</el-button>
                <el-button type="primary" @click="saveData" v-if="!info">{{ t('common.button.save') }}</el-button>
            </div>
        </template>
    </el-dialog>
</template>
<script setup>
import { useI18n } from 'vue-i18n'
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
const { proxy } = getCurrentInstance();
import useUserStore from '@/store/system/user.js';

const { t } = useI18n();
const userStore = useUserStore();
const { dpp_connection } = proxy.useDict('dpp_connection');
const props = defineProps({
    visible: { type: Boolean, default: true },
    title: { type: String, default: '表单标题' },
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

// 变量定义
let loading = ref(false);
let loadingList = ref(false);
let TablesByDataSource = ref([]);
let ColumnByAssettab = ref([]);
let dpModelRefs = ref();
let form = ref({});
const tableFields = ref([]); // 来源表格
const createTypeList = ref([]); // 数据源列表
// 修改 时间范围增
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
        incrementColumn: "", // 增量字段
        incrementStart: "", // 开始值
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
        callback(new Error('请输入开始值'))
        return
    }
    const numValue = Number(value)
    if (isNaN(numValue)) {
        callback(new Error('请输入有效的数字'))
        return
    }
    if (!Number.isInteger(numValue)) {
        callback(new Error('开始值必须为整数'))
        return
    }

    callback()
}
// 获取数据源列表
const getDatasourceList = async () => {
    try {
        loading.value = true;
        const response = await listDaDatasource({
            pageSize: 9999,
            projectCode: userStore.projectCode,
            projectId: userStore.projectId,
            datasourceType: "DM8,Oracle11,MySql,Oracle,Kingbase8,Doris,SQL_Server,SQL_Server2008,PostgreSQL",
        });
        createTypeList.value = response.data.rows;
    } finally {
        loading.value = false;
    }
};
let loadingTables = ref(false);
// 获取表列表
const getTablesByDatasourceId = async (id) => {
    TablesByDataSource.value = await fetchData(
        getTablesByDataSourceId,
        { datasourceId: id },
        loadingTables
    );
};
// 获取列数据
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
// 通用的获取数据的函数
const fetchData = async (requestFn, params, loadingState) => {
    try {
        loadingState.value = true;
        const response = await requestFn(params);
        return response.data;
    } finally {
        loadingState.value = false;
    }
};

// 处理数据源变化
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

// 处理数据源变化
const handleDatasourceChange = (value) => {
    const selectedDatasource = createTypeList.value.find((item) => item.id == value);
    if (selectedDatasource) {
        resetAndFetchTables(selectedDatasource);
    }
};

// 处理表变化
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

// 连接方式切换
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
    ElMessageBox.confirm('确定要删除这条数据吗?', t('common.message.prompt'), {
        confirmButtonText: t('common.button.confirm'),
        cancelButtonText: t('common.button.cancel'),
        type: 'warning'
    })
        .then(() => {
            // 删除操作
            const index = form.value.taskParams.dateIncrementConfig.column.indexOf(row);
            if (index !== -1) {
                form.value.taskParams.dateIncrementConfig.column.splice(index, 1);
            }
            ElMessage.success(t('common.message.deleteSuccess'));
        })
        .catch(() => {
            ElMessage.info('取消删除');
        });
};
const handleAssetTableChange = (value) => {
    // 找到对应的选中项
    const selectedItem = dppNoPageListList.value.find((item) => item.id == value);

    form.value.taskParams.asset_id = selectedItem.tableName;
    form.value.taskParams.table_name = selectedItem.tableName;

    // 调用 API 获取数据源信息
    getDaDatasource(selectedItem.datasourceId).then((response) => {
        let { datasourceType, datasourceConfig, ip, port, id } = response.data;
        let code = JSON.parse(datasourceConfig);
        // 更新 readerDatasource
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
        // 获取列数据
        ColumnByAssettab.value = [];
        getColumnByAssetIdList(id, value);
    });
};

const off = () => {
    proxy.resetForm('dpModelRefs');
    // 清空表格字段数据
    ColumnByAssettab.value = [];
    TablesByDataSource.value = [];
    tableFields.value = [];
};
// 保存数据
const saveData = async () => {
    try {
        // 异步验证表单
        const valid = await dpModelRefs.value.validate();
        if (!valid) return;
        if (
            form.value?.taskParams.type == '1' &&
            (!ColumnByAssettab.value || ColumnByAssettab.value.length == 0)
        ) {
            return proxy.$message.warning('校验未通过，请选择属性字段');
        }
        // 如果没有 code，就调用接口获取唯一的 code
        if (!form.value.code) {
            loading.value = true;
            const response = await getNodeUniqueKey({
                projectCode: userStore.projectCode || '133545087166112',
                projectId: userStore.projectId
            });
            loading.value = false;
            form.value.code = response.data;
        }
        const taskParams = form.value?.taskParams;
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
        console.error('保存数据失败:', error);
        loading.value = false;
    }
};
const closeDialog = () => {
    off();
    // 关闭对话框
    emit('update', false);
};

// 监听属性变化
function deepCopy(data) {
    if (data === undefined || data === null) {
        return {}; // 或者返回一个默认值
    }
    try {
        return JSON.parse(JSON.stringify(data));
    } catch (e) {
        return {}; // 或者返回一个默认值
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
// 监听属性变化
watchEffect(() => {
    if (props.visible) {
        // 数据源
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
