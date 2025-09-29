<template>
  <el-dialog v-model="visibleDialog" :draggable="true" :title="currentNode.data.name" showCancelButton
    :show-close="false" class="medium-dialog" destroy-on-close>
    <el-form ref="dpModelRefs" :model="form" label-width="110px" @submit.prevent v-loading="loadingList"
      :disabled="true">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务名称" prop="taskConfig.name" :rules="[
            { required: !info, message: '请输入任务名称', trigger: 'blur' },
          ]">
            <el-input v-if="!info" v-model="form.taskConfig.name" placeholder="请输入任务名称" />
            <div class="form-readonly" v-else>{{ form.taskConfig.name || '-' }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据开发类目" prop="taskConfig.catCode" :rules="[
            {
              required: !info,
              message: '请选择数据开发类目',
              trigger: 'change',
            },
          ]">
            <el-tree-select filterable v-model="form.taskConfig.catCode" :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }" value-key="code" placeholder="请选择数据开发类目"
              check-strictly />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任人" prop="taskConfig.personCharge" :rules="[
            { required: !info, message: '请选择责任人', trigger: 'change' },
          ]">
            <el-tree-select filterable v-model="form.taskConfig.personCharge" :data="userList" :props="{
              value: 'userId',
              label: 'nickName',
              children: 'children',
            }" value-key="ID" placeholder="请选择责任人" check-strictly @change="handleContactChange" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="taskConfig.contactNumber">
            <el-input v-if="!info" v-model="form.taskConfig.contactNumber" placeholder="请输入联系电话" disabled />
            <div class="form-readonly" v-else>{{ form.taskConfig.contactNumber || '-' }}</div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="描述" prop="taskConfig.description">
            <el-input v-if="!info" v-model="form.taskConfig.description" type="textarea" placeholder="请输入描述" />
            <div class="form-readonly" v-else>{{ form.taskConfig.description || '-' }}</div>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- <el-form
        v-loading="loading"
        ref="daDiscoveryTaskRef"
        :model="form"
        label-width="120px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              label="任务优先级"
              prop="priority"
              :rules="[
                {
                  required: true,
                  message: '请选择任务优先级',
                  trigger: 'change',
                },
              ]"
            >
              <el-select
                v-model="form.priority"
                placeholder="请选择任务优先级"
                clearable
                class="el-form-input-width"
                :disabled="false"
              >
                <el-option
                  v-for="dict in dpp_etl_node_priority"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form> -->
      <component :is="currentFormComponent" :form="form" :createTypeList="createTypeList"
        :dpp_etl_node_priority="dpp_etl_node_priority" :info="info" />

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="目标数据连接" prop="taskParams.writerDatasource.datasourceId" :rules="[
            {
              required: !info,
              message: '请选择目标数据连接',
              trigger: 'change',
            },
          ]">
            <el-select v-if="!info" v-model="form.taskParams.writerDatasource.datasourceId" placeholder="请选择目标数据连接"
              @change="handleDatasourceChange" filterable>
              <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName"
                :value="dict.id"></el-option>
            </el-select>
            <div class="form-readonly" v-else>{{createTypeList.find(item => item.id ===
              form.taskParams.writerDatasource.datasourceId)?.datasourceName || '-'}}</div>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="数据连接类型" prop="taskParams.writerDatasource.datasourceType">
            <el-input v-if="!info" v-model="form.taskParams.writerDatasource.datasourceType" placeholder="请输入数据连接类型"
              disabled />
            <div class="form-readonly" v-else>{{ form.taskParams.writerDatasource.datasourceType || '-' }}</div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据连接实例" prop="taskParams.writerDatasource.dbname">
            <el-input v-if="!info" v-model="form.taskParams.writerDatasource.dbname" placeholder="请输入数据连接实例" disabled />
            <div class="form-readonly" v-else>{{ form.taskParams.writerDatasource.dbname || '-' }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="选择表" prop="taskParams.target_asset_id" :rules="[
            { required: !info, message: '请选择表', trigger: 'change' },
          ]">
            <el-select v-if="!info" v-model="form.taskParams.target_asset_id" placeholder="请选择表" @change="handleChange"
              filterable>
              <el-option v-for="item in TablesByDataSource" :key="item.id" :label="item.tableName" :value="item.id" />
            </el-select>
            <div class="form-readonly" v-else>{{ form.taskParams.target_asset_id ? form.taskParams.target_asset_id : '-'
            }}</div>
          </el-form-item>
        </el-col>
      </el-row>
      <div class="h2-title">字段映射</div>

      <div style="margin-top: -20px">
        <YourChildComponent ref="childComponent" :tableFields="form.taskParams.columns"
          :toColumnsList="form.taskParams.target_columns" v-loading="loadingList" :isDisabled="true" :info="info" />
      </div>
      <div class="h2-title">输出配置</div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="前置SQL" prop="preSql">
            <el-input v-if="!info" v-model="form.preSql" type="textarea" placeholder="请输入前置SQL" />
            <div class="form-readonly" v-else>{{ form.preSql || '-' }}</div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="写入模式" prop="taskParams.writeModeType" :rules="[
            {
              required: !info,
              message: '请选择写入模式',
              trigger: 'change',
            },
          ]">
            <el-radio-group v-if="!info" v-model="form.taskParams.writeModeType">
              <el-radio :value="'1'">全量</el-radio>
              <el-radio :value="'2'">增量</el-radio>
              <el-radio :value="'3'">增量更新</el-radio>
            </el-radio-group>
            <div class="form-readonly" v-else>{{ form.taskParams.writeModeType == 1 ? '全量' :
              form.taskParams.writeModeType == 2 ? '增量' : '增量更新' || '-' }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单次写入数据" prop="taskParams.description">
            <el-input v-if="!info" v-model="form.taskParams.description" placeholder="请输入单次写入数据条数" type="number">
              <template #append>条</template>
            </el-input>
            <div class="form-readonly" v-else>{{ form.taskParams.description ? form.taskParams.description + '条' : '-'
            }}</div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24" class=" hasMsg">
          <el-form-item label="后置SQL" prop="taskParams.postSql">
            <el-input v-if="!info" v-model="form.taskParams.postSql" type="textarea" placeholder="请输入后置SQL" />
            <div v-else class="form-readonly">{{ form.taskParams.postSql || '-' }}</div>
            <span class="msg"><el-icon>
                <InfoFilled />
              </el-icon>数据同步完成后执行的SQL</span>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">关闭 </el-button>

        <el-button type="primary" @click="saveData" v-if="!info">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup>
import { dppNoPageList } from "@/api/da/asset/asset.js";
import {
  listDaDatasourceByProjectCode,
  getDaDatasource,
} from "@/api/da/dataSource/dataSource.js";
import { getDaDatasourceList } from "@/api/dp/model/model.js";
import {
  getTablesByDataSourceId,
  getColumnByAssetId,
  getNodeUniqueKey,
  etlTask,
} from "@/api/dpp/task/index.js";
import { fetchNodeUniqueKey } from "@/utils/opBase.js";
import { listAttTaskCat } from "@/api/att/cat/taskCat/taskCat.js";
import { deptUserTree } from "@/api/system/system/user.js";
const { proxy } = getCurrentInstance();
const { dpp_connection, dpp_etl_node_priority } = proxy.useDict(
  "dpp_connection",
  "dpp_etl_node_priority"
);

import useUserStore from "@/store/system/user.js";
import YourChildComponent from "./fieldMap.vue";
import CsvForm from "./base/csvForm.vue";
import ExcelInputForm from "./base/excelForm.vue";
import InputForm from "./base/tableForm";
import KafkaForm from "./base/kafkaForm";
const userStore = useUserStore();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  currentNode: { type: Object, default: () => ({}) },
  userList: { type: Object, default: () => ({}) },
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
let loading = ref(false); // 加载状态（全局）
let loadingList = ref(false); // 加载状态（列表）
let opens = ref(false); // 是否打开状态
let row = ref(); // 当前行数据
let TablesByDataSource = ref([]); // 数据源表格列表
let ColumnByAssettab = ref([]); // 资产表格列表
let dpModelRefs = ref(); // 表单引用
const tableFields = ref([]); // 来源表格字段
const createTypeList = ref([]); // 数据源列表

// 表单数据
const form = ref({
  taskConfig: {},
  id: "",
  priority: "",
  taskParams: {
    readerDatasource: "",
    clmt: "",
    asset_id: "",
    writerDatasource: "",
    target_table_name: "",
    columns: [],
    target_columns: [],
  },
});

const childComponent = ref(null); // 表字段组件引用
// 获取数据源列表
const getDatasourceList = async () => {
  try {
    loading.value = true;
    const response = await listDaDatasourceByProjectCode({
      projectCode: userStore.projectCode,
      projectId: userStore.projectId,
    });
    createTypeList.value = response.data.rows || [];
    console.log(
      "🚀 ~ getDatasourceList ~ response.data.rows:",
      response.data.rows
    );
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
  ColumnByAssettab.value = await fetchData(
    getColumnByAssetId,
    { assetId: id },
    loadingList
  );
};

// 获取列数据
const getColumns = () => {
  return childComponent.value?.getColumns(); // 从子组件获取列数据
};

// 通用的获取数据的函数
const fetchData = async (requestFn, params, loadingState) => {
  try {
    loadingState.value = true; // 设置加载状态
    const response = await requestFn(params); // 调用传入的请求函数
    return response.data;
  } catch (error) {
    console.error("获取数据出错:", error); // 捕获错误
  } finally {
    loadingState.value = false; // 关闭加载状态
  }
};

// 处理数据源变化
const resetAndFetchTables = async (selectedDatasource) => {
  TablesByDataSource.value = [];
  ColumnByAssettab.value = [];
  let { datasourceType, datasourceConfig, ip, port, id } = selectedDatasource;
  let code = JSON.parse(datasourceConfig); // 解析配置

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

  await getTablesByDatasourceId(id); // 获取表列表
};

// 处理数据源变化
const handleDatasourceChange = (value) => {
  const selectedDatasource = createTypeList.value.find(
    (item) => item.id == value
  );
  if (selectedDatasource) {
    resetAndFetchTables(selectedDatasource); // 更新表格和列数据
  }
};

// 处理表变化
const setTableName = (selectedDatasource) => {
  form.value.taskParams.target_table_name = selectedDatasource.tableName;
};

// 处理表格变化
const handleChange = (value) => {
  const selectedDatasource = TablesByDataSource.value.find(
    (item) => item.id == value
  );
  if (selectedDatasource) {
    setTableName(selectedDatasource);
    ColumnByAssettab.value = [];
    getColumnByAssetIdList(selectedDatasource.id); // 获取列数据
  }
};

// 重置表单数据
const off = () => {
  proxy.resetForm("dpModelRefs"); // 重置表单
  ColumnByAssettab.value = []; // 清空列数据
};
let daDiscoveryTaskRef = ref();
// 保存数据
const saveData = async () => {
  try {
    const valid = await dpModelRefs.value.validate(); // 校验表单
    if (!form.value.priority) return ElMessage.warning("保存失败，请选择任务优先级");
    if (!valid) return;

    let obj = { ...props.currentNode.data, priority: form.value.priority }; // 先复制已有数据

    // 如果没有 code，则获取新的 code 并更新 obj
    if (!props.currentNode.data.code) {
      const newCode = await fetchNodeUniqueKey(); // 确保 code 获取完成后再进行下一步
      obj = {
        ...props.currentNode.data,
        priority: form.value.priority,
        code: newCode,
      };
    }

    emit("confirm", obj);
    emit("update:visible", false); // 关闭弹框
  } catch (error) {
    console.error("保存数据失败:", error);
    loading.value = false;
  }
};

// 关闭弹框
const closeDialog = () => {
  off(); // 重置数据
  emit("update:visible", false); // 关闭弹框
};

// 监听弹框显隐
watchEffect(() => {
  if (!props.visible) {
    off(); // 弹框关闭时重置数据
    return;
  }
});
let componentType = ref();

// 提取任务参数
function extractTaskParams(data) {
  let { taskDefinitionList, taskConfig, id } = data;
  const inputTask =
    taskDefinitionList.find((item) => item.taskParams.type === 1) || {};
  const outputTask =
    taskDefinitionList.find((item) => item.taskParams.type === 2) || {};
  componentType.value = inputTask.componentType;

  form.value = {
    taskConfig: taskConfig,
    id: id,
    priority: props.currentNode.data.priority,
    taskParams: {
      readerDatasource: inputTask.taskParams?.readerDatasource || "",
      clmt: inputTask.taskParams?.clmt || "",
      asset_id: inputTask.taskParams?.asset_id || "",
      where: inputTask.taskParams?.where || "",
      file: inputTask.taskParams?.file || "",
      excelFile: inputTask.taskParams?.excelFile || "",
      startData: inputTask.taskParams?.startData || "",
      startColumn: inputTask.taskParams?.startColumn || "",
      topic: inputTask.taskParams?.topic || "",
      target_asset_id: outputTask.taskParams?.target_asset_id || "",
      writerDatasource: outputTask.taskParams?.writerDatasource || "",
      target_table_name: outputTask.taskParams?.target_table_name || "",
      columns: outputTask.taskParams?.toColumnsList || [],
      target_columns: outputTask.taskParams?.tableFields || [],
      writeModeType: outputTask.taskParams?.writeModeType || "",
      description: outputTask.taskParams?.description || "",
      postSql: outputTask.taskParams?.postSql || "",
    },
  };
  console.log(
    "🚀 ~ extractTaskParams ~ form.value.priority:wwwwww",
    form.value.priority
  );

  form.value.taskConfig.personCharge = Number(
    form.value.taskConfig.personCharge
  ); // 处理数据转换
  getTablesByDatasourceId(form.value.taskParams.writerDatasource.datasourceId);
}

const currentFormComponent = computed(() => {
  switch (componentType.value) {
    case "1":
      return InputForm;
    case "2":
      return ExcelInputForm;
    case "3":
      return KafkaForm;
    case "4":
      return CsvForm;
    case "31":
      return "TransformForm";
    case "91":
      return "OutputForm";
    default:
      return InputForm;
  }
});
const handleContactChange = (selectedValue) => {
  const selectedUser = props.userList.find(
    (user) => user.userId == selectedValue
  );
  form.value.taskConfig.contactNumber = selectedUser?.phonenumber || "";
};
// 获取数据列表
function getList() {
  loadingList.value = true;
  etlTask(props.currentNode.data.taskParams.subTaskId).then((response) => {
    extractTaskParams(response.data); // 提取任务参数
    loadingList.value = false;
  });
}

// 获取部门树和用户列表
let deptOptions = ref([]);
function getDeptTree() {
  listAttTaskCat()
    .then((response) => {
      const treeData = proxy.handleTree(response.data, "id", "parentId");

      treeData.forEach((node) => {
        node.value = node.value || node.id; // 这里可以根据需要调整默认值
      });

      deptOptions.value = [
        { name: "数据集成类目", value: "", children: treeData },
      ];
    })
    .catch((error) => {
      console.error("获取数据出错", error);
    });
}

// 处理弹框显示
function handleVisibleChange() {
  getDatasourceList();
  if (props.currentNode.data.taskParams.subTaskId) {
    getList(); // 获取任务数据
  }
  getDeptTree(); // 获取部门和用户列表
}
handleVisibleChange(); // 初始化获取数据
</script>



<style scoped lang="less">
.blue-text {
  color: #2666fb;
}

:deep(.el-select) {
  .el-select__wrapper.is-disabled {
    cursor: default;
    background-color: #fcfcfc;
    --el-select-disabled-color: #333;

    .el-select__suffix {
      display: none;
    }
  }
}
</style>
