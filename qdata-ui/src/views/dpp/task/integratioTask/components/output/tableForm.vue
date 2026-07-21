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
  <!-- table output -->
  <el-dialog
    v-model="visibleDialog"
    :draggable="true"
    :title="currentNode?.data?.name"
    showCancelButton
    :show-close="false"
    class="medium-dialog"
    destroy-on-close
  >
    <el-form
      ref="dpModelRefs"
      :model="form"
      label-width="110px"
      @submit.prevent
      v-loading="loading"
      :disabled="info"
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.nodeName', 'Node Name')"
            prop="name"
            :rules="[
              {
                required: true,
                message: td(
                  'dpp.integration.nodeNameRequired',
                  'Please enter node name'
                ),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.name"
              :placeholder="
                td('dpp.integration.nodeNamePlaceholder', 'Please enter node name')
              "
            />
            <div v-else class="form-readonly">{{ form.name }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.type', 'Type')"
            prop="typeName"
           :label-position="labelPosition">
            <el-select
              v-if="!info"
              v-model="form.taskParams.typeName"
              :placeholder="td('dpp.integration.typePlaceholder', 'Please enter type')"
              filterable
              disabled
            >
              <el-option
                v-for="dict in typeList"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
            <div v-else class="form-readonly">
              {{ form.taskParams.typeName }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
            :label="td('common.texts.description')"
            prop="description"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.description"
              type="textarea"
              :placeholder="td('common.form.descriptionPlaceholder')"
            />
            <div v-else class="form-readonly">
              {{ form.description || "-" }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.targetDbConnection', 'Target Data Connection')"
            prop="taskParams.writerDatasource.datasourceId"
            :rules="[
              {
                required: true,
                message: td(
                  'dpp.integration.targetDbConnectionRequired',
                  'Please select target data connection'
                ),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-select
              v-if="!info"
              v-model="form.taskParams.writerDatasource.datasourceId"
              :placeholder="
                td(
                  'dpp.integration.targetDbConnectionPlaceholder',
                  'Please select target data connection'
                )
              "
              @change="handleDatasourceChange"
              filterable
            >
              <el-option
                v-for="dict in createTypeList"
                :key="dict.id"
                :label="dict.datasourceName"
                :value="dict.id"
              ></el-option>
            </el-select>
            <div v-else class="form-readonly">
              {{
                createTypeList.find(
                  (item) =>
                    item.id == form.taskParams.writerDatasource.datasourceId
                )?.datasourceName || "-"
              }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.dbConnectionType', 'Data Connection Type')"
            prop="taskParams.writerDatasource.datasourceType"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.taskParams.writerDatasource.datasourceType"
              :placeholder="
                td(
                  'dpp.integration.dbConnectionTypePlaceholder',
                  'Please enter data connection type'
                )
              "
              disabled
            />
            <div v-else class="form-readonly">
              {{ form.taskParams.writerDatasource.datasourceType || "-" }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.dbConnectionInstance', 'Data Connection Instance')"
            prop="taskParams.writerDatasource.dbname"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.taskParams.writerDatasource.dbname"
              :placeholder="
                td(
                  'dpp.integration.dbConnectionInstancePlaceholder',
                  'Please enter data connection instance'
                )
              "
              disabled
            />
            <div v-else class="form-readonly">
              {{ form.taskParams.writerDatasource.dbname || "-" }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.selectTable', 'Select Table')"
            prop="taskParams.target_asset_id"
            :rules="[
              {
                required: true,
                message: td('dpp.integration.selectTableRequired', 'Please select table'),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-select
              v-if="!info"
              v-model="form.taskParams.target_asset_id"
              :placeholder="
                td('dpp.integration.selectTablePlaceholder', 'Please select table')
              "
              @change="handleChange"
              filterable
              :loading="loadingTables"
            >
              <el-option
                v-for="item in TablesByDataSource"
                :key="item.tableName"
                :label="item.tableName"
                :value="item.tableName"
              />
            </el-select>
            <div v-else class="form-readonly">
              {{ form.taskParams.target_asset_id || "-" }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
            :label="td('dpp.integration.whereCondition', 'Where Condition')"
            prop="where"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.taskParams.where"
              type="textarea"
              :placeholder="
                td(
                  'dpp.integration.whereConditionPlaceholder',
                  'e.g. id > 10 and id < 1000, do not end with semicolon'
                )
              "
            />
            <div v-else class="form-readonly">
              {{ form.taskParams.where || "-" }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>

      <div class="h2-title">
        {{ td("dpp.integration.fieldMapping", "Field Mapping") }}
      </div>

      <div style="margin-top: -20px">
        <YourChildComponent
          ref="childComponent"
          :tableFields="tableFields"
          :toColumnsList="ColumnByAssettab"
          v-loading="loadingList"
          :info="info"
        />
      </div>
      <div class="h2-title">
        {{ td("dpp.integration.outputConfig", "Output Config") }}
      </div>

      <el-row :gutter="20">
        <el-col :span="24" class="hasMsg">
          <el-form-item
            :label="td('dpp.integration.preSql', 'Pre-SQL')"
            prop="preSql"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.taskParams.preSql"
              type="textarea"
              :placeholder="
                td('dpp.integration.preSqlPlaceholder', 'Please enter pre-SQL')
              "
            />
            <div v-else class="form-readonly">{{ form.taskParams.preSql || "-" }}</div>
            <span class="msg"
              ><el-icon> <InfoFilled /> </el-icon
              >{{
                td("dpp.integration.preSqlHint", "SQL executed before data write")
              }}</span
            >
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.writeMode', 'Write Mode')"
            prop="taskParams.writeModeType"
            :rules="[
              {
                required: true,
                message: td(
                  'dpp.integration.writeModeRequired',
                  'Please select write mode'
                ),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-radio-group
              v-if="!info"
              v-model="form.taskParams.writeModeType"
            >
              <el-radio :value="2">{{
                td("dpp.integration.append", "Append")
              }}</el-radio>
              <el-radio :value="1">{{
                td("dpp.integration.fullVolume", "Full Volume")
              }}</el-radio>
              <el-radio :value="3">{{
                td("dpp.integration.incrementalUpdate", "Incremental Update")
              }}</el-radio>
            </el-radio-group>
            <div v-else class="form-readonly">
              {{
                form.taskParams.writeModeType == 1
                  ? td("dpp.integration.fullVolume", "Full Volume")
                  : form.taskParams.writeModeType == 2
                  ? td("dpp.integration.append", "Append")
                  : td("dpp.integration.incrementalUpdate", "Incremental Update")
              }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12" class="hasMsg">
          <el-form-item
            :label="td('dpp.integration.singleWriteData', 'Single Write Count')"
            prop="taskParams.description"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.taskParams.description"
              :placeholder="
                td(
                  'dpp.integration.singleWriteDataPlaceholder',
                  'Please enter single write count'
                )
              "
              type="number"
            >
              <template #append>{{
                td("dpp.integration.recordsUnit", "records")
              }}</template>
            </el-input>
            <div v-else class="form-readonly">
              {{
                form.taskParams.description
                  ? form.taskParams.description +
                    td("dpp.integration.recordsUnit", "records")
                  : "-"
              }}
            </div>
            <span class="msg"
              ><el-icon> <InfoFilled /> </el-icon
              >{{
                td("dpp.integration.singleWriteDataHint", "Default 1000 records if not specified")
              }}</span
            >
          </el-form-item>
        </el-col>
      </el-row>
      <el-row
        :gutter="20"
        v-if="
          form.taskParams.writeModeType == 3 &&
          form.taskParams.writerDatasource.datasourceType !== 'Doris'
        "
      >
        <el-col :span="24">
          <el-form-item
            :label="td('dpp.integration.updatePrimaryKey', 'Update Primary Key Field')"
            prop="taskParams.selectedColumns"
            :rules="[
              {
                required: true,
                message: td(
                  'dpp.integration.updatePrimaryKeyRequired',
                  'Please select update primary key field'
                ),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-checkbox-group
              v-if="!info"
              v-model="form.taskParams.selectedColumns"
            >
              <el-checkbox
                v-for="item in ColumnByAssettab"
                :key="item.id"
                :label="item.columnName"
                :value="item.columnName"
              >
                {{ item.columnName }}
              </el-checkbox>
            </el-checkbox-group>
            <div v-else class="form-readonly">
              {{ form.taskParams.selectedColumns || "-" }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24" class="hasMsg">
          <el-form-item
            :label="td('dpp.integration.postSql', 'Post-SQL')"
            prop="taskParams.postSql"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.taskParams.postSql"
              type="textarea"
              :placeholder="
                td('dpp.integration.postSqlPlaceholder', 'Please enter post-SQL')
              "
            />
            <div v-else class="form-readonly">
              {{ form.taskParams.postSql || "-" }}
            </div>
            <span class="msg"
              ><el-icon> <InfoFilled /> </el-icon
              >{{
                td("dpp.integration.postSqlHint", "SQL executed after data sync completes")
              }}</span
            >
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">{{
          td("common.button.close")
        }}</el-button>
        <el-button type="primary" @click="saveData" v-if="!info">{{
          td("common.button.save")
        }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup name="OutputForm">
import useDefaultLang from "@/composables/useDefaultLang";
import { listDaDatasource } from "@/api/da/dataSource/dataSource.js";

import { handleType2TaskParams } from "@/views/dpp/utils/opBase.js";
import { typeList } from "@/utils/graph.js";
import {
  getTablesByDataSourceId,
  getColumnByAssetId,
  getNodeUniqueKey,
} from "@/api/dpp/task/index.js";
const { proxy } = getCurrentInstance();
import useUserStore from "@/store/system/user.js";
import YourChildComponent from "../fieldMap.vue";

const { td } = useDefaultLang();
const userStore = useUserStore();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "" },
  currentNode: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
});
const emit = defineEmits(["update", "confirm"]);
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update", newValue);
  },
});

// variable definition
let loading = ref(false);
let loadingList = ref(false);
let opens = ref(false);
let row = ref();
let TablesByDataSource = ref([]);
let ColumnByAssettab = ref([]);
let dpModelRefs = ref();
let form = ref({});
let loadingTables = ref(false);
function handleRule(data) {
  row.value = {};
  row.value = data;
  opens.value = true;
}
const submitForm = (value) => {
  tableFields.value.forEach((column) => {
    if (row.value.id == column.id) {
      console.log("column", column);
      if (value.length > 0) {
        column.cleanRuleList = value;
        column.elementId = value.map((item) => item.ruleId);
      }
    }
    opens.value = false;
  });
};

const childComponent = ref(null); // table fields
const tableFields = ref([]); // Source form
const createTypeList = ref([]); // Data source list

// Get a list of data sources
const getDatasourceList = async () => {
  try {
    loading.value = true;
    const response = await listDaDatasource({
      projectCode: userStore.projectCode,
      projectId: userStore.projectId,
      datasourceType:
        "DM8,Oracle11,MySql,Oracle,Kingbase8,Doris,SQL_Server,SQL_Server2008,PostgreSQL",
      pageSize: 9999,
    });
    createTypeList.value = response.data.rows;
  } finally {
    loading.value = false;
  }
};

// Get table list
const getTablesByDatasourceId = async (id) => {
  TablesByDataSource.value = await fetchData(
    getTablesByDataSourceId,
    { datasourceId: id },
    loadingTables
  );
};

// Get column data
const getColumnByAssetIdList = async (id) => {
  ColumnByAssettab.value = await fetchData(
    getColumnByAssetId,
    {
      id: form.value.taskParams.writerDatasource.datasourceId,
      tableName: form.value.taskParams.target_asset_id,
    },
    loadingList
  );
};

// Get column data
const getColumns = () => {
  return childComponent.value?.getColumns();
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
  form.value.taskParams.target_datasource_id = "";
  form.value.taskParams.writerDatasource = {
    datasourceType,
    datasourceConfig,
    ip,
    port,
    dbname: code.dbname,
    target_asset_id: id,
    datasourceId: id,
  };

  await getTablesByDatasourceId(id);
};

// Handle data source changes
const handleDatasourceChange = (value) => {
  const selectedDatasource = createTypeList.value.find(
    (item) => item.id == value
  );
  if (selectedDatasource) {
    resetAndFetchTables(selectedDatasource);
  }
};

// Handle table changes
const setTableName = (selectedDatasource) => {
  form.value.taskParams.target_table_name = selectedDatasource.tableName;
};

const handleChange = (value) => {
  const selectedDatasource = TablesByDataSource.value.find(
    (item) => item.tableName == value
  );
  if (selectedDatasource) {
    setTableName(selectedDatasource);
    ColumnByAssettab.value = [];
    getColumnByAssetIdList(selectedDatasource.id);
  }
};

const off = () => {
  proxy.resetForm("dpModelRefs");
  // Clear table field data
  ColumnByAssettab.value = [];
  TablesByDataSource.value = [];
  tableFields.value = [];
};
// save data
const saveData = async () => {
  try {
    const valid = await dpModelRefs.value?.validate();
    if (!valid) return;

    // Generate unique code when there is no code
    if (!form.value.code) {
      loading.value = true;
      try {
        const { data } = await getNodeUniqueKey({
          projectCode: userStore.projectCode || "133545087166112",
          projectId: userStore.projectId,
        });
        form.value.code = data;
      } finally {
        loading.value = false;
      }
    }

    const taskParams = form.value.taskParams || {};
    const { fromColumns = [], toColumns = [] } = getColumns() || {};

    taskParams.tableFields = fromColumns.length
      ? fromColumns
      : taskParams.tableFields;
    console.log("🚀 ~ saveData ~ fromColumns:", fromColumns);
    taskParams.toColumnsList = toColumns.length
      ? toColumns
      : ColumnByAssettab.value;
    const { target_columns, columns } = handleType2TaskParams(
      taskParams.tableFields,
      taskParams.toColumnsList
    );
    taskParams.target_columns = target_columns;
    taskParams.columns = columns;
    console.log(
      "🚀 ~ saveData ~ taskParams.tableFields :",
      taskParams.tableFields
    );

    taskParams.outputFields = ColumnByAssettab.value;
    console.log("🚀 ~ saveData ~ form.value:", form.value);
    form.value.taskParams = { ...form.value.taskParams, ...taskParams };
    emit("confirm", form.value);
  } catch (error) {
    console.error("Failed to save data:", error);
    loading.value = false;
  }
};

const closeDialog = () => {
  off();
  // Close dialog
  emit("update", false);
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

// Common functions that handle data source and column operations
const handleDatasource = (datasource, assetId) => {
  if (datasource?.datasourceId) {
    getTablesByDatasourceId(datasource.datasourceId);
    // If you need to process assetId, you can call it here
    // getColumnByAssetIdList(assetId);
  } else {
    console.warn("Invalid data source information", datasource);
  }
};
// Listen for property changes
watchEffect(() => {
  if (!props.visible) {
    off();
    return;
  }
  getDatasourceList();

  form.value = deepCopy(props.currentNode?.data || {});
  console.log("🚀 ~ watchEffect ~ form.value :", form.value);

  const taskParams = form.value?.taskParams || {};
  tableFields.value = taskParams.tableFields?.length
    ? deepCopy(taskParams.tableFields)
    : deepCopy(taskParams.inputFields);
  ColumnByAssettab.value = taskParams.toColumnsList || [];
});
handleDatasource(form.value?.taskParams.writerDatasource || "");
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
