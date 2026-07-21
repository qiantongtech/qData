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
    :title="effectiveTitle"
    destroy-on-close
    width="60%"
    :append-to="$refs['app-container']"
  >
    <el-form
      ref="daDiscoveryTaskRef"
      :model="form"
      label-width="120px"
      @submit.prevent
      :disabled="title == td('dpp.developTask.taskDetail', 'Task Details')"
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.developTask.taskName', 'Task Name')"
            prop="name"
            :rules="[
              {
                required: title != td('dpp.developTask.taskDetail', 'Task Details'),
                message: td('dpp.developTask.inputTaskName', 'Please enter task name'),
                trigger: 'blur',
              },
            ]"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.developTask.taskDetail', 'Task Details')"
              v-model="form.name"
              :placeholder="
                td('dpp.developTask.inputTaskName', 'Please enter task name')
              "
            />
            <div class="form-readonly" v-else>{{ form.name }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.developTask.dataDevCategory', 'Data Development Category')"
            prop="catCode"
            :rules="[
              {
                required: title != td('dpp.developTask.taskDetail', 'Task Details'),
                message: td(
                  'dpp.developTask.selectTaskCategory',
                  'Please select data development category'
                ),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-tree-select
              filterable
              v-model="form.catCode"
              :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }"
              value-key="id"
              :placeholder="
                td('dpp.developTask.selectTaskCategory', 'Please select data development category')
              "
              check-strictly
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.developTask.scheduleCycle', 'Schedule Cycle')"
            prop="crontab"
            :rules="[
              {
                required: title != td('dpp.developTask.taskDetail', 'Task Details'),
                message: td(
                  'dpp.developTask.selectScheduleCycle',
                  'Please select schedule cycle'
                ),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.developTask.taskDetail', 'Task Details')"
              v-model="form.crontab"
              :placeholder="
                td('dpp.developTask.selectScheduleCycle', 'Please select schedule cycle')
              "
              readonly
            >
              <template #append>
                <el-button
                  type="primary"
                  @click="handleShowCron"
                  style="background-color: #2666fb; color: #fff"
                >
                  {{ td("dpp.developTask.configure", "Configure") }}
                  <i class="el-icon-time el-icon--right"></i>
                </el-button>
              </template>
            </el-input>
            <div class="form-readonly" v-else>{{ form.crontab }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.developTask.executionEngine', 'Execution Engine')"
            prop="typaCode"
            :rules="[
              {
                required: title != td('dpp.developTask.taskDetail', 'Task Details'),
                message: td(
                  'dpp.developTask.selectExecutionEngine',
                  'Please select execution engine'
                ),
                trigger: 'change',
              },
            ]"
           :label-position="labelPosition">
            <el-tree-select
              filterable
              :disabled="info"
              v-model="form.typaCode"
              :data="treeData"
              :props="{ value: 'value', label: 'label', children: 'children' }"
              value-key="label"
              check-strictly
              @change="getDaDatasource(true)"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.developTask.responsiblePerson')"
            prop="personCharge"
            :rules="[
              {
                required: title != td('dpp.developTask.taskDetail', 'Task Details'),
                message: td('dpp.developTask.selectResponsiblePerson'),
                trigger: 'change',
              },
            ]"
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
                td('dpp.developTask.selectResponsiblePerson', 'Please select responsible person')
              "
              check-strictly
              @change="handleContactChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
              :label="td('dpp.developTask.contactNumber', 'Contact Phone')"
              prop="contactNumber"
              :label-position="labelPosition">
            <el-input
                v-if="title != td('dpp.developTask.taskDetail', 'Task Details')"
                v-model="form.contactNumber"
                :placeholder="
                td('dpp.developTask.inputContactNumber', 'Please enter contact phone')
              "
                disabled
            />
            <div class="form-readonly" v-else>{{ form.contactNumber }}</div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
              :label="td('dpp.developTask.scheduler', 'Scheduler')"
              prop="scheduler"
              :rules="[
              {
                required: title != td('dpp.developTask.taskDetail', 'Task Details'),
                message: td('dpp.developTask.selectScheduler', 'Please select a scheduler'),
                trigger: 'change',
              },
            ]"
              :label-position="labelPosition"
          >
            <el-radio-group
                v-if="title != td('dpp.developTask.taskDetail', 'Task Details')"
                v-model="form.scheduler"
                class="el-form-input-width"
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
            <div class="form-readonly" v-else>
              {{ getOptionLabel(schedulerOptions, form.scheduler) }}
            </div>
            <p style="display: flex;align-items: center;line-height: 1;font-size: 12px;color: #888; margin-top: 10px;">
              {{ schedulerGuide.description }}
            </p>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
            :label="td('dpp.developTask.description', 'Description')"
            prop="description"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.developTask.taskDetail', 'Task Details')"
              v-model="form.description"
              type="textarea"
              :placeholder="
                td('dpp.developTask.inputDescription', 'Please enter description')
              "
            />
            <div class="form-readonly" v-else>
              {{ form.description || "-" }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.developTask.taskStatus', 'Task Status')"
            prop="releaseState"
             :label-position="labelPosition"><el-radio-group
              style="width: 100%"
              v-model="form.releaseState"
              class="el-form-input-width"
              v-if="title != td('dpp.developTask.taskDetail', 'Task Details')"
            >
              <el-radio
                v-for="dict in dpp_etl_task_status"
                :key="dict.value"
                :value="dict.value"
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
      <!-- <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="td('dpp.developTask.remark', 'Remarks')" prop="remark" :label-position="labelPosition">
            <el-input v-model="form.remark" type="textarea" :placeholder="td('dpp.developTask.inputRemark', 'Please enter remarks')" />
          </el-form-item>
        </el-col>
      </el-row> -->
      <div class="h2" @click="templateShow = !templateShow">
        {{ td("dpp.developTask.useTemplate", "> Use Template") }}
      </div>
      <template v-if="templateAct.id || templateShow">
        <div class="h2-template" v-loading="tempLoading">
          <div
            class="h2-item"
            :class="{ act: templateAct.id == item.id }"
            v-for="item in templateList"
            :key="item.id"
            @click="handleTemplate(item)"
          >
            <div class="h2-item-title">{{ item.name }}</div>
            <div class="h2-item-editor">
              <CodeShow
                v-model="item.content"
                :config="{
                  renderSideBySide: false,
                  fontSize: 9,
                  scrollbar: {
                    vertical: 'hidden',
                    horizontal: 'hidden',
                  },
                }"
              />
            </div>
          </div>
          <el-empty
            style="width: 100%"
            v-if="total == 0"
            :description="td('common.noData')"
          />
        </div>
        <pagination
          layout="prev, pager, next"
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </template>
    </el-form>

    <template #footer>
      <div style="text-align: right">
        <template v-if="info">
          <el-button @click="closeDialog">{{
            td("common.button.close", "Close")
          }}</el-button>
          <el-button
            type="primary"
            @click="saveClose"
            v-if="!route.query.info"
            >{{ td("common.button.save", "Save") }}</el-button
          >
        </template>
        <template v-else>
          <el-button @click="saveClose">{{
            td("dpp.developTask.saveOnly", "Save Only")
          }}</el-button>
          <el-button type="primary" @click="saveData">{{
            td("dpp.developTask.saveAndConfigure", "Save and Configure Flow")
          }}</el-button>
        </template>
      </div>
    </template>
  </el-dialog>
  <el-dialog
    :title="td('dpp.developTask.cronGenerator', 'Cron Expression Generator')"
    v-model="openCron"
    class="dialog"
    :append-to="$refs['app-container']"
    destroy-on-close
  >
    <crontab
      ref="crontabRef"
      @hide="openCron = false"
      @fill="crontabFill"
      :expression="expression"
    >
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
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import CodeShow from "@/components/SqlEditor/editorShow/index.vue";
import Crontab from "@/components/Crontab/index.vue";
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const { proxy } = getCurrentInstance();
import { dppEtlSqlTemp, getNodeUniqueKey } from "@/api/dpp/task/index.js";
import { listDaDatasourceNoKafkaByProjectCode } from "@/api/da/dataSource/dataSource";
const { dpp_etl_task_status, scheduler_type } = proxy.useDict("dpp_etl_task_status","scheduler_type");
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
import { treeData } from "@/views/dpp/task/developTask/data";
import {checkApi} from "@/api/ds/api/api.js";
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  data: { type: Object, default: () => ({}) },
  deptOptions: { type: Object, default: () => ({}) },
  userList: { type: Object, default: () => ({}) },
  info: { type: Boolean, default: false },
});
const effectiveTitle = computed(() => props.title || td('dpp.developTask.formTitle'));

