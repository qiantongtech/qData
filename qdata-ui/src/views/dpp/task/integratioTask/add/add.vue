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
  <el-dialog
    v-model="visibleDialog"
    draggable
    class="dialog"
    :title="title"
    destroy-on-close
    width="60%"
    :append-to="$refs['app-container']"
  >
    <el-form
      ref="daDiscoveryTaskRef"
      :model="form"
      :rules="
        title == td('dpp.integratioTask.taskDetail', 'Task Details') ? {} : rules
      "
      label-width="146px"
      @submit.prevent
      :disabled="title == td('dpp.integratioTask.taskDetail', 'Task Details')"
     :label-position="labelPosition">
      <div class="h2-title">
        {{ td("dpp.integratioTask.basicInfo", "Basic Info") }}
      </div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.taskName', 'Task Name')"
            prop="name"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
              v-model="form.name"
              :placeholder="
                td('dpp.integratioTask.inputTaskName', 'Please enter task name')
              "
            />
            <div class="form-readonly" v-else>{{ form.name }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.taskCategory', 'Task Category')"
            prop="catCode"
           :label-position="labelPosition">
            <el-tree-select
              :default-expanded-keys="defaultExpandedCats"
              filterable
              v-model="form.catCode"
              :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }"
              value-key="id"
              :placeholder="
                td('dpp.integratioTask.selectTaskCategory', 'Please select task category')
              "
              check-strictly
              @node-click="handleNodeClick"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.executionStrategy', 'Execution Strategy')"
            prop="executionType"
           :label-position="labelPosition">
            <el-select
              v-if="title != td('dpp.integratioTask.taskDetail')"
              class="el-form-input-width"
              v-model="form.executionType"
              :placeholder="
                td(
                  'dpp.integratioTask.selectExecutionStrategy',
                  'Please select execution strategy'
                )
              "
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
          <el-form-item
            :label="td('dpp.integratioTask.scheduleCycle', 'Schedule Cycle')"
            prop="crontab"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail')"
              v-model="form.crontab"
              :placeholder="
                td('dpp.integratioTask.selectScheduleCycle', 'Please select schedule cycle')
              "
            >
              <template #append>
                <el-button
                  type="primary"
                  @click="handleShowCron"
                  style="background-color: #2666fb; color: #fff"
                >
                  {{ td("dpp.integratioTask.configure", "Configure") }}
                  <i class="el-icon-time el-icon--right"></i>
                </el-button>
              </template>
            </el-input>
            <div class="form-readonly" v-else>{{ form.crontab }}</div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
              :label="td('dpp.integratioTask.scheduler', 'Scheduler')"
              prop="scheduler"
              :label-position="labelPosition"
          >
            <el-radio-group
                v-if="title != td('dpp.integratioTask.taskDetail')"
                class="el-form-input-width"
                v-model="form.scheduler"
                @change="handleSchedulerChange"
                style="width: 100%"
            >
              <el-radio
                  v-for="(item, index) in scheduler_type"
                  :key="index"
                  :value="item.value"
                  :label="item.label"
              >
                {{ item.label }}
              </el-radio>
            </el-radio-group>
            <div class="form-readonly" v-else>{{ form.scheduler || "-" }}</div>
            <p style="display: flex;align-items: center;line-height: 1;font-size: 12px;color: #888; margin-top: 10px;">
              {{ schedulerGuide.description }}
            </p>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.responsiblePerson', 'Responsible Person')"
            prop="personCharge"
           :label-position="labelPosition">
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
              :placeholder="
                td('dpp.integratioTask.selectResponsiblePerson', 'Please select responsible person')
              "
              check-strictly
              @change="handleContactChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.contactNumber', 'Contact Phone')"
            prop="contactNumber"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail')"
              v-model="form.contactNumber"
              :placeholder="
                td('dpp.integratioTask.inputContactNumber', 'Please enter contact phone')
              "
              disabled
            >
            </el-input>
            <div class="form-readonly" v-else>{{ form.contactNumber }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            :label="td('common.texts.description')"
            prop="description"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail')"
              v-model="form.description"
              type="textarea"
              :placeholder="td('common.form.descriptionPlaceholder')"
            />
            <div class="form-readonly" v-else>
              {{ form.description || "-" }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.taskStatus', 'Task Status')"
            prop="releaseState"
           :label-position="labelPosition">
            <el-radio-group
              v-if="title != td('dpp.integratioTask.taskDetail')"
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
      <div class="h2-title">
        {{ td("dpp.integratioTask.attributeInfo", "Attribute Info") }}
      </div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.taskPriority', 'Task Priority')"
            prop="taskPriority"
           :label-position="labelPosition">
            <el-select
              v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
              clearable
              v-model="form.taskPriority"
              :placeholder="
                td('dpp.integratioTask.selectTaskPriority', 'Please select task priority')
              "
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
          <el-form-item
            :label="td('dpp.integratioTask.workerGroup', 'Worker Group')"
            prop="workerGroup"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
              v-model="form.workerGroup"
              :placeholder="
                td('dpp.integratioTask.inputWorkerGroup', 'Please enter worker group')
              "
              disabled
            />
            <div class="form-readonly" v-else>
              {{ form.workerGroup ?? "-" }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.failRetryTimes', 'Retry Count on Failure')"
            prop="failRetryTimes"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
              type="number"
              v-model="form.failRetryTimes"
              :placeholder="
                td(
                  'dpp.integratioTask.inputFailRetryTimes',
                  'Please enter retry count on failure'
                )
              "
            >
              <template #append>{{
                td("dpp.integratioTask.times", "times")
              }}</template>
            </el-input>
            <div class="form-readonly" v-else>
              {{ form.failRetryTimes || "-" }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.failRetryInterval', 'Retry Interval')"
            prop="failRetryInterval"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
              type="number"
              v-model="form.failRetryInterval"
              :placeholder="
                td(
                  'dpp.integratioTask.inputFailRetryInterval',
                  'Please enter retry interval'
                )
              "
            >
              <template #append>{{
                td("dpp.integratioTask.minutes", "min")
              }}</template>
            </el-input>
            <div class="form-readonly" v-else>
              {{ form.failRetryInterval || "-" }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.delayExecutionTime', 'Delay Execution Time')"
            prop="delayTime"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
              type="number"
              v-model="form.delayTime"
              :placeholder="
                td('dpp.integratioTask.inputDelayTime', 'Please enter delay execution time')
              "
            >
              <template #append>分</template>
            </el-input>
            <div class="form-readonly" v-else>{{ form.delayTime || "-" }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.executionEngine', 'Execution Engine')"
            prop="taskType"
           :label-position="labelPosition">
            <el-radio-group
              v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
              v-model="form.taskType"
              class="el-form-input-width"
              :disabled="props.data.id"
              @change="handleExecutionEngineChange"
            >
              <el-radio
                v-for="(item, index) in schedulerTypeList"
                :key="index"
                :value="item.value"
                :label="item.label"
              >
                {{ item.label }}
              </el-radio>
            </el-radio-group>
            <div class="form-readonly" v-else>{{ form.taskType || "-" }}</div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <template v-if="form.taskType == 'SPARK'">
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.integratioTask.driverCores', 'Driver Cores')"
              prop="driverCores"
             :label-position="labelPosition">
              <el-input-number
                v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
                :placeholder="
                  td(
                    'dpp.integratioTask.inputDriverCores',
                    'Please enter driver cores'
                  )
                "
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
            <el-form-item
              :label="td('dpp.integratioTask.driverMemory', 'Driver Memory')"
              prop="driverMemory"
             :label-position="labelPosition">
              <el-input
                v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
                v-model="form.driverMemory"
                :placeholder="
                  td(
                    'dpp.integratioTask.inputDriverMemory',
                    'Please enter driver memory'
                  )
                "
                style="width: 100%"
              >
              </el-input>
              <div class="form-readonly" v-else>
                {{ form.driverMemory || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.integratioTask.executorCount', 'Executor Count')"
              prop="numExecutors"
             :label-position="labelPosition">
              <el-input-number
                v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
                :placeholder="
                  td(
                    'dpp.integratioTask.inputExecutorCount',
                    'Please enter executor count'
                  )
                "
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
            <el-form-item
              :label="td('dpp.integratioTask.executorMemory', 'Executor Memory')"
              prop="executorMemory"
             :label-position="labelPosition">
              <el-input
                v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
                v-model="form.executorMemory"
                :placeholder="
                  td(
                    'dpp.integratioTask.inputExecutorMemory',
                    'Please enter executor memory'
                  )
                "
                style="width: 100%"
              >
              </el-input>
              <div class="form-readonly" v-else>
                {{ form.executorMemory || "-" }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              :label="td('dpp.integratioTask.executorCores', 'Executor Cores')"
              prop="executorCores"
             :label-position="labelPosition">
              <el-input-number
                v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
                :placeholder="
                  td(
                    'dpp.integratioTask.inputExecutorCores',
                    'Please enter executor cores'
                  )
                "
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
            <el-form-item
              :label="td('dpp.integratioTask.yarnQueue', 'Yarn Queue')"
              prop="yarnQueue"
             :label-position="labelPosition">
              <el-input
                v-if="title != td('dpp.integratioTask.taskDetail', 'Task Details')"
                v-model="form.yarnQueue"
                :placeholder="
                  td(
                    'dpp.integratioTask.inputYarnQueue',
                    'Please enter Yarn queue (optional)'
                  )
                "
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
          <el-form-item :label="t('common.texts.remark')" prop="remark" :label-position="labelPosition">
            <el-input v-model="form.remark" type="textarea" :placeholder="td('common.form.remarkPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row> -->
    </el-form>
    <template #footer>
      <div style="text-align: right">
        <template v-if="info">
          <el-button @click="closeDialog">{{
            td("common.button.close", "Close")
          }}</el-button>
          <el-button
            type="primary"
            v-if="!route.query.info"
            @click="saveClose"
            >{{ td("common.button.save", "Save") }}</el-button
          >
        </template>
        <template v-else>
          <el-button @click="saveClose">{{
            td("dpp.integratioTask.onlySave", "Save Only")
          }}</el-button>
          <el-button type="primary" @click="saveData">{{
            td("dpp.integratioTask.saveAndConfigFlow", "Save and Configure Flow")
          }}</el-button>
        </template>
      </div>
    </template>
  </el-dialog>

  <el-dialog
    :title="td('dpp.integratioTask.cronGenerator', 'Cron Expression Generator')"
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
import { checkApi } from "@/api/ds/api/api.js";
import useDefaultLang from "@/composables/useDefaultLang";
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import Crontab from "@/components/Crontab/index.vue";
const { proxy } = getCurrentInstance();
const {
  dpp_etl_task_execution_type,
  dpp_etl_task_status,
  dpp_etl_task_priority,
  scheduler_type,
  actuator_type
} = proxy.useDict(
  "dpp_etl_task_execution_type",
  "dpp_etl_task_status",
  "dpp_etl_task_priority",
    "scheduler_type",
    "actuator_type"
);
import { useRoute, useRouter } from "vue-router";

const { td } = useDefaultLang();
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

const schedulerTypeList = ref([]);
const updateSchedulerTypeList = () => {
  schedulerTypeList.value = actuator_type.value.filter((item) =>
    form.value.scheduler === "QUARTZ"
      ? item.value === "DATAX"
      : item.value !== "DATAX"
  );
};

// Define form validation rules
const rules = {
  name: [
    {
      required: true,
      message: td("dpp.integratioTask.inputTaskName", "Please enter task name"),
      trigger: "change",
    },
  ],
  catCode: [
    {
      required: true,
      message: td("dpp.integratioTask.selectTaskCategory", "Please select task category"),
      trigger: "change",
    },
  ],
  executionType: [
    {
      required: true,
      message: td(
        "dpp.integratioTask.selectExecutionStrategy",
        "Please select execution strategy"
      ),
      trigger: "change",
    },
  ],
  scheduler: [
    {
      required: true,
      message: td("dpp.integratioTask.selectScheduler", "Scheduler is required"),
      trigger: "change",
    },
  ],
  crontab: [
    {
      required: true,
      message: td("dpp.integratioTask.selectScheduleCycle", "Please select schedule cycle"),
      trigger: "change",
    },
  ],

  // releaseState: [{ required: true, message: "Task status cannot be empty", trigger: "change" }],
  taskType: [
    {
      required: true,
      message: td("dpp.integratioTask.executionEngine", "Execution Engine"),
      trigger: "change",
    },
  ],
  personCharge: [
    {
      required: true,
      message: td(
        "dpp.integratioTask.selectResponsiblePerson",
        "Please select responsible person"
      ),
      trigger: "change",
    },
  ],
};
const form = ref({
  catId: "",
  name: "",
  catCode: "", // Can be initialized to empty or set to default value
  executionType: "PARALLEL", // Initialized to empty or default value
  scheduler: "QUARTZ",
  actuator: "DATAX",
  crontab: "",
  releaseState: "0",
  description: "",
  contactNumber: "",
  personCharge: "",
  // Newly added
  taskPriority: "",
  workerGroup: "default",
  failRetryTimes: "",
  failRetryInterval: "",
  delayTime: "",
  taskType: "DATAX",
  // Fink configuration
  jobManagerMemory: "1G",
  taskManagerMemory: "2G",
  slot: 1,
  taskManager: 2,
  parallelism: 1,
  // Spark configuration
  driverCores: 1,
  driverMemory: "512m",
  numExecutors: 1,
  executorMemory: "512m",
  executorCores: 1,
  yarnQueue: "",
});

const schedulerGuide = computed(() => {
  if (form.value.scheduler === "DOLPHINSCHEDULER") {
    return {
      description: td(
        "dpp.integratioTask.dolphinSchedulerGuideDescription",
        "please ensure that the DolphinScheduler service is running"
      ),
    };
  }

  return {
    description: td(
      "dpp.integratioTask.quartzGuideDescription",
      "The task is executed by the built-in components of the system"
    ),
  };
});

const reset = () => {
  proxy.resetForm("daDiscoveryTaskRef");
  form.value = {
    name: "",
    catId: "",
    scheduler: "QUARTZ",
    actuator: "DATAX",
    catCode: "", // Can be initialized to empty or set to default value
    executionType: "PARALLEL", // Initialized to empty or default value
    crontab: "",
    releaseState: "0",
    description: "",
    contactNumber: "",
    personCharge: "",
    // Newly added
    taskPriority: "",
    workerGroup: "default",
    failRetryTimes: "",
    failRetryInterval: "",
    delayTime: "",
    taskType: "DATAX",
    // Fink configuration
    jobManagerMemory: "1G",
    taskManagerMemory: "2G",
    slot: 1,
    taskManager: 2,
    parallelism: 1,
    // Spark configuration
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
        // Preserve DS and Spark as defaults for legacy tasks without scheduler or actuator values.
        form.value.scheduler = form.value.scheduler || "DOLPHINSCHEDULER";
        form.value.taskType = form.value.actuator == "DATAX" ? "DATAX" : form.value.taskType || "SPARK";
        updateSchedulerTypeList();
        syncActuatorByEngine();
        enforceQuartzDataX();
        form.value.personCharge = Number(form.value.personCharge) || "";
        form.value.crontab = props?.data.taskConfig?.crontab;
      } else {
        form.value.catCode = props?.catCode || "";
        updateSchedulerTypeList();
        syncActuatorByEngine();
      }
    } else {
      reset();
    }
  }
);
const handleNodeClick = (val) => {
  console.log("Task category changed; current value:", val);
  form.value.catId = val.id;
};

/**
 * DolphinScheduler
 * @returns {Promise<void>}
 */
const handleSchedulerChange = async () => {
  updateSchedulerTypeList();
  if (form.value.scheduler == "QUARTZ") {
    form.value.taskType = "DATAX";
  } else {
    form.value.taskType = "SPARK";
  }
  syncActuatorByEngine();
};

watch(actuator_type, updateSchedulerTypeList, { immediate: true });

const handleExecutionEngineChange = (value) => {
  if (form.value.scheduler != "QUARTZ" && value == "DATAX") {
    proxy.$modal.msgWarning(td("dpp.integratioTask.unsupportedEngineSwitch", "Currently, switching is not supported！"));
    form.value.taskType = "SPARK";
  }
  if (form.value.scheduler == "QUARTZ" && value != "DATAX") {
    proxy.$modal.msgWarning(td("dpp.integratioTask.unsupportedEngineSwitch", "Currently, switching is not supported！"));
    form.value.taskType = "DATAX";
  }
  syncActuatorByEngine();
};

const enforceQuartzDataX = () => {
  if (form.value.scheduler == "QUARTZ") {
    form.value.taskType = "DATAX";
  }
};

const syncActuatorByEngine = () => {
  form.value.actuator = form.value.taskType == "DATAX" ? "DATAX" : "SPARK";
};
// Computed property handling v-model
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
const saveClose = async () => {
  if (form.value.scheduler === 'DOLPHINSCHEDULER' && !await checkDSUpStart()) {
    return;
  }
  daDiscoveryTaskRef.value.validate((valid) => {
    if (valid) {
      syncActuatorByEngine();
      emit("save", form.value);
      emit("update:visible", false);
    } else {
      console.log("Form validation failed");
    }
  });
};
// How to save data
const saveData = async () => {
  if (form.value.scheduler === 'DOLPHINSCHEDULER' && !await checkDSUpStart()) {
    return;
  }
  daDiscoveryTaskRef.value.validate((valid) => {
    if (valid) {
      syncActuatorByEngine();
      emit("confirm", form.value);
      emit("update:visible", false);
    } else {
      console.log("Form validation failed");
    }
  });
};

/**
 * check dolphinscheduler api
 * @returns {Promise<AxiosResponse<any>>}
 */
const checkDSUpStart = async () => {
  const resp = await checkApi();
  if (!resp.data) {
    proxy.$modal.msgWarning(td("dpp.integratioTask.upDs", "Please start the DolphinScheduler scheduler！"));
  }
  return resp.data;
}

let openCron = ref(false);
const expression = ref("");
/** Scheduling cycle button operation */
function handleShowCron() {
  expression.value = form.value.crontab;
  openCron.value = true;
}
/** Return value after confirmation */
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
