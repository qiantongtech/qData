<template>
  <el-dialog v-model="visibleDialog" :draggable="true" :title="currentNode?.data?.name" showCancelButton
    :show-close="false" class="medium-dialog" destroy-on-close>
    <el-form ref="dpModelRefs" :model="form" label-width="110px" @submit.prevent v-loading="loading" :rules="rules">
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
          <el-form-item label="数据源连接" prop="taskParams.readerDatasource.datasourceId" :rules="[
            {
              required: true,
              message: '请选择源数据库连接',
              trigger: 'change',
            },
          ]">
            <el-select v-model="form.taskParams.readerDatasource.datasourceId" placeholder="请选择源数据库连接"
              @change="handleDatasourceChange" filterable>
              <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName"
                :value="dict.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主题名称" prop="taskParams.topic" :rules="[
            {
              required: true,
              message: '请输入主题名称',
              trigger: 'change',
            },
          ]">
            <el-input v-model="form.taskParams.topic" placeholder="请输入主题名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="配置参数" prop="taskParams.config">
            <el-input v-model="form.taskParams.config" type="textarea"
              placeholder='例如: {"group.id"&colon;"demo_test"}' />
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
        <el-table-column label="JSON解析取值" align="left" prop="key">
          <template #default="scope">
            {{ scope.row.key || "-" }}
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
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">关闭</el-button>
        <el-button type="primary" @click="saveData" v-if="!info">保存</el-button>
      </div>
    </template>
  </el-dialog>
  <kafkaUploadDialog :visible="open" title="属性字段编辑" @update:visible="open = $event" @confirm="handletaskConfig"
    :data="row" />
</template>
<script setup>
import { getDaDatasourceList } from "@/api/dp/model/model.js";
import { typeList } from "@/utils/graph.js";
import {
  getTablesByDataSourceId,
  getColumnByAssetId,
  getNodeUniqueKey,
} from "@/api/dpp/task/index.js";
import kafkaUploadDialog from "../kafkaUpload.vue";
import { listDaDatasourceByProjectCode } from "@/api/da/dataSource/dataSource.js";
const { proxy } = getCurrentInstance();
import useUserStore from "@/store/system/user.js";
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
let rules = {
  config: [
    {
      trigger: "blur",
      validator: (rule, value, callback) => {
        if (value === null || value === undefined || value === "") {
          callback();
          return;
        }
        var flag = false;
        if (typeof value === "string") {
          try {
            const obj = JSON.parse(value);
            if (typeof obj === "object" && obj) {
              flag = true;
            }
          } catch (e) { }
        }
        if (flag) {
          callback();
        } else {
          callback("不是一个正确的JSON格式");
        }
      },
    },
  ],
};
// 变量定义
let loading = ref(false);
let loadingList = ref(false);
let TablesByDataSource = ref([]);
let ColumnByAssettab = ref([]);
// 修改
const open = ref(false);
let row = ref({});
const openDialog = (obj) => {
  row.value = obj;
  open.value = true;
};
const handletaskConfig = (obj) => {
  if (row.value?.index) {
    ColumnByAssettab.value[row.value.index - 1] = {
      ...ColumnByAssettab.value[row.value.index - 1],
      ...obj,
    };
  } else {
    ColumnByAssettab.value.push({ ...obj });
  }
};
// 删除
const handleDelete = (row) => {
  if (!row?.columnName) {
    console.warn("删除失败：row 没有 columnName");
    return;
  }
  ColumnByAssettab.value = ColumnByAssettab.value.filter(
    (item) => item.columnName !== row.columnName
  );
};
let dpModelRefs = ref();
let form = ref({});
const childComponent = ref(null); // 表字段
const tableFields = ref([]); // 来源表格
const createTypeList = ref([]); // 数据源列表
// 获取数据源列表
const getDatasourceList = async () => {
  try {
    loading.value = true;
    const response = await listDaDatasourceByProjectCode({
      projectCode: userStore.projectCode,
      projectId: userStore.projectId,
      datasourceType: "Kafka",
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
    loadingList
  );
};
// 获取列数据
const getColumnByAssetIdList = async (id) => {
  // ColumnByAssettab.value = await fetchData(
  //   getColumnByAssetId,
  //   { assetId: id },
  //   loadingList
  // );
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
  let { datasourceType, datasourceConfig, ip, port, id, config } =
    selectedDatasource;
  let code = JSON.parse(datasourceConfig);
  form.value.taskParams.config = config;
  form.value.taskParams.asset_id = "";
  form.value.taskParams.readerDatasource = {
    datasourceType,
    datasourceConfig,
    ip,
    port,
    dbname: code.dbname,
    datasource_id: id,
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
  form.value.taskParams.table_name = selectedDatasource.tableName;
};

const handleChange = (value) => {
  const selectedDatasource = TablesByDataSource.value.find(
    (item) => item.id == value
  );
  if (selectedDatasource) {
    setTableName(selectedDatasource);
    // ColumnByAssettab.value = [];
    getColumnByAssetIdList(selectedDatasource.id);
  }
};
const off = () => {
  proxy.resetForm("dpModelRefs");
  // 清空表格字段数据
  // ColumnByAssettab.value = [];
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
      form.value?.taskParams.type == "1" &&
      (!ColumnByAssettab.value || ColumnByAssettab.value.length == 0)
    ) {
      return proxy.$message.warning("校验未通过，请选择属性字段");
    }
    // 如果没有 code，就调用接口获取唯一的 code
    if (!form.value.code) {
      loading.value = true;
      const response = await getNodeUniqueKey({
        projectCode: userStore.projectCode || "133545087166112",
        projectId: userStore.projectId,
      });
      loading.value = false; // 结束加载状态
      form.value.code = response.data; // 设置唯一的 code
    }
    const taskParams = form.value?.taskParams;
    taskParams.tableFields = ColumnByAssettab.value;
    taskParams.columnsList = ColumnByAssettab.value.map(({ columnName, columnType }) => ({
      colName: columnName,
      dataType: columnType,
    }));
    taskParams.columns = taskParams.tableFields.map((item) => {
      return {
        name: item.columnName,
        type: item.columnType,
        key: item.key
      };
    });
    emit("confirm", form.value);

  } catch (error) {
    console.error("保存数据失败:", error);
    loading.value = false; // 确保错误发生时也结束加载状态
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
    // getTablesByDatasourceId(datasource.datasourceId);
  } else {
    console.warn("无效的数据源信息", datasource);
  }
};
// 监听属性变化
watchEffect(() => {
  if (props.visible) {
    getDatasourceList();

    // 数据源
    form.value = deepCopy(props.currentNode.data);
    ColumnByAssettab.value = deepCopy(props.currentNode.data.taskParams.tableFields) || [];
    if (props.currentNode.data.code) {
      handleDatasource(props.currentNode.data.taskParams?.readerDatasource || "");
    }
  } else {
    off();
  }
});

</script>
<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
