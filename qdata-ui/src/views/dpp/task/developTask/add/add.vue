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
      :disabled="title == td('dpp.developTask.taskDetail', '任务详情')"
     :label-position="labelPosition">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.developTask.taskName', '任务名称')"
            prop="name"
            :rules="[
              {
                required: title != td('dpp.developTask.taskDetail', '任务详情'),
                message: td('dpp.developTask.inputTaskName', '请输入任务名称'),
                trigger: 'blur',
              },
            ]"
           :label-position="labelPosition">
            <el-input
              v-if="title != td('dpp.developTask.taskDetail', '任务详情')"
              v-model="form.name"
              :placeholder="
                td('dpp.developTask.inputTaskName', '请输入任务名称')
              "
            />
            <div class="form-readonly" v-else>{{ form.name }}</div>
            <p class="form-item-description">
              {{ td("dpp.developTask.taskNameDescription", "任务唯一标识，用于检索和区分任务") }}
            </p>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="td('dpp.developTask.dataDevCategory', '数据开发类目')"
            prop="catCode"
            :rules="[
              {
                required: title != td('dpp.developTask.taskDetail', '任务详情'),
                message: td(
                  'dpp.developTask.selectTaskCategory',
                  '请选择数据开发类目'
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
                td('dpp.developTask.selectTaskCategory', '请选择数据开发类目')
              "
              check-strictly
            />
            <p class="form-item-description">
              {{ td("dpp.developTask.taskCategoryDescription", "任务所属开发类目，用于分类管理") }}
            </p>
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
                required: title != td('dpp.developTask.taskDetail', '任务详情'),
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
                td('dpp.developTask.selectResponsiblePerson', '请选择责任人')
              "
              check-strictly
              @change="handleContactChange"
            />
            <p class="form-item-description">
              {{ td("dpp.developTask.responsiblePersonDescription", "任务维护责任人，用于异常跟进和追溯") }}
            </p>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
              :label="td('dpp.developTask.contactNumber', '联系电话')"
              prop="contactNumber"
              :label-position="labelPosition">
            <el-input
                v-if="title != td('dpp.developTask.taskDetail', '任务详情')"
                v-model="form.contactNumber"
                :placeholder="
                td('dpp.developTask.inputContactNumber', '请输入联系电话')
              "
                disabled
            />
            <div class="form-readonly" v-else>{{ form.contactNumber }}</div>
            <p class="form-item-description">
              {{ td("dpp.developTask.contactNumberDescription", "责任人联系电话，随责任人自动带出") }}
            </p>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
              :label="td('dpp.integration.dbConnectionType', '数据连接类型')"
              prop="typaCode"
              :rules="[
              {
                required: title != td('dpp.developTask.taskDetail', '任务详情'),
                message: td(
                  'dpp.developTask.selectExecutionEngine',
                  '请选择执行引擎'
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
            <p class="form-item-description">
              {{ td("dpp.developTask.executionEngineDescription", "指定开发任务使用的数据库类型") }}
            </p>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
              :label="td('dpp.developTask.taskStatus', '任务状态')"
              prop="releaseState"
              :label-position="labelPosition"><el-radio-group
              style="width: 100%"
              v-model="form.releaseState"
              class="el-form-input-width"
              v-if="title != td('dpp.developTask.taskDetail', '任务详情')"
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
            <p class="form-item-description">
              {{ td("dpp.developTask.taskStatusDescription", "控制任务是否处于可运行状态") }}
            </p>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
              :label="td('dpp.developTask.description', '描述')"
              prop="description"
              :label-position="labelPosition">
            <el-input
                v-if="title != td('dpp.developTask.taskDetail', '任务详情')"
                v-model="form.description"
                type="textarea"
                :placeholder="
                td('dpp.developTask.inputDescription', '请输入描述')
              "
            />
            <div class="form-readonly" v-else>
              {{ form.description || "-" }}
            </div>
            <p class="form-item-description">
              {{ td("dpp.developTask.descriptionDescription", "记录任务用途和业务说明") }}
            </p>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item
              :label="td('dpp.integratioTask.schedulerSystem', '调度系统')"
              prop="scheduler"
              :rules="[
              {
                required: title != td('dpp.developTask.taskDetail', '任务详情'),
                message: td('dpp.developTask.selectScheduler', '请选择调度器'),
                trigger: 'change',
              },
            ]"
              :label-position="labelPosition"
          >
            <el-radio-group
                v-if="title != td('dpp.developTask.taskDetail', '任务详情')"
                v-model="form.scheduler"
                class="scheduler-card-group"
                @change="handleSchedulerChange"
            >
              <el-radio
                  v-for="(item, index) in scheduler_type"
                  :key="index"
                  :value="item.value"
                  class="option-card"
              >
                <img
                    class="option-card__icon scheduler-card__icon"
                    :src="getSchedulerMeta(item.value).icon"
                    :alt="item.label"
                />
                <span class="option-card__content">
                  <span class="option-card__heading">
                    <span class="option-card__name">{{ item.label }}</span>
                    <el-tag type="primary" :underline="false" class="task-cat-ellipsis" size="small">
                      {{ getSchedulerMeta(item.value).tag || "-" }}
                    </el-tag>
                  </span>
                  <span class="option-card__description">
                    {{ getSchedulerMeta(item.value).description }}
                  </span>
                </span>
              </el-radio>
            </el-radio-group>
            <div class="form-readonly" v-else>
              {{ getOptionLabel(form.scheduler) }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>


      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
              :label="td('dpp.developTask.scheduleCycle', '调度周期')"
              prop="crontab"
              :rules="[
              {
                required: title != td('dpp.developTask.taskDetail', '任务详情'),
                message: td(
                  'dpp.developTask.selectScheduleCycle',
                  '请选择调度周期'
                ),
                trigger: 'change',
              },
            ]"
              :label-position="labelPosition">
            <el-input
                v-if="title != td('dpp.developTask.taskDetail', '任务详情')"
                v-model="form.crontab"
                :placeholder="
                td('dpp.developTask.selectScheduleCycle', '请选择调度周期')
              "
                readonly
            >
              <template #append>
                <el-button
                    type="primary"
                    @click="handleShowCron"
                    style="background-color: #2666fb; color: #fff"
                >
                  {{ td("dpp.developTask.configure", "配置") }}
                  <i class="el-icon-time el-icon--right"></i>
                </el-button>
              </template>
            </el-input>
            <div class="form-readonly" v-else>{{ form.crontab }}</div>
            <p class="form-item-description">
              {{ td("dpp.developTask.scheduleCycleDescription", "定义任务自动触发的时间和频率") }}
            </p>
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
        {{ td("dpp.developTask.useTemplate", "> 使用模板") }}
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
            td("common.button.close", "关闭")
          }}</el-button>
          <el-button
            type="primary"
            @click="saveClose"
            v-if="!route.query.info"
            >{{ td("common.button.save", "保存") }}</el-button
          >
        </template>
        <template v-else>
          <el-button @click="saveClose">{{
            td("dpp.developTask.saveOnly", "仅保存")
          }}</el-button>
          <el-button type="primary" @click="saveData">{{
            td("dpp.developTask.saveAndConfigure", "保存并配置流程")
          }}</el-button>
        </template>
      </div>
    </template>
  </el-dialog>
  <el-dialog
    :title="td('dpp.developTask.cronGenerator', 'Cron表达式生成器')"
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
import quartzIcon from "@/assets/images/common/img-quartz.png";
import dolphinSchedulerIcon from "@/assets/images/common/img-ds.png";
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const { proxy } = getCurrentInstance();
import { dppEtlSqlTemp, getNodeUniqueKey } from "@/api/dpp/task/index.js";
import { listDaDatasourceNoKafkaByProjectCode } from "@/api/da/dataSource/dataSource";
const { dpp_etl_task_status } = proxy.useDict("dpp_etl_task_status");
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
import { treeData } from "@/views/dpp/task/developTask/data";
import {checkApi} from "@/api/ds/api/api.js";

const scheduler_type = [
  { label: "Quartz", value: "QUARTZ" },
  { label: "DolphinScheduler", value: "DOLPHINSCHEDULER" },
];

const schedulerMeta = {
  QUARTZ: {
    icon: quartzIcon,
    tag: td("dpp.integratioTask.lightweight", "轻量级"),
    description: td(
      "dpp.integratioTask.quartzCardDescription",
      "轻量级调度，适用于简单定时任务。"
    ),
  },
  DOLPHINSCHEDULER: {
    icon: dolphinSchedulerIcon,
    tag: td("dpp.integratioTask.enterprise", "企业级"),
    description: td(
      "dpp.integratioTask.dolphinSchedulerCardDescription",
      "企业级调度，适用于复杂工作流编排。"
    ),
  },
};

const getSchedulerMeta = (value) =>
  schedulerMeta[value] || schedulerMeta.QUARTZ;

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

const getOptionLabel = (value) => {
  return scheduler_type.find((item) => item.value == value)?.label || value || "-";
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
    proxy.$modal.msgWarning(td("dpp.integratioTask.upDs", "请启动DolphinScheduler调度器！"));
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

.form-item-description {
  display: flex;
  align-items: center;
  margin-top: 10px;
  color: #888;
  font-size: 12px;
  line-height: 1;
  margin-bottom: 0;
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

.scheduler-section-title {
  display: flex;
  align-items: center;
  margin-top: 4px;
  padding-top: 16px;
  border-top: 1px solid var(--el-border-color-lighter);
}

.scheduler-card-group {
  --option-card-width: max(210px, calc((100% - 34px) / 3));

  display: flex;
  flex-wrap: nowrap;
  gap: 17px;
  width: 100%;
}

:deep(.scheduler-card-group .option-card.el-radio) {
  flex: 0 0 var(--option-card-width);
  width: var(--option-card-width);
  margin-right: 0;
}

:deep(.option-card.el-radio) {
  position: relative;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  min-width: 0;
  padding: 13px 30px 13px 15px;
  white-space: normal;
  transition: border-color 0.2s, box-shadow 0.2s;
  height: 86px;
  background: #FFFFFF;
  border-radius: 2px;
  border: 1px solid #D6DAE1;

  &:hover:not(.is-disabled) {
    border-color: var(--el-color-primary);
  }

  &.is-checked {
    border-color: var(--el-color-primary);
  }

  &.is-disabled {
    background: var(--el-fill-color-lighter);
  }

  .el-radio__input {
    position: absolute;
    top: 14px;
    right: 12px;
  }

  .el-radio__label {
    display: flex;
    flex: 1;
    align-self: stretch;
    align-items: center;
    min-width: 0;
    width: 100%;
    padding-left: 0;
    color: var(--el-text-color-primary);
  }
}

.option-card__icon {
  flex: 0 0 auto;
  width: 36px;
  margin-right: 12px;
  object-fit: contain;
}

.option-card__content,
.option-card__heading,
.option-card__description {
  display: block;
}

.option-card__content {
  flex: 1;
  min-width: 0;
  width: 0;
}

.option-card__heading {
  display: flex;
  align-items: center;
  min-width: 0;
  margin-bottom: 4px;
  width: 100%;

  .task-cat-ellipsis {
    min-width: 60px;
  }
}

.option-card__name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  height: 20px;
  margin-right: 8px;
  color: var(--color-third-party-Langfuse);
  font-family: PingFangSC, PingFang SC;
  font-size: 14px;
  font-style: normal;
  font-weight: 400;
  line-height: 20px;
  text-align: left;
  max-width: calc(100% - 60px);
}

.option-card__description {
  display: -webkit-box;
  overflow: hidden;
  width: 108%;
  color: rgba(0, 0, 0, 0.65);
  font-family: PingFangSC, PingFang SC;
  font-size: 12px;
  font-style: normal;
  font-weight: 400;
  line-height: 18px;
  text-align: left;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

:deep(.option-card.el-radio.is-disabled) {
  .option-card__icon,
  .option-card__content {
    opacity: 0.55;
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
