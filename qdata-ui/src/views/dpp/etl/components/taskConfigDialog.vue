<template>
  <el-dialog v-model="visibleDialog" draggable class="dialog" :title="title" destroy-on-close :append-to="$refs['app-container']">
    <el-form ref="daDiscoveryTaskRef" :model="form" :rules="rules" label-width="146px" @submit.prevent>
      <el-divider content-position="left">
        <span class="blue-text">基本信息</span>
      </el-divider>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入任务名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务类目" prop="catCode">
            <el-tree-select v-model="form.catCode" :data="deptOptions" :props="{ value: 'code', label: 'name', children: 'children' }" value-key="ID" placeholder="请选择任务类目" check-strictly />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任人" prop="personCharge">
            <el-tree-select
              v-model="form.personCharge"
              :data="userList"
              :props="{
                value: 'userId',
                label: 'nickName',
                children: 'children',
              }"
              value-key="ID"
              placeholder="请选择责任人"
              check-strictly
              @change="handleContactChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactNumber">
            <el-input v-model="form.contactNumber" placeholder="请输入联系电话" disabled> </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="执行策略" prop="executionType">
            <el-select class="el-form-input-width" v-model="form.executionType" placeholder="请选择执行策略" style="width: 100%">
              <el-option v-for="dict in dpp_etl_task_execution_type" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="调度周期" prop="crontab">
            <el-input v-model="form.crontab" placeholder="请选择调度周期">
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
          <el-form-item label="任务状态" prop="releaseState">
            <el-radio-group v-model="form.releaseState" class="el-form-input-width">
              <el-radio v-for="dict in dpp_etl_task_status" :key="dict.value" :label="dict.value" :disabled="dict.value == 1">
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">
        <span class="blue-text">属性信息</span>
      </el-divider>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务优先级" prop="taskPriority">
            <el-select clearable v-model="form.taskPriority" placeholder="请选择任务优先级">
              <el-option v-for="(item, index) in dpp_etl_task_priority" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Worker分组" prop="workerGroup">
            <el-input v-model="form.workerGroup" placeholder="请输入Worker分组" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="失败重试次数" prop="failRetryTimes">
            <el-input type="number" v-model="form.failRetryTimes" placeholder="请输入失败重试次数">
              <template #append>次</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="失败重试间隔" prop="failRetryInterval">
            <el-input type="number" v-model="form.failRetryInterval" placeholder="请输入失败重试间隔">
              <template #append>分</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="延迟执行时间" prop="delayTime">
            <el-input type="number" v-model="form.delayTime" placeholder="请输入延迟执行时间">
              <template #append>分</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行引擎" prop="taskType">
            <el-radio-group v-model="form.taskType" class="el-form-input-width" :disabled="props.data.id">
              <el-radio label="FLINK"> FLINK </el-radio>
              <el-radio label="SPARK"> SPARK </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <template v-if="form.taskType == 'FLINK'">
          <el-col :span="12">
            <el-form-item label="JobManager内存数" prop="jobManagerMemory">
              <el-input v-model="form.jobManagerMemory" placeholder="请输入JobManager内存数"> </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="TaskManager内存数" prop="taskManagerMemory">
              <el-input v-model="form.taskManagerMemory" placeholder="请输入TaskManager内存数"> </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Slot数量" prop="slot">
              <el-input-number placeholder="请输入Slot数量" v-model="form.slot" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="TaskManager数量" prop="taskManager">
              <el-input v-model="form.taskManager" placeholder="请输入TaskManager数量"> </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="并行度" prop="parallelism">
              <el-input-number placeholder="请输入并行度" v-model="form.parallelism" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Yarn队列" prop="yarnQueue">
              <el-input v-model="form.yarnQueue" placeholder="请输入Yarn队列(选填)"> </el-input>
            </el-form-item>
          </el-col>
        </template>
        <template v-if="form.taskType == 'SPARK'">
          <el-col :span="12">
            <el-form-item label="Driver核心数" prop="driverCores">
              <el-input-number placeholder="请输入Driver核心数" v-model="form.driverCores" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Driver内存数" prop="driverMemory">
              <el-input v-model="form.driverMemory" placeholder="请输入Driver内存数"> </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Executor数量" prop="numExecutors">
              <el-input-number placeholder="请输入Executor数量" v-model="form.numExecutors" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Executor内存数" prop="executorMemory">
              <el-input v-model="form.executorMemory" placeholder="请输入Executor内存数"> </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Executor核心数" prop="executorCores">
              <el-input-number placeholder="请输入Executor核心数" v-model="form.executorCores" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Yarn队列" prop="yarnQueue">
              <el-input v-model="form.yarnQueue" placeholder="请输入Yarn队列(选填)"> </el-input>
            </el-form-item>
          </el-col>
        </template>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="任务描述" prop="description">
            <el-input v-model="form.description" type="textarea" placeholder="请输入任务描述" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <template v-if="info">
          <el-button @click="closeDialog">关闭</el-button>
          <el-button type="primary" @click="saveClose">保存</el-button>
        </template>
        <template v-else>
          <el-button @click="saveClose">仅保存</el-button>
          <el-button type="primary" @click="saveData">保存并配置流程</el-button>
        </template>
      </div>
    </template>
  </el-dialog>

  <el-dialog title="Cron表达式生成器" v-model="openCron" :append-to="$refs['app-container']" destroy-on-close>
    <!--    <crontab ref="crontabRef" @hide="openCron = false" @fill="crontabFill" :expression="expression" :Crontab="false">-->
    <crontab ref="crontabRef" @hide="openCron = false" @fill="crontabFill" :expression="expression"> </crontab>
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import Crontab from "@/components/Crontab/index.vue";
const { proxy } = getCurrentInstance();
const { dpp_etl_task_execution_type, dpp_etl_task_status, dpp_etl_task_priority } = proxy.useDict("dpp_etl_task_execution_type", "dpp_etl_task_status", "dpp_etl_task_priority");

