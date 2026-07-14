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
        title == td('dpp.integratioTask.taskDetail', '任务详情') ? {} : rules
      "
      label-width="146px"
      @submit.prevent
      :disabled="title == td('dpp.integratioTask.taskDetail', '任务详情')"
     :label-position="labelPosition">
      <div class="h2-title">
        {{ td("dpp.integratioTask.basicInfo", "基本信息") }}
      </div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.taskName', '任务名称')"
            prop="name"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
              v-model="form.name"
              :placeholder="
                td('dpp.integratioTask.inputTaskName', '请输入任务名称')
              "
            />
            <div class="form-readonly" v-else>{{ form.name }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.taskCategory', '任务类目')"
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
                td('dpp.integratioTask.selectTaskCategory', '请选择任务类目')
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
            :label="td('dpp.integratioTask.executionStrategy', '执行策略')"
            prop="executionType"
           :label-position="labelPosition">
            <el-select
              v-if="title != td('dpp.integratioTask.taskDetail')"
              class="el-form-input-width"
              v-model="form.executionType"
              :placeholder="
                td(
                  'dpp.integratioTask.selectExecutionStrategy',
                  '请选择执行策略'
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
            :label="td('dpp.integratioTask.scheduleCycle', '调度周期')"
            prop="crontab"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail')"
              v-model="form.crontab"
              :placeholder="
                td('dpp.integratioTask.selectScheduleCycle', '请选择调度周期')
              "
            >
              <template #append>
                <el-button
                  type="primary"
                  @click="handleShowCron"
                  style="background-color: #2666fb; color: #fff"
                >
                  {{ td("dpp.integratioTask.configure", "配置") }}
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
              :label="td('dpp.integratioTask.scheduler', '调度器')"
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
            :label="td('dpp.integratioTask.responsiblePerson', '责任人')"
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
                td('dpp.integratioTask.selectResponsiblePerson', '请选择责任人')
              "
              check-strictly
              @change="handleContactChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.contactNumber', '联系电话')"
            prop="contactNumber"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail')"
              v-model="form.contactNumber"
              :placeholder="
                td('dpp.integratioTask.inputContactNumber', '请输入联系电话')
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
            :label="td('dpp.integratioTask.taskStatus', '任务状态')"
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
        {{ td("dpp.integratioTask.attributeInfo", "属性信息") }}
      </div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.taskPriority', '任务优先级')"
            prop="taskPriority"
           :label-position="labelPosition">
            <el-select
              v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
              clearable
              v-model="form.taskPriority"
              :placeholder="
                td('dpp.integratioTask.selectTaskPriority', '请选择任务优先级')
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
            :label="td('dpp.integratioTask.workerGroup', 'Worker分组')"
            prop="workerGroup"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
              v-model="form.workerGroup"
              :placeholder="
                td('dpp.integratioTask.inputWorkerGroup', '请输入Worker分组')
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
            :label="td('dpp.integratioTask.failRetryTimes', '失败重试次数')"
            prop="failRetryTimes"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
              type="number"
              v-model="form.failRetryTimes"
              :placeholder="
                td(
                  'dpp.integratioTask.inputFailRetryTimes',
                  '请输入失败重试次数'
                )
              "
            >
              <template #append>{{
                td("dpp.integratioTask.times", "次")
              }}</template>
            </el-input>
            <div class="form-readonly" v-else>
              {{ form.failRetryTimes || "-" }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.failRetryInterval', '失败重试间隔')"
            prop="failRetryInterval"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
              type="number"
              v-model="form.failRetryInterval"
              :placeholder="
                td(
                  'dpp.integratioTask.inputFailRetryInterval',
                  '请输入失败重试间隔'
                )
              "
            >
              <template #append>{{
                td("dpp.integratioTask.minutes", "分")
              }}</template>
            </el-input>
            <div class="form-readonly" v-else>
              {{ form.failRetryInterval || "-" }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.delayExecutionTime', '延迟执行时间')"
            prop="delayTime"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
              type="number"
              v-model="form.delayTime"
              :placeholder="
                td('dpp.integratioTask.inputDelayTime', '请输入延迟执行时间')
              "
            >
              <template #append>分</template>
            </el-input>
            <div class="form-readonly" v-else>{{ form.delayTime || "-" }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.integratioTask.executionEngine', '执行引擎')"
            prop="taskType"
           :label-position="labelPosition">
            <el-radio-group
              v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
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
              :label="td('dpp.integratioTask.driverCores', 'Driver核心数')"
              prop="driverCores"
             :label-position="labelPosition">
              <el-input-number
                v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                :placeholder="
                  td(
                    'dpp.integratioTask.inputDriverCores',
                    '请输入Driver核心数'
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
              :label="td('dpp.integratioTask.driverMemory', 'Driver内存数')"
              prop="driverMemory"
             :label-position="labelPosition">
              <el-input
                v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                v-model="form.driverMemory"
                :placeholder="
                  td(
                    'dpp.integratioTask.inputDriverMemory',
                    '请输入Driver内存数'
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
              :label="td('dpp.integratioTask.executorCount', 'Executor数量')"
              prop="numExecutors"
             :label-position="labelPosition">
              <el-input-number
                v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                :placeholder="
                  td(
                    'dpp.integratioTask.inputExecutorCount',
                    '请输入Executor数量'
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
              :label="td('dpp.integratioTask.executorMemory', 'Executor内存数')"
              prop="executorMemory"
             :label-position="labelPosition">
              <el-input
                v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                v-model="form.executorMemory"
                :placeholder="
                  td(
                    'dpp.integratioTask.inputExecutorMemory',
                    '请输入Executor内存数'
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
              :label="td('dpp.integratioTask.executorCores', 'Executor核心数')"
              prop="executorCores"
             :label-position="labelPosition">
              <el-input-number
                v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                :placeholder="
                  td(
                    'dpp.integratioTask.inputExecutorCores',
                    '请输入Executor核心数'
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
              :label="td('dpp.integratioTask.yarnQueue', 'Yarn队列')"
              prop="yarnQueue"
             :label-position="labelPosition">
              <el-input
                v-if="title != td('dpp.integratioTask.taskDetail', '任务详情')"
                v-model="form.yarnQueue"
                :placeholder="
                  td(
                    'dpp.integratioTask.inputYarnQueue',
                    '请输入Yarn队列(选填)'
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
            td("common.button.close", "关闭")
          }}</el-button>
          <el-button
            type="primary"
            v-if="!route.query.info"
            @click="saveClose"
            >{{ td("common.button.save", "保存") }}</el-button
          >
        </template>
        <template v-else>
          <el-button @click="saveClose">{{
            td("dpp.integratioTask.onlySave", "仅保存")
          }}</el-button>
          <el-button type="primary" @click="saveData">{{
            td("dpp.integratioTask.saveAndConfigFlow", "保存并配置流程")
          }}</el-button>
        </template>
      </div>
    </template>
  </el-dialog>

  <el-dialog
    :title="td('dpp.integratioTask.cronGenerator', 'Cron表达式生成器')"
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
import { ElMessage } from "element-plus";
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

// 根据调度器拆分执行引擎选项，默认 Quartz 仅展示 DataX。
const schedulerTypeList = ref([]);
const updateSchedulerTypeList = () => {
  schedulerTypeList.value = actuator_type.value.filter((item) =>
    form.value.scheduler === "QUARTZ"
      ? item.value === "DATAX"
      : item.value !== "DATAX"
  );
};

// 定义表单验证规则
const rules = {
  name: [
    {
      required: true,
      message: td("dpp.integratioTask.inputTaskName", "任务名称不能为空"),
      trigger: "change",
    },
  ],
  catCode: [
    {
      required: true,
      message: td("dpp.integratioTask.selectTaskCategory", "任务类目不能为空"),
      trigger: "change",
    },
  ],
  executionType: [
    {
      required: true,
      message: td(
        "dpp.integratioTask.selectExecutionStrategy",
        "执行策略不能为空"
      ),
      trigger: "change",
    },
  ],
  scheduler: [
    {
      required: true,
      message: td("dpp.integratioTask.selectScheduler", "调度器不能为空"),
      trigger: "change",
    },
  ],
  crontab: [
    {
      required: true,
      message: td("dpp.integratioTask.selectScheduleCycle", "调度周期不能为空"),
      trigger: "change",
    },
  ],
  // releaseState: [{ required: true, message: "任务状态不能为空", trigger: "change" }],
  taskType: [
    {
      required: true,
      message: td("dpp.integratioTask.executionEngine", "执行引擎不能为空"),
      trigger: "change",
    },
  ],
  personCharge: [
    {
      required: true,
      message: td(
        "dpp.integratioTask.selectResponsiblePerson",
        "责任人不能为空"
      ),
      trigger: "change",
    },
  ],
};
const form = ref({
  catId: "",
  name: "",
  catCode: "", // 可以初始化为空，也可以设为默认值
  executionType: "PARALLEL", // 初始化为空或默认值
  scheduler: "QUARTZ",
  actuator: "DATAX",
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
  taskType: "DATAX",
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

const schedulerGuide = computed(() => {
  if (form.value.scheduler === "DOLPHINSCHEDULER") {
    return {
      description: td(
        "dpp.integratioTask.dolphinSchedulerGuideDescription",
        "使用前请确保 DolphinScheduler 服务已启动。"
      ),
    };
  }

  return {
    description: td(
      "dpp.integratioTask.quartzGuideDescription",
      "由系统内置组件执行任务。"
    ),
  };
});

const reset = () => {
  proxy.resetForm("daDiscoveryTaskRef");
  form.value = {
    name: "",
    catId: "",
    catCode: "", // 可以初始化为空，也可以设为默认值
    executionType: "PARALLEL", // 初始化为空或默认值
    scheduler: "QUARTZ",
    actuator: "DATAX",
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
    taskType: "DATAX",
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
        // 兼容老任务：以前没有 scheduler/actuator 时，默认还是走 DS + Spark。
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
  console.log("任务类目改变了，当前值：", val);
  form.value.catId = val.id;
};

/**
 * DolphinScheduler调度器状态检查
 * @returns {Promise<void>}
 */
const handleSchedulerChange = async () => {
  updateSchedulerTypeList();
  if (form.value.scheduler == "QUARTZ") {
    // Quartz 当前只支持 DataX，切到 Quartz 时自动帮用户选好。
    form.value.taskType = "DATAX";
  } else {
    form.value.taskType = "SPARK";
  }
  syncActuatorByEngine();
};

// 字典为异步加载，加载完成后初始化 DataX 选项。
watch(actuator_type, updateSchedulerTypeList, { immediate: true });

// 引擎切换时，同步执行引擎
const handleExecutionEngineChange = (value) => {
  if (form.value.scheduler != "QUARTZ" && value == "DATAX") {
    // 非 Quartz 暂时不能选 DataX，提示后回到原来的 Spark。
    proxy.$modal.msgWarning(td("dpp.integratioTask.unsupportedEngineSwitch", "暂不支持切换"));
    form.value.taskType = "SPARK";
  }
  if (form.value.scheduler == "QUARTZ" && value != "DATAX") {
    // Quartz 暂时不能切到 Spark，提示后继续固定 DataX。
    proxy.$modal.msgWarning(td("dpp.integratioTask.unsupportedEngineSwitch", "暂不支持切换"));
    form.value.taskType = "DATAX";
  }
  syncActuatorByEngine();
};

// 保存前再兜底一次，避免编辑回显或脚本赋值绕过 change 事件。
const enforceQuartzDataX = () => {
  // 保存前再兜底一次，避免编辑回显或脚本赋值绕过 change 事件。
  if (form.value.scheduler == "QUARTZ") {
    form.value.taskType = "DATAX";
  }
};

// 引擎切换时，同步执行引擎
const syncActuatorByEngine = () => {
  // 后端识别的是 actuator：DataX 单独执行，其他执行引擎仍归到 DS 执行器。
  form.value.actuator = form.value.taskType == "DATAX" ? "DATAX" : "SPARK";
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
      console.log("表单校验未通过");
    }
  });
};
// 保存数据的方法
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
      console.log("表单校验未通过");
    }
  });
};

/**
 * 检查 dolphinscheduler api
 * @returns {Promise<AxiosResponse<any>>}
 */
const checkDSUpStart = async () => {
  const resp = await checkApi();
  if (!resp.data) {
    proxy.$modal.msgWarning(td("dpp.integratioTask.upDs", "请启动DolphinScheduler调度器！"));
  }
  return resp.data;
}

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