const emit = defineEmits(["update:visible", "confirm", "save"]);

const schedulerOptions = [
  { label: "Quartz", value: "QUARTZ" },
  { label: "DolphinScheduler", value: "DOLPHINSCHEDULER" },
];
const getOptionLabel = (options, value) => {
  return options.find((item) => item.value == value)?.label || value || "-";
};

const form = ref({
  // form data
  name: "",
  catCode: "",
  personCharge: "",
  contactNumber: "",
  crontab: "",
  scheduler: "QUARTZ",
  actuator: "JDBC",
  releaseState: "0",
  description: "",
  // json value
  typaCode: "DM",
  // fixed value
  executionType: "PARALLEL", // Initialized to empty or default value
  status: "0",
  datasources: { datasourceId: "" },
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

const total = ref(0);
const queryParams = ref({
  pageNum: 1,
  pageSize: 6,
});
const tempLoading = ref(false);
const getList = async () => {
  tempLoading.value = true;
  try {
    let type = treeData.filter((item) => item.value == form.value.typaCode)[0]
      .id;
    let params = {
      ...queryParams.value,
      type: type,
    };
    dppEtlSqlTemp(params).then((response) => {
      templateList.value = response.data.rows;
      total.value = response.data.total;
    });
  } finally {
    tempLoading.value = false;
  }
};

const templateShow = ref(true);
const templateAct = ref({
  id: "",
  sqlData: { content: "" },
  queryParams: queryParams.value,
  typaCode: "DM",
});
const templateList = ref([]);
const handleTemplate = (item) => {
  templateAct.value = {
    id: item.id,
    sqlData: item,
    queryParams: queryParams.value,
    typaCode: form.value.typaCode,
  };
};

let loading = ref(false);
let createTypeList = ref([]);

/** Query data development task list */
function getDaDatasource(flag) {
  templateAct.value.typaCode = form.value.typaCode;
  // Refresh template list
  getList();
  loading.value = true;
  listDaDatasourceNoKafkaByProjectCode({
    projectCode: userStore.projectCode,
    projectId: userStore.projectId,
    datasourceType: form.value.typaCode,
  }).then((response) => {
    createTypeList.value = response.data;
    if (flag) {
      form.value.datasources.datasourceId = "";
    }
    // console.log("🚀 ~ getDaDatasourceList ~ response:", response);
    loading.value = false;
  });
}
watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      form.value = { ...form.value, ...props.data };
      form.value.scheduler = form.value.scheduler || "DOLPHINSCHEDULER";
      form.value.actuator = form.value.actuator || "SPARK";
      enforceQuartzJDBC();
      // Template
      templateAct.value = form.value.draftJson
        ? JSON.parse(form.value.draftJson)
        : { ...templateAct.value };
      // Get template list
      queryParams.value = templateAct.value.queryParams || queryParams.value;
      // execution engine
      form.value.typaCode = templateAct.value.typaCode;
      getDaDatasource();
      getList();
      form.value.personCharge = Number(form.value.personCharge) || "";
      // Task status
      if (form.value.status != null && form.value.status != undefined) {
        form.value.releaseState =
          form.value.status == "-1" ? "0" : form.value.status;
      }
    } else {
      proxy.resetForm("daDiscoveryTaskRef");
    }
  }
);

