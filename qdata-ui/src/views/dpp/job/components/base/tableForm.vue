<template>
  <el-row :gutter="20">
    <el-col :span="12">
      <el-form-item label="连接方式" prop="clmt">
        <el-radio-group v-if="!info" @change="handleReleaseStateChange" v-model="form.taskParams.clmt"
          style="width: 100%">
          <el-radio v-for="dict in dpp_connection" :key="dict.value" :label="dict.value">
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
        <div class="form-readonly" v-else>{{dpp_connection.find(item => item.value == form.taskParams.clmt)?.label ||
          '-'}}</div>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form v-loading="loading" ref="daDiscoveryTaskRef" :model="form" label-width="120px" @submit.prevent>
        <el-form-item label="任务优先级" prop="priority" :rules="[
          {
            required: true,
            message: '请选择任务优先级',
            trigger: 'change',
          },
        ]">
          <el-select v-if="!info" style="width: 100%" v-model="form.priority" placeholder="请选择任务优先级" clearable
            class="el-form-input-width" :disabled="false">
            <el-option v-for="dict in dpp_etl_node_priority" :key="dict.value" :label="dict.label"
              :value="dict.value" />
          </el-select>
          <div class="form-readonly" v-else>{{dpp_etl_node_priority.find(item => item.value == form.priority)?.label ||
            '-'}}</div>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
  <el-row :gutter="20" v-if="form.taskParams.clmt == '1'">
    <el-col :span="12">
      <el-form-item label="资产表" prop="taskParams.asset_id" :rules="[
        { required: true, message: '请选择资产表', trigger: 'change' },
      ]">
        <el-select v-if="!info" v-model="form.taskParams.asset_id" filterable @change="handleAssetTableChange">
          <el-option v-for="item in dppNoPageListList" :key="item.id" :label="item.tableName" :value="item.id" />
        </el-select>
        <div class="form-readonly" v-else>{{ form.taskParams.asset_id ? form.taskParams.asset_id : '-' }}</div>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20" v-if="form.taskParams.clmt == '0'">
    <el-col :span="12">
      <el-form-item label="源数据库连接" prop="taskParams.readerDatasource.datasourceId" :rules="[
        { required: true, message: '请选择源数据库连接', trigger: 'change' },
      ]">
        <el-select v-if="!info" v-model="form.taskParams.readerDatasource.datasourceId" placeholder="请选择源数据库连接"
          @change="handleDatasourceChange" filterable>
          <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName"
            :value="dict.id"></el-option>
        </el-select>
        <div class="form-readonly" v-else>{{ form.taskParams.readerDatasource.datasourceId ?
          form.taskParams.readerDatasource.datasourceId : '-' }}</div>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item label="数据连接类型" prop="taskParams.readerDatasource.datasourceType">
        <el-input v-if="!info" v-model="form.taskParams.readerDatasource.datasourceType" placeholder="请输入数据连接类型"
          disabled />
        <div class="form-readonly" v-else>{{ form.taskParams.readerDatasource.datasourceType || '-' }}</div>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20" v-if="form.taskParams.clmt == '0'">
    <el-col :span="12">
      <el-form-item label="数据连接实例" prop="taskParams.readerDatasource.dbname">
        <el-input v-if="!info" v-model="form.taskParams.readerDatasource.dbname" placeholder="请输入数据连接实例" disabled />
        <div class="form-readonly" v-else>{{ form.taskParams.readerDatasource.dbname || '-' }}</div>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item label="选择表" prop="taskParams.asset_id"
        :rules="[{ required: true, message: '请选择表', trigger: 'change' }]">
        <el-select v-if="!info" v-model="form.taskParams.asset_id" filterable @change="handleChange">
          <el-option v-for="item in TablesByDataSource" :key="item.id" :label="item.tableName" :value="item.id" />
        </el-select>
        <div class="form-readonly" v-else>{{ form.taskParams.asset_id ? form.taskParams.asset_id : '-' }}</div>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="24">
      <el-form-item label="where条件" prop="where">
        <el-input v-if="!info" v-model="form.taskParams.where" type="textarea" placeholder="请输入where条件" />
        <div class="form-readonly" v-else>{{ form.taskParams.where || '-' }}</div>
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script setup>
const props = defineProps({
  form: Object, // 父组件传入 form
  createTypeList: Array, // 父组件传入 createTypeList
  dpp_etl_node_priority: Array,
  info: Boolean,
}); // 确保 form 来自父组件
import useUserStore from "@/store/system/user.js";
import {
  getTablesByDataSourceId,
  getColumnByAssetId,
} from "@/api/dpp/task/index.js";
const userStore = useUserStore();
import { dppNoPageList } from "@/api/da/asset/asset.js";
import { getDaDatasource } from "@/api/da/dataSource/dataSource.js";
const { proxy } = getCurrentInstance();
const { dpp_connection } = proxy.useDict("dpp_connection");
let loadingList = ref(false);
const dppNoPageListList = ref([]);
const ColumnByAssettab = ref([]);
const TablesByDataSource = ref([]);
// 获取表列表
const getTablesByDatasourceId = async (id) => {
  console.log("🚀 shiewhu ");
  TablesByDataSource.value = await fetchData(
    getTablesByDataSourceId,
    { datasourceId: id },
    loadingList
  );
};
const resetAndFetchTables = async (selectedDatasource) => {
  TablesByDataSource.value = [];
  ColumnByAssettab.value = [];
  let { datasourceType, datasourceConfig, ip, port, id } = selectedDatasource;
  let code = JSON.parse(datasourceConfig);
  props.form.taskParams.asset_id = "";
  props.form.taskParams.readerDatasource = {
    datasourceType,
    datasourceConfig,
    ip,
    port,
    dbname: code.dbname,
    datasource_id: id,
    datasourceId: id,
  };

  await getTablesByDatasourceId(id);
};
// 获取资产表数据
const getdppNoPageListList = async () => {
  const response = await dppNoPageList({
    projectCode: userStore.projectCode,
    projectId: userStore.projectId,
  });
  dppNoPageListList.value = response.data;
};

