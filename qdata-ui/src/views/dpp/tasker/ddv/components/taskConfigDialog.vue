<template>
  <el-dialog v-model="visibleDialog" draggable class="dialog" :title="title" destroy-on-close width="60%"
    :append-to="$refs['app-container']">
    <el-form ref="daDiscoveryTaskRef" :model="form" label-width="120px" @submit.prevent>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务名称" prop="taskConfig.name" :rules="[
            { required: true, message: '请输入任务名称', trigger: 'blur' },
          ]">
            <el-input v-model="form.taskConfig.name" placeholder="请输入任务名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据开发类目" prop="taskConfig.catCode" :rules="[
            {
              required: true,
              message: '请选择数据开发类目',
              trigger: 'change',
            },
          ]">
            <el-tree-select v-model="form.taskConfig.catCode" :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }" value-key="id" placeholder="请选择数据开发类目"
              check-strictly />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任人" prop="taskConfig.personCharge" :rules="[
            { required: true, message: '请选择责任人', trigger: 'change' },
          ]">
            <el-tree-select v-model="form.taskConfig.personCharge" :data="userList" :props="{
              value: 'userId',
              label: 'nickName',
              children: 'children',
            }" value-key="ID" placeholder="请选择责任人" check-strictly @change="handleContactChange" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="taskConfig.contactNumber">
            <el-input v-model="form.taskConfig.contactNumber" placeholder="请输入联系电话" disabled />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="调度周期" prop="taskConfig.crontab" :rules="[
            { required: true, message: '请输入调度周期', trigger: 'change' },
          ]">
            <el-input v-model="form.taskConfig.crontab" placeholder="请输入调度周期" readonly>
              <template #append>
                <el-button type="primary" @click="handleShowCron" style="background-color: #2666fb; color: #fff">
                  配置
                  <i class="el-icon-time el-icon--right"></i>
                </el-button>
              </template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务状态" prop="taskConfig.releaseState" :rules="[
            { required: true, message: '请选择任务状态', trigger: 'change' },
          ]">
            <el-radio-group style="width: 100%" v-model="form.taskConfig.releaseState" class="el-form-input-width">
              <el-radio v-for="dict in dpp_etl_task_status" :key="dict.value" :value="dict.value"
                :disabled="dict.value == 1">
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="任务描述" prop="taskConfig.description">
            <el-input v-model="form.taskConfig.description" type="textarea" placeholder="请输入任务描述" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据源类型" prop="taskParams.typaCode
              {
                required: true,
                message: '请选择数据源类型',
                trigger: 'change',
              },
            ]">
            <el-tree-select v-model="form.taskParams.typaCode" :data="treeData"
              :props="{ value: 'label', label: 'label', children: 'children' }" value-key="label" check-strictly
              @change="getDaDatasource(true)" />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="form.taskParams.typaCode == 'SparkSql'">
          <el-form-item label="分段执行符号" prop="taskParams.segm">
            <el-input v-model="form.taskParams.segm" placeholder="请输入分段执行符号" />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="form.taskParams.typaCode != 'SparkSql'">
          <el-form-item label="数据源连接" prop="taskParams.datasources.datasourceId" :rules="[
            {
              required: true,
              message: '请选择数据源连接',
              trigger: 'change',
            },
          ]">
            <el-select v-model="form.taskParams.datasources.datasourceId" placeholder="请输入数据源连接"
              @change="handleDatasourceChange" filterable>
              <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName"
                :value="dict.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12" v-if="form.taskParams.typaCode != 'SparkSql'">
          <el-form-item label="SQL类型" prop="taskParams.sqlType" :rules="[
            {
              required: true,
              message: '请选择SQL类型',
              trigger: 'change',
            },
          ]">
            <el-radio-group v-model="form.taskParams.sqlType" inline>
              <el-radio v-for="option in visibleRadioOptions" :key="option.id" :value="option.id">
                {{ option.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="form.taskParams.typaCode != 'SparkSql'">
          <el-form-item label="分段执行符号" prop="taskParams.segm">
            <el-input v-model="form.taskParams.segm" placeholder="请输入分段执行符号" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="center">
        <span class="blue-text">SQL参数</span>
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

      <el-table stripe height="310px" :data="form.taskParams.localParams">
        <el-table-column label="序号" type="index" width="80" align="center">
          <template #default="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="参数名称" align="center" prop="prop" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.prop || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="参数类型" align="center" prop="type">
          <template #default="scope">
            {{ scope.row.type || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="参数值" align="center" prop="value">
          <template #default="scope">
            {{ scope.row.value || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit"
              @click="openDialog({ ...scope.row, index: scope.$index + 1 })">修改</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>

    <template #footer>
      <div style="text-align: right">
        <!-- 关闭按钮 -->
        <el-button @click="closeDialog">关闭</el-button>
        <!-- 保存按钮 -->
        <el-button type="primary" @click="saveData" v-if="!info">保存</el-button>
      </div>
    </template>
  </el-dialog>
  <taskConfigUploadDialog :visible="open" @update:visible="open = $event" @confirm="handletaskConfig" :data="row" />
  <el-dialog title="Cron表达式生成器" v-model="openCron" :append-to="$refs['app-container']" destroy-on-close>
    <crontab ref="crontabRef" @hide="openCron = false" @fill="crontabFill" :expression="expression">
    </crontab>
    <!--    <crontab-->
    <!--      ref="crontabRef"-->
    <!--      @hide="openCron = false"-->
    <!--      @fill="crontabFill"-->
    <!--      :expression="expression"-->
    <!--      :Crontab="false"-->
    <!--    >-->
    <!--    </crontab>-->
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import Crontab from "@/components/Crontab/index.vue";
const { proxy } = getCurrentInstance();
import {
  getDaDatasourceList,
  getNodeUniqueKey,
} from "@/api/dpp/etl/dppEtlTask";
import { listDaDatasourceNoKafkaByProjectCode } from "@/api/da/datasource/daDatasource";
const { dpp_etl_task_execution_type, dpp_etl_task_status } = proxy.useDict(
  "dpp_etl_task_execution_type",
  "dpp_etl_task_status"
);
import taskConfigUploadDialog from "./taskConfigUploadDialog";
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
import { treeData } from "./data";
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  data: { type: Object, default: () => ({}) },
  deptOptions: { type: Object, default: () => ({}) },
  userList: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
});

const emit = defineEmits(["update:visible", "confirm"]);
let radioOptions = ref([
  { componentType: "51", label: "查询", taskType: "SQL", id: "0", show: true },
  {
    componentType: "51",
    label: "非查询",
    taskType: "SQL",
    id: "1",
    show: true,
  },
  {
    componentType: "52",
    label: "储存过程",
    taskType: "PROCEDURE",
    id: "2",
    show: true,
  },
  {
    componentType: "53",
    label: "SparkSql开发",
    taskType: "SPARK",
    id: "4",
    show: false,
  },
]);

const visibleRadioOptions = computed(() =>
  radioOptions.value.filter((option) => option.show)
);

const form = ref({
  id: "",
  code: "", // 组件的 code
  taskType: "",
  name: "", // 名字
  version: "", // 版本号
  componentType: "",
  catCode: "", // 默认值
  personCharge: "", // 默认值
  contactNumber: "",
  crontab: "",
  releaseState: "",
  description: "",
  taskParams: {
    sqlType: "0",
    type: "",
    sql: "select * from DPP_ETL_TASK",
    typaCode: "", // 默认值
    datasources: {
      datasourceId: "", // 默认值
      datasourceType: "",
      dbname: "",
    },
  },
});
let loading = ref(false);
let createTypeList = ref([]);
const handleDatasourceChange = async (value) => {
  const selectedDatasource = createTypeList.value.find(
    (item) => item.id == value
  );
  let { datasourceType, datasourceConfig, ip, port, id } = selectedDatasource;
  let code = JSON.parse(datasourceConfig);
  form.value.taskParams.datasources = {
    datasourceType,
    datasourceConfig,
    ip,
    port,
    dbname: code.dbname,
    datasource_id: id,
    datasourceId: id,
  };
};

// 修改
const open = ref(false);
let row = ref({});
const openDialog = (obj) => {
  row.value = obj;
  open.value = true;
};
const handletaskConfig = (obj) => {
  if (row.value?.index) {
    // 如果找到匹配项，则更新
    form.value.taskParams.localParams[row.value.index - 1] = {
      ...form.value.taskParams.localParams[row.value.index - 1],
      ...obj,
    };
  } else {
    // 如果找不到，则追加到数组末尾
    form.value.taskParams.localParams.push({ ...obj });
    console.log("🚀 ~ handletaskConfig ~ obj111:", obj);
  }
};

const handleDelete = (row) => {
  form.value.taskParams.localParams = form.value.taskParams.localParams.filter(
    (item) => item.prop !== row.prop
  );
};
/** 查询数据开发任务列表 */
function getDaDatasource(flag) {
  loading.value = true;
  listDaDatasourceNoKafkaByProjectCode({
    projectCode: userStore.projectCode,
    projectId: userStore.projectId,
    datasourceType: form.value.taskParams.typaCode,
  }).then((response) => {
    createTypeList.value = response.data;
    if (flag) {
      form.value.taskParams.datasources.datasourceId = "";
    }

    console.log("🚀 ~ getDaDatasourceList ~ response:", response);
    loading.value = false;
  });
}
watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      form.value = props.data;
      getDaDatasource();
      form.value.taskConfig.personCharge =
        Number(form.value.taskConfig.personCharge) || "";
    } else {
      proxy.resetForm("daDiscoveryTaskRef");
    }
  }
);

// 计算属性处理 v-model
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

// 关闭对话框的方法
const closeDialog = () => {
  emit("update:visible", false);
};

let openCron = ref(false);
const expression = ref("");
/** 调度周期按钮操作 */
function handleShowCron() {
  expression.value = form.value.taskConfig.crontab;
  openCron.value = true;
}
/** 确定后回传值 */
function crontabFill(value) {
  form.value.taskConfig.crontab = value;
}
let daDiscoveryTaskRef = ref();
// 保存数据的方法
const saveData = async () => {
  try {
    const valid = await daDiscoveryTaskRef.value.validate();

    if (valid) {
      if (!form.value.code) {
        const response = await getNodeUniqueKey({
          projectCode: userStore.projectCode,
          projectId: userStore.projectId,
        });
        if (response && response.data) {
          form.value.code = response.data; // Set unique code
        }
      }

      let obj;

      if (form.value?.taskParams?.typaCode == "SparkSql") {
        obj = radioOptions.value?.find((option) => option.id == 4);
      } else {
        obj = radioOptions.value?.find(
          (option) => option.id == form.value?.taskParams?.sqlType
        );
      }

      const formData = JSON.parse(JSON.stringify(form.value));
      const { componentType, taskType } = obj;
      formData.taskParams.type = formData.taskParams.datasources.datasourceType;
      formData.componentType = componentType;
      formData.taskType = taskType;

      emit("confirm", formData);
      console.log("🚀 ~ saveData ~ formData:", formData);

      emit("update:visible", false);
    } else {
      console.log("表单校验失败");
    }
  } catch (error) {
    console.error("保存数据时出错:", error);
  }
};

const handleContactChange = (selectedValue) => {
  const selectedUser = props.userList.find(
    (user) => user.userId == selectedValue
  );
  console.log("🚀 ~ handleContactChange ~ selectedUser:", selectedUser);
  form.value.taskConfig.contactNumber = selectedUser?.phonenumber || "";
};
// 定义表单验证规则额
</script>
<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
