<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <el-dialog
    v-model="visibleDialog"
    draggable
    class="dialog"
    :title="title"
    destroy-on-close
    :append-to="$refs['app-container']"
  >
    <el-form
      ref="daDiscoveryTaskRef"
      :model="form"
      :rules="title == '任务详情' ? {} : rules"
      label-width="146px"
      @submit.prevent
      :disabled="title == '任务详情'"
    >
      <div class="h2-title">基本信息</div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务名称" prop="name">
            <el-input
              v-if="title != '任务详情'"
              v-model="form.name"
              placeholder="请输入任务名称"
            />
            <div class="form-readonly" v-else>{{ form.name }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务类目" prop="catCode">
            <el-tree-select
              :default-expanded-keys="defaultExpandedCats"
              filterable
              v-model="form.catCode"
              :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }"
              value-key="id"
              placeholder="请选择任务类目"
              check-strictly
              @node-click="handleNodeClick"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="执行策略" prop="executionType">
            <el-select
              v-if="title != '任务详情'"
              class="el-form-input-width"
              v-model="form.executionType"
              placeholder="请选择执行策略"
              style="width: 100%"
            >
              <el-option
                v-for="dict in dpp_etl_task_execution_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
            <div class="form-readonly" v-else>
              {{
                dpp_etl_task_execution_type.find(
                  (item) => item.value == form.executionType
                )?.label || "-"
              }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="调度周期" prop="crontab">
            <el-input
              v-if="title != '任务详情'"
              v-model="form.crontab"
              placeholder="请选择调度周期"
            >
              <template #append>
                <el-button
                  type="primary"
                  @click="handleShowCron"
                  style="background-color: #2666fb; color: #fff"
                >
                  配置
                  <i class="el-icon-time el-icon--right"></i>
                </el-button>
              </template>
            </el-input>
            <div class="form-readonly" v-else>{{ form.crontab }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任人" prop="personCharge">
            <el-tree-select
              filterable
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
            <el-input
              v-if="title != '任务详情'"
              v-model="form.contactNumber"
              placeholder="请输入联系电话"
              disabled
            >
            </el-input>
            <div class="form-readonly" v-else>{{ form.contactNumber }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="描述" prop="description">
            <el-input
              v-if="title != '任务详情'"
              v-model="form.description"
              type="textarea"
              placeholder="请输入描述"
            />
            <div class="form-readonly" v-else>
              {{ form.description || "-" }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务状态" prop="releaseState">
            <el-radio-group
              v-if="title != '任务详情'"
              v-model="form.releaseState"
              class="el-form-input-width"
            >
              <el-radio
                v-for="dict in dpp_etl_task_status"
                :key="dict.value"
                :label="dict.value"
                :disabled="dict.value == 1"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
            <div class="form-readonly" v-else>
              {{
                dpp_etl_task_status.find(
                  (item) => item.value == form.releaseState
                )?.label || "-"
              }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <div class="h2-title">属性信息</div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务优先级" prop="taskPriority">
            <el-select
              v-if="title != '任务详情'"
              clearable
              v-model="form.taskPriority"
              placeholder="请选择任务优先级"
            >
              <el-option
                v-for="(item, index) in dpp_etl_task_priority"
                :key="index"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <div class="form-readonly" v-else>
              {{
                dpp_etl_task_priority.find(
                  (item) => item.value == form.taskPriority
                )?.label || "-"
              }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Worker分组" prop="workerGroup">
            <el-input
              v-if="title != '任务详情'"
              v-model="form.workerGroup"
              placeholder="请输入Worker分组"
              disabled
            />
            <div class="form-readonly" v-else>
              {{ form.workerGroup ?? "-" }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="失败重试次数" prop="failRetryTimes">
            <el-input
              v-if="title != '任务详情'"
              type="number"
              v-model="form.failRetryTimes"
              placeholder="请输入失败重试次数"
            >
              <template #append>次</template>
            </el-input>
            <div class="form-readonly" v-else>
              {{ form.failRetryTimes || "-" }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="失败重试间隔" prop="failRetryInterval">
            <el-input
              v-if="title != '任务详情'"
              type="number"
              v-model="form.failRetryInterval"
              placeholder="请输入失败重试间隔"
            >
              <template #append>分</template>
            </el-input>
            <div class="form-readonly" v-else>
              {{ form.failRetryInterval || "-" }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="延迟执行时间" prop="delayTime">
            <el-input
              v-if="title != '任务详情'"
              type="number"
              v-model="form.delayTime"
              placeholder="请输入延迟执行时间"
            >
              <template #append>分</template>
            </el-input>
            <div class="form-readonly" v-else>{{ form.delayTime || "-" }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行引擎" prop="taskType">
            <el-radio-group
              v-if="title != '任务详情'"
              v-model="form.taskType"
              class="el-form-input-width"
              :disabled="props.data.id"
            >
              <el-radio label="SPARK"> SPARK </el-radio>
            </el-radio-group>
            <div class="form-readonly" v-else>{{ form.taskType || "-" }}</div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <template v-if="form.taskType == 'SPARK'">
          <el-col :span="12">
            <el-form-item label="Driver核心数" prop="driverCores">
              <el-input-number
                v-if="title != '任务详情'"
                placeholder="请输入Driver核心数"
                v-model="form.driverCores"
                controls-position="right"
                :min="0"
                style="width: 100%"
              />
              <div class="form-readonly" v-else>
                {{ form.driverCores || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Driver内存数" prop="driverMemory">
              <el-input
                v-if="title != '任务详情'"
                v-model="form.driverMemory"
                placeholder="请输入Driver内存数"
                style="width: 100%"
              >
              </el-input>
              <div class="form-readonly" v-else>
                {{ form.driverMemory || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Executor数量" prop="numExecutors">
              <el-input-number
                v-if="title != '任务详情'"
                placeholder="请输入Executor数量"
                v-model="form.numExecutors"
                controls-position="right"
                style="width: 100%"
                :min="0"
              />
              <div class="form-readonly" v-else>
                {{ form.numExecutors || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Executor内存数" prop="executorMemory">
              <el-input
                v-if="title != '任务详情'"
                v-model="form.executorMemory"
                placeholder="请输入Executor内存数"
                style="width: 100%"
              >
              </el-input>
              <div class="form-readonly" v-else>
                {{ form.executorMemory || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Executor核心数" prop="executorCores">
              <el-input-number
                v-if="title != '任务详情'"
                placeholder="请输入Executor核心数"
                v-model="form.executorCores"
                controls-position="right"
                style="width: 100%"
                :min="0"
              />
              <div class="form-readonly" v-else>
                {{ form.executorCores || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Yarn队列" prop="yarnQueue">
              <el-input
                v-if="title != '任务详情'"
                v-model="form.yarnQueue"
                placeholder="请输入Yarn队列(选填)"
              >
              </el-input>
              <div class="form-readonly" v-else>
                {{ form.yarnQueue || "-" }}
              </div>
            </el-form-item>
          </el-col>
        </template>
      </el-row>
      <!-- <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
          </el-form-item>
        </el-col>
      </el-row> -->
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <template v-if="info">
          <el-button @click="closeDialog">关闭</el-button>
          <el-button type="primary" v-if="!route.query.info" @click="saveClose"
            >保存</el-button
          >
        </template>
        <template v-else>
          <el-button @click="saveClose">仅保存</el-button>
          <el-button type="primary" @click="saveData">保存并配置流程</el-button>
        </template>
      </div>
    </template>
  </el-dialog>

  <el-dialog
    title="Cron表达式生成器"
    v-model="openCron"
    class="dialog"
    :append-to="$refs['app-container']"
    destroy-on-close
  >
    <!--    <crontab ref="crontabRef" @hide="openCron = false" @fill="crontabFill" :expression="expression" :Crontab="false">-->
    <crontab
      ref="crontabRef"
      @hide="openCron = false"
      @fill="crontabFill"
      :expression="expression"
    >
    </crontab>
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import Crontab from "@/components/Crontab/index.vue";
const { proxy } = getCurrentInstance();
const {
  dpp_etl_task_execution_type,
  dpp_etl_task_status,
  dpp_etl_task_priority,
} = proxy.useDict(
  "dpp_etl_task_execution_type",
  "dpp_etl_task_status",
  "dpp_etl_task_priority"
);
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  data: { type: Object, default: () => ({}) },
  deptOptions: { type: Object, default: () => ({}) },
  userList: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
  catCode: { type: String, default: "" },
});

const emit = defineEmits(["update:visible", "confirm", "save"]);

// 定义表单验证规则
const rules = {
  name: [{ required: true, message: "任务名称不能为空", trigger: "change" }],
  catCode: [{ required: true, message: "任务类目不能为空", trigger: "change" }],
  executionType: [
    { required: true, message: "执行策略不能为空", trigger: "change" },
  ],
  crontab: [{ required: true, message: "调度周期不能为空", trigger: "change" }],
  // releaseState: [{ required: true, message: "任务状态不能为空", trigger: "change" }],
  engine: [{ required: true, message: "执行引擎不能为空", trigger: "change" }],
  personCharge: [
    { required: true, message: "责任人不能为空", trigger: "change" },
  ],
};
const form = ref({
  catId: "",
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
  taskType: "SPARK",
  // Fink配置
  jobManagerMemory: "1G",
  taskManagerMemory: "2G",
  slot: 1,
  taskManager: 2,
  parallelism: 1,
  // Spark配置
  driverCores: 1,
  driverMemory: "512m",
  numExecutors: 1,
  executorMemory: "512m",
  executorCores: 1,
  yarnQueue: "",
});

const reset = () => {
  proxy.resetForm("daDiscoveryTaskRef");
  form.value = {
    name: "",
    catId: "",
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
    taskType: "SPARK",
    // Fink配置
    jobManagerMemory: "1G",
    taskManagerMemory: "2G",
    slot: 1,
    taskManager: 2,
    parallelism: 1,
    // Spark配置
    driverCores: 1,
    driverMemory: "512m",
    numExecutors: 1,
    executorMemory: "512m",
    executorCores: 1,
    yarnQueue: "",
  };
};
watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      if (props.data.id) {
        let data = JSON.parse(JSON.stringify(props.data.taskConfig));
        console.log("🚀 ~ props.data.taskConfig:", props.data.taskConfig);
        let draftJson = JSON.parse(data.draftJson);
        form.value = { ...data, ...draftJson };
        form.value.personCharge = Number(form.value.personCharge) || "";
        form.value.crontab = props?.data.taskConfig?.crontab;
      } else {
        form.value.catCode = props?.catCode || "";
      }
    } else {
      reset();
    }
  }
);
const handleNodeClick = (val) => {
  console.log("任务类目改变了，当前值：", val);
  form.value.catId = val.id;
};
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
      console.log("表单校验未通过");
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
      console.log("表单校验未通过");
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
  const selectedUser = props.userList.find(
    (user) => user.userId == selectedValue
  );
  console.log("🚀 ~ handleContactChange ~ selectedUser:", selectedUser);
  form.value.contactNumber = selectedUser?.phonenumber || "";
};
const defaultExpandedCats = computed(() => {
  return props.deptOptions.map((item) => item.id);
});
</script>
<style lang="scss" scoped>
.blue-text {
  color: var(--el-color-primary);
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