// Computed property handling v-model
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

/**
 * DolphinScheduler
 * @returns {Promise<void>}
 */
const handleSchedulerChange = async () => {
  if (form.value.scheduler == "QUARTZ") {
    form.value.actuator = "JDBC";
  } else {
    form.value.actuator = "SPARK";
  }
};

const enforceQuartzJDBC = () => {
  if (form.value.scheduler == "QUARTZ") {
    form.value.actuator = "JDBC";
  }
};

let daDiscoveryTaskRef = ref();
// How to close a dialog box
const closeDialog = () => {
  emit("update:visible", false);
};
const saveClose = async () => {
  try {
    if (form.value.scheduler === 'DOLPHINSCHEDULER' && !await checkDSUpStart()) {
      return;
    }
    const valid = await daDiscoveryTaskRef.value.validate();
    if (valid) {
      enforceQuartzJDBC();
      if (!form.value.code) {
        const response = await getNodeUniqueKey({
          scheduler: form.value.scheduler,
          projectCode: userStore.projectCode || "133545087166112",
          projectId: userStore.projectId,
        });
        if (response && response.data) {
          form.value.code = response.data; // Set unique code
        }
      }
      const formData = JSON.parse(JSON.stringify(form.value));
      formData.draftJson = JSON.stringify(templateAct.value);
      console.log("🚀 ~ saveData ~ formData:", formData);
      emit("save", formData);
      emit("update:visible", false);
    } else {
      console.log("Form validation failed");
    }
  } catch (error) {
    console.error("Error while saving data:", error);
  }
};
// How to save data
const saveData = async () => {
  try {
    if (form.value.scheduler === 'DOLPHINSCHEDULER' && !await checkDSUpStart()) {
      return;
    }
    const valid = await daDiscoveryTaskRef.value.validate();
    if (valid) {
      enforceQuartzJDBC();
      if (!form.value.code) {
        const response = await getNodeUniqueKey({
          projectCode: userStore.projectCode || "133545087166112",
          projectId: userStore.projectId,
        });
        if (response && response.data) {
          form.value.code = response.data; // Set unique code
        }
      }
      const formData = JSON.parse(JSON.stringify(form.value));
      formData.draftJson = JSON.stringify(templateAct.value);
      console.log("🚀 ~ saveData ~ formData:", formData);
      emit("confirm", formData);
      emit("update:visible", false);
    } else {
      console.log("Form validation failed");
    }
  } catch (error) {
    console.error("Error while saving data:", error);
  }
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
// Define form validation rules
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

.h2 {
  user-select: none;
  cursor: pointer;
  font-size: 14px;
  color: var(--el-color-primary);

  &:hover {
    color: var(--el-color-primary-light-3);
  }
}

.h2-template {
  display: flex;
  flex-flow: row wrap;
  margin-top: 10px;
  background: #f8f9fa;
  padding: 10px;
  gap: 10px;
  border-radius: 6px;

  .h2-item {
    position: relative;
    width: 32.6%;
    border: 1px solid rgba(5, 5, 5, 0.06);
    border-radius: 6px;
    transition: box-shadow 0.3s, border-color 0.3s;

    &:hover {
      border-color: transparent;
      box-shadow: 0 1px 2px -2px #00000029, 0 3px 6px #0000001f,
        0 5px 12px 4px #00000017;
    }

    &.act {
      .h2-item-title,
      .h2-item-editor {
        background: #e6f7ff;
      }

      &::after {
        visibility: visible;
        position: absolute;
        inset-block-start: 2px;
        inset-inline-end: 2px;
        opacity: 1;
        width: 0;
        height: 0;
        border: 6px solid #1890ff;
        border-block-end: 6px solid transparent;
        border-inline-start: 6px solid transparent;
        border-start-end-radius: 2px;
        content: "";
      }
    }

    .h2-item-title {
      background: #fff;
      padding: 8px 12px 0;
      font-size: 14px;
      color: #000000e0;
    }

    .h2-item-editor {
      background: #fff;
      padding: 8px;
      height: 150px;
    }
  }
}
</style>
