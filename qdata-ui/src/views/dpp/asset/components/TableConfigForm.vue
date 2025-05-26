<template>
  <el-row :gutter="20">
    <el-col :span="12">
      <el-form-item label="源数据库连接" prop="datasourceId" :rules="[
        { required: true, message: '请选择源数据库连接', trigger: 'change' },
      ]">
        <el-select v-model="localForm.datasourceId" placeholder="请选择源数据库连接" @change="handleDatasourceChange" filterable
          :loading="loading" :disabled="localForm.id">
          <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName" :value="dict.id" />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item label="数据源类型" prop="datasourceType">
        <el-input v-model="localForm.datasourceType" disabled />
      </el-form-item>
    </el-col>
  </el-row>

  <el-row :gutter="20">
    <el-col :span="12">
      <el-form-item label="数据源实例" prop="dbname">
        <el-input v-model="localForm.dbname" disabled />
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item label="选择表" prop="tableName" :rules="[{ required: true, message: '请选择表', trigger: 'change' }]">
        <el-select v-model="localForm.tableName" filterable @change="handleTableChange" :loading="loadingList"
          :disabled="localForm.id">
          <el-option v-for="item in tablesByDataSource" :key="item.tableName" :label="item.tableName"
            :value="item.tableName" />

        </el-select>
      </el-form-item>
    </el-col>
  </el-row>
  <!-- tableComment -->
</template>

<script setup>
import { ref, watch, getCurrentInstance } from "vue";
import { ElMessage } from "element-plus";
import useUserStore from "@/store/system/user";
import {
  getTablesByDataSourceId,
  getColumnByAssetId,
} from "@/api/dpp/etl/dppEtlTask";
import { dppNoPageList } from "@/api/da/asset/daAsset";
import { getDaDatasource, listDaDatasourceNoKafkaByProjectCode } from "@/api/da/datasource/daDatasource";
const props = defineProps({
  form: Object,
});
const emit = defineEmits(["update:form"]);

const { proxy } = getCurrentInstance();
const { dpp_connection } = proxy.useDict("dpp_connection");

const userStore = useUserStore();
const createTypeList = ref([]); // 数据源列表
let loading = ref(false)
const getDatasourceList = async () => {
  console.log("🚀 ~ getDatasourceList ~ getDatasourceList:", getDatasourceList)
  try {
    loading.value = true;
    const response = await listDaDatasourceNoKafkaByProjectCode({
      projectCode: userStore.projectCode,
      projectId: userStore.projectId,
    });
    createTypeList.value = response.data || [];
  } finally {
    loading.value = false;
  }
};
const loadingList = ref(false);
const dppAssetList = ref([]);
const columnsByAssetTable = ref([]);
const tablesByDataSource = ref([]);

const localForm = ref({ ...props.form });

// 同步 props.form 到 localForm

getDatasourceList()

// 通用数据获取函数
const fetchData = async (requestFn, params, loadingState) => {
  try {
    loadingState.value = true;
    const response = await requestFn(params);
    return response?.data || [];
  } finally {
    loadingState.value = false;
  }
};

// 获取表列表
const getTablesByDatasourceId = async (id) => {
  tablesByDataSource.value = await fetchData(
    getTablesByDataSourceId,
    { datasourceId: id },
    loadingList
  );
};

// 数据源变化时
const handleDatasourceChange = async (id) => {
  const selected = createTypeList.value.find((item) => item.id == id);
  if (!selected) return;
  const { datasourceType, datasourceConfig, ip, port } = selected;
  const config = JSON.parse(datasourceConfig);

  Object.assign(localForm.value, {
    datasourceType,
    dbname: config.dbname,
    datasourceId: id,
  });
  localForm.value.tableName = "";
  emit("update:form", localForm.value);

  await getTablesByDatasourceId(id);
  columnsByAssetTable.value = [];
};

// 表变化时
const handleTableChange = (tableName) => {
  const selected = tablesByDataSource.value.find((item) => item.tableName == tableName);
  if (!selected) return;
  localForm.value.tableName = tableName;
  localForm.value.tableComment = selected.tableComment;
  console.log("🚀 ~ handleTableChange ~ localForm.value.tableComment:", localForm.value.tableComment)
  emit("update:form", localForm.value);
  columnsByAssetTable.value = [];
};
// 可用于外部调用，设置资产 ID 并加载数据
const handleAssetTableChange = async (assetId) => {
  localForm.value.assetId = assetId;

  const selected = dppAssetList.value.find((item) => item.id == assetId);
  if (!selected) return;

  const response = await getDaDatasource(selected.datasourceId);
  const { datasourceType, datasourceConfig, ip, port, id } = response.data;
  const config = JSON.parse(datasourceConfig);

  Object.assign(localForm.value, {
    datasourceType,
    ip,
    port,
    dbname: config.dbname,
    datasourceId: id,
  });

  emit("update:form", localForm.value);

  await getTablesByDatasourceId(id);
  columnsByAssetTable.value = [];

};
watchEffect(() => {
  localForm.value = { ...props.form };

});

// if (localForm.value?.datasourceId) {
//   getTablesByDatasourceId(localForm.value.datasourceId);
// }
</script>
