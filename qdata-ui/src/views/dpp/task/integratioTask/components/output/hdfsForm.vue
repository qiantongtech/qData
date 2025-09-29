<template>
  <!-- hdfs输出 -->
  <el-dialog v-model="visibleDialog" :draggable="true" :title="currentNode?.data?.name" showCancelButton
    :show-close="false" class="medium-dialog" destroy-on-close>
    <el-form ref="dpModelRefs" :model="form" label-width="110px" @submit.prevent v-loading="loading" :disabled="info">
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
          <el-form-item label="编码" prop="taskParams.encoding" :rules="[
            { required: true, message: '请输入编码', trigger: 'change' },
          ]">
            <el-input v-model="form.taskParams.encoding" placeholder="请输入编码" />
          </el-form-item>
        </el-col>

        <!-- <el-col :span="12">
          <el-form-item label="数据连接类型" prop="taskParams.writerDatasource.datasourceType">
            <el-input v-model="form.taskParams.writerDatasource.datasourceType" placeholder="请输入数据连接类型" disabled />
          </el-form-item>
        </el-col> -->
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="文件路径" prop="taskParams.path" :rules="[
            { required: true, message: '请输入文件路径', trigger: 'change' },
          ]">
            <el-input v-model="form.taskParams.path" placeholder="请输入文件路径" />
          </el-form-item>
        </el-col>
        <!--        <el-col :span="12">-->
        <!--          <el-form-item label="文件名称" prop="taskParams.fileName" :rules="[-->
        <!--            { required: true, message: '请输入文件路径', trigger: 'change' },-->
        <!--          ]">-->
        <!--            <el-input v-model="form.taskParams.fileName" placeholder="请输入文件名称" />-->
        <!--          </el-form-item>-->
        <!--        </el-col>-->
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="文件类型" prop="taskParams.fileType" :rules="[
            { required: true, message: '请输入文件类型', trigger: 'change' },
          ]">
            <el-select v-model="form.taskParams.fileType" placeholder="请选择文件类型">
              <el-option v-for="item in fileTypes" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分隔符" prop="taskParams.fieldDelimiter" :rules="[
            { required: true, message: '请输入分隔符', trigger: 'change' },
          ]">
            <el-input v-model="form.taskParams.fieldDelimiter" placeholder="请输入分隔符" />
          </el-form-item>
        </el-col>

      </el-row>
      <el-row :gutter="20">
        <el-col :span="12" v-if="form.taskParams.fileType == 'csv'">
          <el-form-item label="压缩方式" prop="taskParams.compression" :rules="[]">
            <el-select v-model="form.taskParams.compression" placeholder="请选择压缩方式">
              <el-option v-for="item in compressionOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>

      </el-row>
      <!-- <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据连接实例" prop="taskParams.writerDatasource.dbname">
            <el-input v-model="form.taskParams.writerDatasource.dbname" placeholder="请输入数据连接实例" disabled />
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
      </el-row> -->

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="配置参数" prop="taskParams.hadoopConfig">
            <el-input v-model="form.taskParams.hadoopConfig" type="textarea"
              placeholder='例如: {&quot;kerberosKeytabFilePath&quot;&colon;&quot;/path/to/keytab/file&quot;}' />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="center">
        <span class="blue-text">属性字段</span>
      </el-divider>
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="openDialog()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
        </el-row>
      </div>
      <el-table stripe height="240px" v-loading="loadingList" :data="ColumnByAssettab">
        <el-table-column label="序号" type="index" width="80" align="left">
          <template #default="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="字段名称" align="left" prop="columnName" :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            {{ scope.row.columnName || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="字段类型" align="left" prop="columnType">
          <template #default="scope">
            {{ scope.row.columnType || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit"
              @click="openDialog({ ...scope.row, index: scope.$index + 1 })">修改</el-button>
            <el-button type="danger" link icon="Delete" :disabled="multiple"
              @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-divider content-position="center">
        <span class="blue-text">字段映射</span>
      </el-divider>
      <div style="margin-top: -20px">
        <YourChildComponent ref="childComponent" :tableFields="tableFields" :toColumnsList="ColumnByAssettab"
          v-loading="loadingList" type="hdfs" />
      </div>
      <el-divider content-position="center">
        <span class="blue-text">输出配置</span>
      </el-divider>
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
            </el-radio-group>

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
  <hdfsFUploadDialog :visible="open" title="属性字段编辑" @update:visible="open = $event" @confirm="handletaskConfig"
    :data="row" type="1" />
</template>
<script setup name="OutputForm">
import {
  listDaDatasource,
} from "@/api/da/dataSource/dataSource.js";
import hdfsFUploadDialog from "../hdfsFUpload.vue";
import {
  handleType2TaskParams
} from "@/views/dpp/utils/opBase.js";
import { typeList } from "@/utils/graph.js";
import {
  getTablesByDataSourceId,
  getColumnByAssetId,
  getNodeUniqueKey,
} from "@/api/dpp/task/index.js";
const { proxy } = getCurrentInstance();
import useUserStore from "@/store/system/user.js";
import YourChildComponent from "../fieldMap.vue";
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
const fileTypes = [
  { label: 'csv', value: 'csv' },
  { label: 'text', value: 'text' }
]
const compressionOptions = [
  { label: 'gzip', value: 'gzip' },
  { label: 'bzip2', value: 'bzip2' },
  { label: 'lzo', value: 'lzo' },
  { label: 'snappy', value: 'snappy' }
]
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
// 修改
const open = ref(false);
const openDialog = (obj) => {
  row.value = obj;
  open.value = true;
};
const handletaskConfig = (obj) => {
  if (row.value?.index) {
    ColumnByAssettab.value[row.value.index - 1] = {
      ...ColumnByAssettab.value[row.value.index - 1],
      isChecked: true,
      ...obj,
    };
  } else {
    ColumnByAssettab.value.push({ ...obj, isChecked: true, });
  }
}// 删除
const handleDelete = (row) => {
  ColumnByAssettab.value = ColumnByAssettab.value.filter(
    (item) => item.id !== row.id
  );
};
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
      datasourceType: "HDFS",
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
  let { datasourceType, datasourceConfig, ip, port, id, config } = selectedDatasource;
  let code = JSON.parse(datasourceConfig);
  form.value.taskParams.hadoopConfig = config;
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

  // await getTablesByDatasourceId(id);
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
    taskParams.toColumnsList = toColumns.length ? toColumns : ColumnByAssettab.value;
    const { target_columns, columns } = handleType2TaskParams(taskParams.tableFields, taskParams.toColumnsList);
    var target_columns_list = [];
    target_columns.forEach((item) => {
      taskParams.toColumnsList.forEach((item2) => {
        if (item == item2.columnName) {
          target_columns_list.push({
            "name": item2.columnName,
            "type": item2.columnType
          })
        }
      });
    });
    taskParams.outputFields = ColumnByAssettab.value;
    taskParams.target_columns = taskParams.toColumnsList;
    taskParams.columns = columns;
    form.value.taskParams = { ...form.value.taskParams, ...taskParams }
    emit("confirm", form.value);

  } catch (error) {
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


// 监听属性变化
watchEffect(() => {
  if (!props.visible) {
    off();
    return;
  }
  getDatasourceList();

  form.value = deepCopy(props.currentNode?.data || {});
  console.log("🚀 ~ watchEffect ~ props.currentNode?.data :", props.currentNode?.data)
  const taskParams = form.value?.taskParams || {};
  tableFields.value = taskParams.tableFields?.length
    ? deepCopy(taskParams.tableFields)
    : deepCopy(taskParams.inputFields);
  ColumnByAssettab.value = taskParams.toColumnsList || [];
});
</script>


<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