const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  data: { type: Object, default: () => ({}) },
  deptOptions: { type: Object, default: () => ({}) },
  userList: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
});

const emit = defineEmits(["update:visible", "confirm", "save"]);

// 定义表单验证规则
const rules = {
  name: [{ required: true, message: "任务名称不能为空", trigger: "change" }],
  catCode: [{ required: true, message: "任务类目不能为空", trigger: "change" }],
  executionType: [{ required: true, message: "执行策略不能为空", trigger: "change" }],
  crontab: [{ required: true, message: "调度周期不能为空", trigger: "change" }],
  releaseState: [{ required: true, message: "任务状态不能为空", trigger: "change" }],
  engine: [{ required: true, message: "执行引擎不能为空", trigger: "change" }],
  personCharge: [{ required: true, message: "责任人不能为空", trigger: "change" }],
};
const form = ref({
  name: "",
  catCode: "", // 可以初始化为空，也可以设为默认值
  executionType: "PARALLEL", // 初始化为空或默认值
  crontab: "",
  releaseState: "0",
  description: "",
  contactNumber: "",
  personCharge: "",
  // 新添加
  taskPriority: "",
  workerGroup: "default",
  failRetryTimes: "",
  failRetryInterval: "",
  delayTime: "",
  taskType: "FLINK",
  // Fink配置
  jobManagerMemory: "1G",
  taskManagerMemory: "2G",
  slot: 1,
  taskManager: 2,
  parallelism: 1,
  // Spark配置
  driverCores: 1,
  driverMemory: "2G",
  numExecutors: 1,
  executorMemory: "4G",
  executorCores: 2,
  yarnQueue: "",
});

const reset = () => {
  proxy.resetForm("daDiscoveryTaskRef");
  form.value = {
    name: "",
    catCode: "", // 可以初始化为空，也可以设为默认值
    executionType: "PARALLEL", // 初始化为空或默认值
    crontab: "",
    releaseState: "0",
    description: "",
    contactNumber: "",
    personCharge: "",
    // 新添加
    taskPriority: "",
    workerGroup: "default",
    failRetryTimes: "",
    failRetryInterval: "",
    delayTime: "",
    taskType: "FLINK",
    // Fink配置
    jobManagerMemory: "1G",
    taskManagerMemory: "2G",
    slot: 1,
    taskManager: 2,
    parallelism: 1,
    // Spark配置
    driverCores: 1,
    driverMemory: "2G",
    numExecutors: 1,
    executorMemory: "4G",
    executorCores: 2,
    yarnQueue: "",
  };
};
watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      if (props.data.id) {
        let data = JSON.parse(JSON.stringify(props.data.taskConfig));
        let draftJson = JSON.parse(data.draftJson);
        form.value = { ...data, ...draftJson };
        form.value.personCharge = Number(form.value.personCharge) || "";
      }
      console.log("🚀 ~ form.value:", form.value);
    } else {
      reset();
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
let daDiscoveryTaskRef = ref();
const closeDialog = () => {
  emit("update:visible", false);
};
const saveClose = () => {
  daDiscoveryTaskRef.value.validate((valid) => {
    if (valid) {
      emit("save", form.value);
      emit("update:visible", false);
    } else {
      console.log("表单校验失败");
    }
  });
};
// 保存数据的方法
const saveData = () => {
  daDiscoveryTaskRef.value.validate((valid) => {
    if (valid) {
      emit("confirm", form.value);
      emit("update:visible", false);
    } else {
      console.log("表单校验失败");
    }
  });
};

let openCron = ref(false);
const expression = ref("");
/** 调度周期按钮操作 */
function handleShowCron() {
  expression.value = form.value.crontab;
  openCron.value = true;
}
/** 确定后回传值 */
function crontabFill(value) {
  form.value.crontab = value;
}
const handleContactChange = (selectedValue) => {
  const selectedUser = props.userList.find((user) => user.userId == selectedValue);
  console.log("🚀 ~ handleContactChange ~ selectedUser:", selectedUser);
  form.value.contactNumber = selectedUser?.phonenumber || "";
};
</script>
<style lang="scss" scoped>
.blue-text {
  color: var(--el-color-primary);
}
</style>