// 连接方式切换
const handleReleaseStateChange = (value) => {
  if (value === "1") {
    getdppNoPageListList();
  }

  // **确保 form.taskParams 存在**
  if (props.form && props.form.taskParams) {
    props.form.taskParams.asset_id = "";
    props.form.taskParams.readerDatasource = {};
  }

  ColumnByAssettab.value = [];
  TablesByDataSource.value = [];
};

// 处理数据源变化
const handleDatasourceChange = (value) => {
  const selectedDatasource = props.createTypeList.find(
    (item) => item.id == value
  );
  if (selectedDatasource) {
    resetAndFetchTables(selectedDatasource);
  }
};

// 处理表变化
const setTableName = (selectedDatasource) => {
  props.form.taskParams.table_name = selectedDatasource.tableName;
};

const handleChange = (value) => {
  const selectedDatasource = TablesByDataSource.value.find(
    (item) => item.id == value
  );
  if (selectedDatasource) {
    setTableName(selectedDatasource);
    ColumnByAssettab.value = [];
    getColumnByAssetIdList(selectedDatasource.id);
  }
};

// 获取列数据
const getColumnByAssetIdList = async (id) => {
  ColumnByAssettab.value = await fetchData(
    getColumnByAssetId,
    { assetId: id },
    loadingList
  );
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

const handleAssetTableChange = (value) => {
  // 确保访问 props.form
  if (props.form && props.form.taskParams) {
    props.form.taskParams.asset_id = value;
  }

  // 找到对应的选中项
  const selectedItem = dppNoPageListList.value.find((item) => item.id == value);
  // 调用 API 获取数据源信息
  getDaDatasource(selectedItem.datasourceId).then((response) => {
    let { datasourceType, datasourceConfig, ip, port, id } = response.data;
    let code = JSON.parse(datasourceConfig);
    // 更新 readerDatasource
    props.form.taskParams.readerDatasource = {
      datasourceType,
      datasourceConfig,
      ip,
      port,
      dbname: code.dbname,
      datasource_id: id,
      datasourceId: id,
    };
    setTableName(response.data);
  });

  // 获取列数据
  ColumnByAssettab.value = [];
  getColumnByAssetIdList(value);
};
watchEffect(() => {
  if (props.form.taskParams.clmt == 1) {
    getdppNoPageListList();
  } else {
    if (props.form.taskParams.readerDatasource.datasourceId) {
      getTablesByDatasourceId(
        props.form.taskParams.readerDatasource.datasourceId
      );
    }
  }
});
</script>
