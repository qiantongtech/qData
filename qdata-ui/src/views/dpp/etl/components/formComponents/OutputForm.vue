<template>
  <el-dialog v-model="visibleDialog" :draggable="true" :title="currentNode?.data?.name" showCancelButton
    :show-close="false" class="medium-dialog" destroy-on-close>
    <el-form ref="dpModelRefs" :model="form" label-width="110px" @submit.prevent v-loading="loading">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="节点名称" prop="name" :rules="[
            { required: true, message: '请输入节点名称', trigger: 'change' },
          ]">
            <el-input v-model="form.name" placeholder="请输入节点名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类型" prop="typeName">
            <el-select v-model="form.taskParams.typeName" placeholder="请输入类型" filterable disabled>
              <el-option v-for="dict in typeList" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="描述" prop="description">
            <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="目标数据连接" prop="taskParams.writerDatasource.datasourceId" :rules="[
            {
              required: true,
              message: '请选择目标数据连接',
              trigger: 'change',
            },
          ]">
            <el-select v-model="form.taskParams.writerDatasource.datasourceId" placeholder="请选择目标数据连接"
              @change="handleDatasourceChange" filterable>
              <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName"
                :value="dict.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据源类型" prop="taskParams.writerDatasource.datasourceType">
            <el-input v-model="form.taskParams.writerDatasource.datasourceType" placeholder="请输入数据源类型" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据源实例" prop="taskParams.writerDatasource.dbname">
            <el-input v-model="form.taskParams.writerDatasource.dbname" placeholder="请输入数据源实例" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="选择表" prop="taskParams.target_asset_id" :rules="[
            { required: true, message: '请选择表', trigger: 'change' },
          ]">
            <el-select v-model="form.taskParams.target_asset_id" placeholder="请选择表" @change="handleChange" filterable
              :loading="loadingTables">
              <el-option v-for="item in TablesByDataSource" :key="item.tableName" :label="item.tableName"
                :value="item.tableName" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="where条件" prop="where">
            <el-input v-model="form.taskParams.where" type="textarea" placeholder="请输入where条件" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="center">
        <span class="blue-text">字段映射</span>
      </el-divider>

      <div style="margin-top: -20px">
        <YourChildComponent ref="childComponent" :tableFields="tableFields" :toColumnsList="ColumnByAssettab"
          v-loading="loadingList" />
      </div>
      <el-divider content-position="center">
        <span class="blue-text">输出配置</span>
      </el-divider>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="前置SQL" prop="preSql">
            <el-input v-model="form.preSql" type="textarea" placeholder="请输入前置SQL" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="写入模式" prop="taskParams.writeModeType" :rules="[
            {
              required: true,
              message: '请选择写入模式',
              trigger: 'change',
            },
          ]">
            <el-radio-group v-model="form.taskParams.writeModeType">
              <el-radio :value="2">追加</el-radio>
              <el-radio :value="1">全量</el-radio>
              <el-radio :value="3">增量更新</el-radio>
            </el-radio-group>

          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单次写入数据" prop="taskParams.description">
            <el-input v-model="form.taskParams.description" placeholder="请输入单次写入数据条数" type="number">
              <template #append>条</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" v-if="form.taskParams.writeModeType == 3">
        <el-col :span="24">
          <el-form-item label="更新主键字段" prop="taskParams.selectedColumns" :rules="[
            {
              required: true,
              message: '请选择更新主键字段',
              trigger: 'change',
            },
          ]">
            <el-checkbox-group v-model="form.taskParams.selectedColumns">
              <el-checkbox v-for="item in ColumnByAssettab" :key="item.id" :label="item.columnName"
                :value="item.columnName">
                {{ item.columnName }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="postSql" prop="taskParams.postSql">
            <el-input v-model="form.taskParams.postSql" type="textarea" placeholder="请输入postSQL" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">关闭</el-button>
        <el-button type="primary" @click="saveData" v-if="!info">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup name="OutputForm">
import {
  listDaDatasource,
} from "@/api/da/datasource/daDatasource";

import {
  handleType2TaskParams
} from "@/views/dpp/etl/components/opBase.js";
import { typeList } from "@/utils/graph";
import {
  getTablesByDataSourceId,
  getColumnByAssetId,
  getNodeUniqueKey,
} from "@/api/dpp/etl/dppEtlTask";
const { proxy } = getCurrentInstance();
import useUserStore from "@/store/system/user";
import YourChildComponent from "./components/YourChildComponent.vue";
const userStore = useUserStore();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
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
let loadingTables = ref(false)
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
      datasourceType: "DM8,Oracle11,MySql,Oracle,Kingbase8,Hive,HDFS",
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

    taskParams.tableFields = fromColumns.length ? fromColumns : taskParams.tableFields;
    console.log("🚀 ~ saveData ~ fromColumns:", fromColumns)
    taskParams.toColumnsList = toColumns.length ? toColumns : ColumnByAssettab.value;
    const { target_columns, columns } = handleType2TaskParams(taskParams.tableFields, taskParams.toColumnsList);
    taskParams.target_columns = target_columns;
    taskParams.columns = columns;
    console.log("🚀 ~ saveData ~ taskParams.tableFields :", taskParams.tableFields)

    taskParams.outputFields = ColumnByAssettab.value;
    console.log("🚀 ~ saveData ~ form.value:", form.value)
    form.value.taskParams = { ...form.value.taskParams, ...taskParams }
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
  console.log("🚀 ~ watchEffect ~ form.value :", form.value)

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
