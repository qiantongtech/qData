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
  <!-- 表输出 -->
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
            :label="td('dpp.integration.nodeName', '节点名称')"
            prop="name"
            :rules="[
              {
                required: true,
                message: td(
                  'dpp.integration.nodeNameRequired',
                  '请输入节点名称'
                ),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.name"
              :placeholder="
                td('dpp.integration.nodeNamePlaceholder', '请输入节点名称')
              "
            />
            <div v-else class="form-readonly">{{ form.name }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.type', '类型')"
            prop="typeName"
           :label-position="labelPosition">
            <el-select
              v-if="!info"
              v-model="form.taskParams.typeName"
              :placeholder="td('dpp.integration.typePlaceholder', '请输入类型')"
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
            :label="td('dpp.integration.targetDbConnection', '目标数据连接')"
            prop="taskParams.writerDatasource.datasourceId"
            :rules="[
              {
                required: true,
                message: td(
                  'dpp.integration.targetDbConnectionRequired',
                  '请选择目标数据连接'
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
                  '请选择目标数据连接'
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
            :label="td('dpp.integration.dbConnectionType', '数据连接类型')"
            prop="taskParams.writerDatasource.datasourceType"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.taskParams.writerDatasource.datasourceType"
              :placeholder="
                td(
                  'dpp.integration.dbConnectionTypePlaceholder',
                  '请输入数据连接类型'
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
            :label="td('dpp.integration.dbConnectionInstance', '数据连接实例')"
            prop="taskParams.writerDatasource.dbname"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.taskParams.writerDatasource.dbname"
              :placeholder="
                td(
                  'dpp.integration.dbConnectionInstancePlaceholder',
                  '请输入数据连接实例'
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
            :label="td('dpp.integration.selectTable', '选择表')"
            prop="taskParams.target_asset_id"
            :rules="[
              {
                required: true,
                message: td('dpp.integration.selectTableRequired', '请选择表'),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-select
              v-if="!info"
              v-model="form.taskParams.target_asset_id"
              :placeholder="
                td('dpp.integration.selectTablePlaceholder', '请选择表')
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
            :label="td('dpp.integration.whereCondition', 'where条件')"
            prop="where"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.taskParams.where"
              type="textarea"
              :placeholder="
                td(
                  'dpp.integration.whereConditionPlaceholder',
                  '请输入where条件'
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
        {{ td("dpp.integration.fieldMapping", "字段映射") }}
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
        {{ td("dpp.integration.outputConfig", "输出配置") }}
      </div>

      <el-row :gutter="20">
        <el-col :span="24" class="hasMsg">
          <el-form-item
            :label="td('dpp.integration.preSql', '前置SQL')"
            prop="preSql"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.taskParams.preSql"
              type="textarea"
              :placeholder="
                td('dpp.integration.preSqlPlaceholder', '请输入前置SQL')
              "
            />
            <div v-else class="form-readonly">{{ form.taskParams.preSql || "-" }}</div>
            <span class="msg"
              ><el-icon> <InfoFilled /> </el-icon
              >{{
                td("dpp.integration.preSqlHint", "数据写入之前执行的SQL")
              }}</span
            >
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integration.writeMode', '写入模式')"
            prop="taskParams.writeModeType"
            :rules="[
              {
                required: true,
                message: td(
                  'dpp.integration.writeModeRequired',
                  '请选择写入模式'
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
                td("dpp.integration.append", "追加")
              }}</el-radio>
              <el-radio :value="1">{{
                td("dpp.integration.fullVolume", "全量")
              }}</el-radio>
              <el-radio :value="3">{{
                td("dpp.integration.incrementalUpdate", "增量更新")
              }}</el-radio>
            </el-radio-group>
            <div v-else class="form-readonly">
              {{
                form.taskParams.writeModeType == 1
                  ? td("dpp.integration.fullVolume", "全量")
                  : form.taskParams.writeModeType == 2
                  ? td("dpp.integration.append", "追加")
                  : td("dpp.integration.incrementalUpdate", "增量更新")
              }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12" class="hasMsg">
          <el-form-item
            :label="td('dpp.integration.singleWriteData', '单次写入数据')"
            prop="taskParams.description"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.taskParams.description"
              :placeholder="
                td(
                  'dpp.integration.singleWriteDataPlaceholder',
                  '请输入单次写入数据条数'
                )
              "
              type="number"
            >
              <template #append>{{
                td("dpp.integration.recordsUnit", "条")
              }}</template>
            </el-input>
            <div v-else class="form-readonly">
              {{
                form.taskParams.description
                  ? form.taskParams.description +
                    td("dpp.integration.recordsUnit", "条")
                  : "-"
              }}
            </div>
            <span class="msg"
              ><el-icon> <InfoFilled /> </el-icon
              >{{
                td("dpp.integration.singleWriteDataHint", "不输入默认值1000条")
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
            :label="td('dpp.integration.updatePrimaryKey', '更新主键字段')"
            prop="taskParams.selectedColumns"
            :rules="[
              {
                required: true,
                message: td(
                  'dpp.integration.updatePrimaryKeyRequired',
                  '请选择更新主键字段'
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
            :label="td('dpp.integration.postSql', '后置SQL')"
            prop="taskParams.postSql"
           :label-position="labelPosition">
            <el-input
              v-if="!info"
              v-model="form.taskParams.postSql"
              type="textarea"
              :placeholder="
                td('dpp.integration.postSqlPlaceholder', '请输入后置SQL')
              "
            />
            <div v-else class="form-readonly">
              {{ form.taskParams.postSql || "-" }}
            </div>
            <span class="msg"
              ><el-icon> <InfoFilled /> </el-icon
              >{{
                td("dpp.integration.postSqlHint", "数据同步完成后执行的SQL")
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

// 变量定义
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

const childComponent = ref(null); // 表字段
const tableFields = ref([]); // 来源表格
const createTypeList = ref([]); // 数据源列表

// 获取数据源列表
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

// 获取表列表
const getTablesByDatasourceId = async (id) => {
  TablesByDataSource.value = await fetchData(
    getTablesByDataSourceId,
    { datasourceId: id },
    loadingTables
  );
};

// 获取列数据
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

// 获取列数据
const getColumns = () => {
  return childComponent.value?.getColumns();
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

// 处理数据源变化
const handleDatasourceChange = (value) => {
  const selectedDatasource = createTypeList.value.find(
    (item) => item.id == value
  );
  if (selectedDatasource) {
    resetAndFetchTables(selectedDatasource);
  }
};

// 处理表变化
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
  // 清空表格字段数据
  ColumnByAssettab.value = [];
  TablesByDataSource.value = [];
  tableFields.value = [];
};
// 保存数据
const saveData = async () => {
  try {
    const valid = await dpModelRefs.value?.validate();
    if (!valid) return;

    // 没有 code 时生成唯一 code
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
    console.error("保存数据失败:", error);
    loading.value = false;
  }
};

const closeDialog = () => {
  off();
  // 关闭对话框
  emit("update", false);
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

// 处理数据源和列操作的共用函数
const handleDatasource = (datasource, assetId) => {
  if (datasource?.datasourceId) {
    getTablesByDatasourceId(datasource.datasourceId);
    // 如果需要处理 assetId，可以在此调用
    // getColumnByAssetIdList(assetId);
  } else {
    console.warn("无效的数据源信息", datasource);
  }
};
// 监听属性变化
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
