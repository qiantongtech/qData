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
  <div class="app-container" ref="app-container">
    <guide-tip tip-id="mc/task/structured" />
    <el-container>
      <!-- <SourceSystemTree
        ref="sourceSystemTreeRef"
        @node-click="handleNodeClick"
        @data-loaded="handleTreeDataLoaded"
      /> -->
      <el-main class="main-content">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQueryClick"
              @reset="handleResetQueryClick"
            />
          </template>
          <template #actions-data>
            <el-button type="primary" plain @click="handleAddClick">
              <i class="iconfont-mini icon-xinzeng mr5"></i
              >{{ td("common.button.add") }}
            </el-button>
            <el-button
              type="danger"
              plain
              icon="Delete"
              :disabled="!store.rows.length"
              @click="handleDeleteColumnClick"
            >
              {{ td("common.button.delete") }}
            </el-button>
          </template>
          <qt-table v-bind="tableStore" ref="tableRef">
            <template #task-status="scope">
              <el-switch
                v-if="scope.row.status != undefined"
                v-model="scope.row.status"
                active-value="1"
                inactive-value="0"
                @change="handleTaskStatusChange(scope.row, $event)"
              />
            </template>

            <template #scheduler-status="scope">
              <el-switch
                v-if="scope.row.schedulerStatus != undefined"
                v-model="scope.row.schedulerStatus"
                active-value="1"
                inactive-value="0"
                @change="handleSchedulerStatusChange(scope.row, $event)"
              />
            </template>

            <template #handle="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                :disabled="row.status == '1'"
                @click="handleEditClick(row)"
              >
                {{ td("common.button.update") }}
              </el-button>
              <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetailClick(row)"
              >
                {{ td("common.button.details") }}
              </el-button>
              <el-popover
                placement="bottom"
                :width="150"
                trigger="click"
                popper-class="handle-popover"
              >
                <template #reference>
                  <el-button link type="primary" icon="ArrowDown">
                    {{ td("common.button.more") }}
                  </el-button>
                </template>
                <div style="width: 100px">
                  <el-button
                    link
                    type="primary"
                    icon="Document"
                    @click="handleInstanceClick(row)"
                    style="padding-left: 14px"
                  >
                    {{ td("mc.task.structured.collectInstance") }}
                  </el-button>
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    :disabled="row.status == '0'"
                    :loading="runClickLoading"
                    @click="handleRunClick(row)"
                  >
                    {{ td("mc.task.structured.executeOnce") }}
                  </el-button>
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="row.status == '1'"
                    @click="handleDeleteClick(row)"
                  >
                    {{ td("common.button.delete") }}
                  </el-button>
                </div>
              </el-popover>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <!-- Scheduling cycle pop-up window -->
    <el-dialog
      :title="td('mc.task.structured.cronGenerator')"
      v-model="cronDialog.open"
      :append-to="$refs['app-container']"
      destroy-on-close
    >
      <Crontab
        @hide="handleCloseCronClick"
        @fill="handleConfirmCronClick"
        :expression="cronDialog.data"
      />
    </el-dialog>

    <!-- Add/modify pop-up window -->
    <el-dialog
      v-model="dialog.open"
      :title="dialog.title"
      width="1200"
      :loading="dialog.loading"
      @close="handleCancelClick"
    >
      <el-form
        :model="dialog.form"
        class="column-form"
        :rules="rules"
        ref="formRef"
        label-width="110px"
       :label-position="labelPosition">
            <el-form-item
                :label="td('mc.task.structured.sourceSystem')"
                prop="sourceSystemId"
                :label-position="labelPosition">
              <el-tree-select
                  filterable
                  v-model="dialog.form.sourceSystemId"
                  :data="store.sourceSystems"
                  :props="{ value: 'id', label: 'name', children: 'children' }"
                  value-key="id"
                  :placeholder="td('mc.task.structured.sourceSystemPlaceholder')"
                  check-strictly
                  @change="handleDomainChange"
                  default-expand-all
              />
            </el-form-item>

            <el-form-item :label="td('mc.task.structured.taskName')" prop="name" :label-position="labelPosition">
              <el-input
                  v-model="dialog.form.name"
                  :placeholder="td('mc.task.structured.taskNamePlaceholder')"
              />
            </el-form-item>

        <el-form-item
            :label="td('mc.task.structured.datasourceName')"
            prop="datasourceId"
            :label-position="labelPosition">
          <el-select
              v-model="dialog.form.datasourceId"
              :placeholder="td('mc.task.structured.datasourceNamePlaceholder')"
              @change="handleDatasourceChange"
          >
            <el-option
                v-for="item in store.datasources"
                :key="item.id"
                :label="item.datasourceName"
                :value="item.id"
                :disabled="
                !['DM8', 'MySql', 'Oracle11', 'Oracle', 'PostgreSQL', 'Hive'].includes(
                  item.datasourceType
                )
              "
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="td('mc.task.structured.dbType')" prop="dbType" :label-position="labelPosition">
          <el-input
              v-model="dialog.form.dbType"
              disabled
              :placeholder="td('mc.task.structured.dbTypePlaceholder')"
          />
        </el-form-item>
        <el-form-item :label="td('mc.task.structured.ip')" prop="ip" :label-position="labelPosition">
          <el-input
              v-model="dialog.form.ip"
              disabled
              :placeholder="td('mc.task.structured.ipPlaceholder')"
          />
        </el-form-item>

        <el-form-item :label="td('mc.task.structured.port')" prop="port" :label-position="labelPosition">
          <el-input
              v-model="dialog.form.port"
              disabled
              :placeholder="td('mc.task.structured.portPlaceholder')"
          />
        </el-form-item>
        <el-form-item
            :label="td('mc.task.structured.username')"
            prop="username"
            :label-position="labelPosition">
          <el-input
              v-model="dialog.form.username"
              disabled
              :placeholder="td('mc.task.structured.usernamePlaceholder')"
          />
        </el-form-item>


        <el-form-item :label="td('mc.task.structured.leader')" prop="leader" :label-position="labelPosition">
          <el-tree-select
              filterable
              v-model="dialog.form.leader"
              :data="store.userList"
              :props="{
              value: 'userId',
              label: 'nickName',
              children: 'children',
            }"
              value-key="userId"
              :placeholder="td('mc.task.structured.leaderInfo')"
              check-strictly
              @change="handleUserChange"
          />
        </el-form-item>
        <el-form-item
            :label="td('mc.task.structured.leaderPhone')"
            prop="leaderPhone"
            :label-position="labelPosition">
          <el-input
              v-model="dialog.form.leaderPhone"
              disabled
              :placeholder="td('mc.task.structured.contactPhoneInfo')"
          />
        </el-form-item>

        <el-form-item
            :label="td('mc.task.structured.collectionMode')"
            class="row-full"
            prop="collectionMode"
            v-if="false"
            :label-position="labelPosition">
          <el-radio-group v-model="dialog.form.collectionMode">
            <el-radio
                v-for="dict in toValue(dicts.mc_collect_mode)"
                :key="dict.value"
                :label="dict.value"
            >
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
                :label="td('mc.task.structured.scheduler')"
                prop="scheduler"
                class="row-full"
                :label-position="labelPosition">
              <el-radio-group
                  v-model="dialog.form.scheduler"
                  class="scheduler-card-group"
              >
                <el-radio
                    v-for="item in scheduler_type"
                    :key="item.value"
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
            </el-form-item>

        <el-form-item
          class="cron-expression-form-item"
          :label="td('mc.task.structured.cronExpression')"
          prop="cronExpression"
          :tip="{
            content: td('mc.task.structured.cronTip'),
          }"
        >
          <el-input
            v-model="dialog.form.cronExpression"
            :placeholder="td('mc.task.structured.cronExpressionPlaceholder')"
          >
            <template #append>
              <el-button
                type="primary"
                @click="handleOpenCronClick"
                style="background-color: #2666fb; color: #fff"
              >
                {{ td("mc.task.structured.cronConfig") }}
                <i class="el-icon-time el-icon--right"></i>
              </el-button>
            </template>
          </el-input>
          <p
            v-if="showCronExpressionDescription"
            class="form-item-description cron-expression-description"
          >
            {{ td("mc.task.structured.cronExpressionDescription", "定义任务自动采集的时间和执行频率") }}
          </p>
        </el-form-item>

        <el-form-item
          :label="td('mc.task.structured.collectionScope')"
          class="row-full"
          prop="collectionScope"
         :label-position="labelPosition">
          <div class="collection-wrap">
            <el-radio-group v-model="dialog.form.collectionScope">
              <el-radio
                v-for="dict in toValue(dicts.mc_collect_scope)"
                :key="dict.value"
                :label="dict.value"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>

            <el-form-item
              prop="tables"
              v-if="dialog.form.collectionScope == 1"
              label-width="0"
              style="margin-bottom: 0"
             :label-position="labelPosition">
              <el-transfer
                v-model="dialog.form.tables"
                :data="dialog.tableList"
                :props="{ label: 'label', key: 'dbName' }"
                filterable
                :filter-method="onFilterTransfer"
                :filter-placeholder="td('mc.task.structured.inputDbName')"
                :titles="[
                  td('mc.task.structured.selectSourceDb'),
                  td('mc.task.structured.selectSelectedDb'),
                ]"
                style="--el-transfer-panel-width: 320px"
              />
            </el-form-item>
          </div>
        </el-form-item>

        <el-form-item
          :label="td('common.texts.description')"
          class="row-full"
          prop="description"
         :label-position="labelPosition">
          <el-input
            v-model="dialog.form.description"
            type="textarea"
            :placeholder="td('common.form.descriptionPlaceholder')"
            :min-height="192"
            show-word-limit
            :maxlength="500"
          />
        </el-form-item>

        <el-form-item
          :label="td('common.texts.remark')"
          class="row-full"
          prop="remark"
         :label-position="labelPosition">
          <el-input
            v-model="dialog.form.remark"
            type="textarea"
            :placeholder="td('common.form.remarkPlaceholder')"
            :min-height="192"
            show-word-limit
            :maxlength="500"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCancelClick">{{
            td("common.button.cancel")
          }}</el-button>
          <el-button type="primary" :loading="dialog.loading" @click="handleConfirmClick">
            {{ td("common.button.confirm") }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="McTaskStructured">
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";
import {
  reactive,
  ref,
  getCurrentInstance,
  toValue,
  computed,
  watch,
} from "vue";
import Crontab from "@/components/Crontab/index.vue";
import SourceSystemTree from "./components/SourceSystemTree.vue";
import { getParentLabelPath } from "@/utils/anivia.js";
import {
  listTask,
  addTask,
  getTask,
  updateTask,
  delTask,
  getRealtimeMcTaskScopeList,
  updateReleaseJobTask,
  updateReleaseSchedule,
  runJobOnce,
  sourceSystemTree,
  batchDeleteCheck,
} from "@/api/mc/task/task";
import { listDaDatasource } from "@/api/mc/dataSource/dataSource";
import { deptUserTree } from "@/api/system/system/user.js";
import { listValidSourceSystem } from "@/api/att/sourceSystem/sourceSystem";
import useDefaultLang from "@/composables/useDefaultLang";
import { checkApi } from "@/api/ds/api/api.js";
import { ElMessage } from "element-plus";
import quartzIcon from "@/assets/images/common/img-quartz.png";
import dolphinSchedulerIcon from "@/assets/images/common/img-ds.png";

const { td } = useDefaultLang();

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

const showCronExpressionDescription = ref(true);
const validateCronExpression = (_rule, value, callback) => {
  showCronExpressionDescription.value = Boolean(value);
  if (!value) {
    callback(
      new Error(td("mc.task.structured.cronRequired", "请配置调度周期"))
    );
    return;
  }
  callback();
};

const rules = {
  sourceSystemId: [
    {
      required: true,
      message: td("mc.task.structured.sourceSystemRequired"),
      trigger: "change",
    },
  ],
  name: [
    {
      required: true,
      message: td("mc.task.structured.taskNameRequired"),
      trigger: "blur",
    },
    {
      min: 2,
      max: 20,
      message: td("mc.task.structured.taskNameLength"),
      trigger: "blur",
    },
  ],
  datasourceId: [
    {
      required: true,
      message: td("mc.task.structured.datasourceRequired"),
      trigger: "change",
    },
  ],
  dbType: [
    {
      required: true,
      message: td("mc.task.structured.dbTypeRequired"),
      trigger: ["blur", "change"],
    },
  ],
  ip: [
    {
      required: true,
      message: td("mc.task.structured.ipRequired"),
      trigger: ["blur", "change"],
    },
  ],
  port: [
    {
      required: true,
      message: td("mc.task.structured.portRequired"),
      trigger: ["blur", "change"],
    },
  ],
  username: [
    {
      required: true,
      message: td("mc.task.structured.usernameRequired"),
      trigger: ["blur", "change"],
    },
  ],
  cronExpression: [
    {
      required: true,
      validator: validateCronExpression,
      trigger: "blur",
    },
  ],
  scheduler: [
    {
      required: true,
      message: td("mc.task.structured.schedulerRequired"),
      trigger: "change",
    },
  ],
  collectionMode: [
    {
      required: true,
      message: td("mc.task.structured.collectionModeRequired"),
      trigger: "change",
    },
  ],
  collectionScope: [
    {
      required: true,
      message: td("mc.task.structured.collectionScopeRequired"),
      trigger: "change",
    },
  ],
  tables: [
    {
      required: true,
      validator: (rule, value, callback) => {
        if (
          dialog.form.collectionScope == "1" &&
          (!value || value.length === 0)
        ) {
          callback(new Error(td("mc.task.structured.selectedDbRequired")));
        } else {
          callback();
        }
      },
      trigger: "change",
    },
  ],
};

const DETAIL_PATH = "/dg/meta/task/detail";

const { proxy } = getCurrentInstance();
const runClickLoading = ref(false);
const dicts = proxy.useDict(
  "datasource_type",
  "mc_collect_scope",
  "mc_collect_mode"
);

const router = useRouter();

const formRef = ref();
const sourceSystemTreeRef = ref();
const store = reactive({
  loading: false,
  rows: [],
  domains: [],
  treeDomains: [],
  sourceSystems: [],
  flatSourceSystems: [],
  datasources: [],
  userList: [],
});

function getAllSourceSystems() {
  listValidSourceSystem().then((res) => {
    store.sourceSystems = res.data;
    // Flatten data for lookups
    const flatten = (list) => {
      if (!Array.isArray(list)) return [];
      let result = [];
      list.forEach((item) => {
        result.push(item);
        if (item.children && item.children.length > 0) {
          result = result.concat(flatten(item.children));
        }
      });
      return result;
    };
    store.flatSourceSystems = flatten(res.data);
  });
}

function handleTreeDataLoaded({ treeData, flatData }) {
  store.treeDomains = treeData;
  store.domains = flatData;
}

// Node click event
function handleNodeClick(data) {
  // Clear previous filters
  tableStore.params.sourceSystemId = undefined;
  tableStore.params.datasourceId = undefined;
  tableStore.params.id = undefined;
  tableStore.params.type = undefined;

  if (data.type === "SOURCE") {
    tableStore.params.sourceSystemId = data.id;
  } else if (data.type === "DATASOURCE") {
    tableStore.params.datasourceId = data.id;
    tableStore.params.type = data.type;
  } else if (data.type === "DATABASE") {
    tableStore.params.id = data.taskId;
  }
  tableRef.value.getList();
}

// list
const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
      onRowDblclick: handleDetailClick,
    },
  },
  columns: [
    {
      type: "selection",
      width: 55,
    },
    {
      label: td("common.texts.number"),
      prop: "id",
      sortable: true,
      width: 60,
    },
    {
      label: td("mc.task.structured.taskName"),
      prop: "name",
      width: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
      link: {
        external: handleDetailClick,
      },
    },
    {
      label: td("common.texts.description"),
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.sourceSystem"),
      prop: "sourceSystemName",
      width: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.dbType"),
      prop: "dbType",
      dict: "datasource_type",
      width: 120,
    },
    {
      label: td("mc.task.structured.datasourceName"),
      prop: "datasourceName",
      width: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.scheduler"),
      prop: "scheduler",
      width: 150,
      formatter: (row) => getSchedulerLabel(row.scheduler),
    },
    // {
    //   label: td("mc.task.structured.collectionMode"),
    //   prop: "collectionMode",
    //   width: 100,
    //   dict: "mc_collect_mode",
    // },

    {
      label: td("mc.task.structured.taskStatus"),
      prop: "status",
      width: 100,
      slot: "task-status",
    },
    {
      label: td("mc.task.structured.schedulerStatus"),
      prop: "schedulerStatus",
      width: 120,
      slot: "scheduler-status",
    },
    {
      label: td("mc.task.structured.personCharge"),
      prop: "personChargeName",
      width: 140,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.lastExecuteTime"),
      prop: "lastExecuteTime",
      sortable: true,
      width: 180,
      date: true,
    },
    {
      label: td("common.texts.createdBy"),
      prop: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("common.texts.createdTime"),
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      width: 160,
      date: true,
    },
    {
      label: td("common.texts.operation"),
      width: 260,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listTask,
  params: {},
  events: {
    formatParams(params) {
      if (!params.time || !params.time.length) return params;
      const { time, ...other } = { ...params };
      other.createTimeStart = time[0];
      other.createTimeEnd = time[1];
      return other;
    },
  },
});

// search terms
const searchStore = reactive({
  items: [
    {
      label: td("mc.task.structured.taskName"),
      prop: "name",
      component: {
        is: "input",
      },
    },

    {
      label: td("common.texts.createdTime"),
      prop: "time",
      style: { width: "320px" },
      component: {
        is: "date-picker",
        type: "daterange",
        startPlaceholder: computed(() =>
          td("common.form.startDatePlaceholder")
        ),
        endPlaceholder: computed(() => td("common.form.endDatePlaceholder")),
      },
    },
  ],
});

// Add/modify pop-up window
const DEFAULT_FORM = {
  collectionMode: "1",
  collectionScope: "2",
  // Preserve DS as the default for legacy behavior and new tasks without a selection.
  scheduler: "QUARTZ",
  tables: [],
};
const dialog = reactive({
  open: false,
  title: "",
  loading: false,
  tableList: [],
  form: {
    ...DEFAULT_FORM,
  },
});

// Scheduling cycle pop-up window
const cronDialog = reactive({
  open: false,
  data: "",
});

// Get the source system path
const getDomainPath = computed(() => {
  return function (id) {
    let domainName = getParentLabelPath(store.sourceSystems, id, {
      idKey: "id",
      labelKey: "name",
      childrenKey: "children",
    });
    const idx = domainName.indexOf("/");
    return idx == -1 ? domainName : domainName.slice(idx + 1);
  };
});

// Get a list of data sources
function getDatasources() {
  listDaDatasource().then((res) => {
    res.data.rows.forEach((item) => {
      item.datasourceConfig = item.datasourceConfig
        ? JSON.parse(item.datasourceConfig)
        : {};
    });
    store.datasources = res.data.rows;
  });
}

// Search button action
function handleQueryClick() {
  tableRef.value?.getList();
}

// reset button action
function handleResetQueryClick() {
  if (sourceSystemTreeRef.value?.resetTree) {
    sourceSystemTreeRef.value.resetTree();
  }
  tableStore.params.sourceSystemId = null;
  tableStore.params.datasourceId = null;
  tableStore.params.id = null;
  tableRef.value?.resetQuery();
}

// Get user list
function getUserList() {
  deptUserTree().then((res) => {
    store.userList = res.data;
  });
}

// Switch user
function handleUserChange(id) {
  const data = store.userList.find((item) => item.userId === id);
  dialog.form.leaderPhone = data.phonenumber;
}

// Switch data source
function handleDatasourceChange(id, falg = true) {
  const data = store.datasources.find((item) => item.id === id);
  dialog.form.ip = data.ip;
  dialog.form.port = data.port;
  dialog.form.username = data.datasourceConfig?.username;
  dialog.form.dbType = data.datasourceType;
  if (falg) {
    dialog.form.tables = [];
  }
  getRealtimeMcTaskScopeList(id).then((res) => {
    dialog.tableList = res.data.map((item) => ({
      ...item,
      label: item.schemaName
        ? `${item.dbName}.${item.schemaName}`
        : item.dbName,
    }));
  });
}

// Switch source system
function handleDomainChange(id) {
  const data = store.flatSourceSystems.find((item) => item.id === id);
  if (data) {
    dialog.form.sourceSystemId = data.id;
    dialog.form.sourceSystemName = data.name;
  }
}
function handleRunClick(val) {
  if (runClickLoading.value) return;
  runClickLoading.value = true;
  runJobOnce({ id: val.id }).then((res) => {
    runClickLoading.value = false;
    if (res.code == 200) {
      ElMessage.success(td("mc.task.structured.executeSuccess"));
    }
  }).catch(() => {
    runClickLoading.value = false;
  });
}
// Open the scheduling cycle pop-up window
function handleOpenCronClick() {
  cronDialog.data = dialog.form.cronExpression;
  cronDialog.open = true;
}

// Close the scheduling cycle pop-up window
function handleCloseCronClick() {
  cronDialog.open = false;
  cronDialog.data = "";
}

// Confirm scheduling cycle pop-up window
function handleConfirmCronClick(data) {
  dialog.form.cronExpression = data;
  formRef.value?.validateField("cronExpression");
  cronDialog.open = false;
}

// Click for details
function handleDetailClick(row) {
  router.push({
    path: DETAIL_PATH,
    query: {
      id: row.id,
    },
  });
}

// Click to add
function handleAddClick() {
  dialog.title = td("mc.task.structured.addTask");
  // Restore the default when opening the create dialog to avoid an empty value left by the previous edit.
  dialog.form.scheduler = dialog.form.scheduler || "DOLPHINSCHEDULER";
  dialog.open = true;
  dialog.func = addTask;
}

// Cancel addition/modification
function handleCancelClick() {
  formRef.value.resetFields();
  showCronExpressionDescription.value = true;
  dialog.form = {
    ...DEFAULT_FORM,
  };
  dialog.tableList = [];
  dialog.loading = false;
  dialog.open = false;
}

// Confirm addition/modification
async function handleConfirmClick() {
  if (dialog.loading) return;
  dialog.loading = true;
  if(!await handleSchedulerChange()){
    return;
  }
  const valid = await formRef.value.validate();
  if (!valid) {
    dialog.loading = false;
    return;
  }
  const { tables, ...params } = dialog.form;
  if (params.collectionScope == "1") {
    params.scopeSaveReqVOS = dialog.tableList.filter((item) =>
      tables.includes(item.dbName)
    );
  }
  try {
    await dialog.func(params);
    handleCancelClick();
    tableRef.value.getList();
  } catch (err) {
    console.error(err);
  } finally {
    dialog.loading = false;
  }
}

// Open the modification pop-up window
function handleEditClick(row) {
  dialog.open = true;
  dialog.func = updateTask;
  dialog.title = td("mc.task.structured.editTask");
  getTask(row.id).then((res) => {
    if (res.data.scopeSaveReqVOS) {
      res.data.tables = res.data.scopeSaveReqVOS.map((item) => item.dbName);
    }
    // Make sure the source system name is included in the echo
    if (res.data.sourceSystemId && !res.data.sourceSystemName) {
      const system = store.flatSourceSystems.find(
        (item) => item.id === res.data.sourceSystemId
      );
      if (system) {
        res.data.sourceSystemName = system.name;
      }
    }
    dialog.form = {
      ...res.data,
      // Display DS for legacy tasks without a scheduler field.
      scheduler: res.data.scheduler || "DOLPHINSCHEDULER",
    };
    handleDatasourceChange(res.data.datasourceId, false);
  });
}

function getSchedulerLabel(value) {
  return (
    scheduler_type.find((item) => item.value == value)?.label ||
    value ||
    "-"
  );
}

// Delete
function handleDeleteClick(row) {
  ElMessageBox.confirm(
    td("mc.task.structured.confirmDelete", '', { id: row.id }),
    td("common.message.systemPrompt"),
    {
      confirmButtonText: td("common.button.confirm"),
      cancelButtonText: td("common.button.cancel"),
      type: "warning",
    }
  )
    .then(() => {
      return delTask(row.id);
    })
    .then(() => {
      ElMessage.success(td("common.message.deleteSuccess"));
      tableRef.value.getList();
    });
}

// Collection examples
function handleInstanceClick(row) {
  router.push({
    path: DETAIL_PATH,
    query: {
      id: row.id,
      tab: "CollectInstance",
    },
  });
}

// Delete selected row
function handleDeleteColumnClick() {
  if (!store.rows.length) return;
  const ids = store.rows.map((item) => item.id);
  store.loading = true;
  batchDeleteCheck(ids).then((res) => {
    const { canDeleteCount, cannotDeleteCount, canDeleteIds } = res.data;
    store.loading = false;
    ElMessageBox.confirm(
      td("mc.task.structured.confirmDeleteSelected", {
        count: canDeleteCount,
        notDeleteCount: cannotDeleteCount,
      }),
      td("common.message.systemPrompt"),
      {
        confirmButtonText: td("common.button.confirm"),
        cancelButtonText: td("common.button.cancel"),
        type: "warning",
      }
    )
      .then(() => {
        if (!canDeleteIds.length) {
          ElMessage.success(td("common.message.deleteSuccess"));
          return;
        }
        return delTask(canDeleteIds);
      })
      .then((res) => {
        if (!res) return;
        ElMessage.success(td("common.message.deleteSuccess"));
        tableRef.value.getList();
      });
  });
}

// filter table
function onFilterTransfer(value, item) {
  if (!value) return item;
  const txt = (item.label || item.dbName || "").toLowerCase();
  return txt.includes(value.toLowerCase());
}

// Switch task status
function handleTaskStatusChange(row, status) {
  const action =
    status == 1
      ? td("mc.task.structured.publish")
      : td("mc.task.structured.cancelPublish");
  ElMessageBox.confirm(
    td("mc.task.structured.confirmPublish", '',{
      action,
      name: row.name,
    }),
    td("common.message.systemPrompt"),
    {
      confirmButtonText: td("common.button.confirm"),
      cancelButtonText: td("common.button.cancel"),
      type: "warning",
    }
  )
    .then(() => {
      return updateReleaseJobTask({
        id: row.id,
        status,
      });
    })
    .then(() => {
      ElMessage.success(
        td("mc.task.structured.publishSuccess",'', {
          name: row.name,
          action,
        })
      );
      row.status = status;
    })
    .catch(() => {
      row.status = status == "1" ? "0" : "1";
    });
}

// Switch scheduling status
function handleSchedulerStatusChange(row, status) {
  const action =
    status == 1
      ? td("mc.task.structured.online")
      : td("mc.task.structured.offline");
  ElMessageBox.confirm(
    td("mc.task.structured.confirmScheduler", '',{
      action,
      name: row.name,
    }),
    td("common.message.systemPrompt"),
    {
      confirmButtonText: td("common.button.confirm"),
      cancelButtonText: td("common.button.cancel"),
      type: "warning",
    }
  )
    .then(() => {
      return updateReleaseSchedule({
        id: row.id,
        status,
      });
    })
    .then(() => {
      ElMessage.success(
        td("mc.task.structured.schedulerSuccess",'', {
          name: row.name,
          action,
        })
      );
      row.schedulerStatus = status;
    })
    .catch(() => {
      row.schedulerStatus = status == "1" ? "0" : "1";
    });
}

/**
 * Checks DolphinScheduler status.
 * @returns {Promise<void>}
 */
const handleSchedulerChange = async () => {
  if (dialog.form.scheduler !== "QUARTZ") {
    const resp = await checkApi();
    if (!resp.data) {
      proxy.$modal.msgWarning(td("dpp.integratioTask.upDs", "Please start the DolphinScheduler scheduler！"));
    }
    return resp.data;
  }
  return true;
};

getDatasources();
getUserList();
getAllSourceSystems();
</script>

<style lang="scss" scoped>
.form-item-description {
  display: flex;
  align-items: center;
  color: #888;
  font-size: 12px;
  line-height: 1.5;
  margin: 0;
}

.cron-expression-form-item :deep(.el-form-item__content) {
  padding-bottom: 18px;
}

.cron-expression-description,
.cron-expression-form-item :deep(.el-form-item__error) {
  position: absolute;
  top: auto;
  bottom: 0;
  left: 0;
  box-sizing: border-box;
  height: 18px;
  padding-top: 0;
  line-height: 18px;
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

:deep(.engine-card-group .option-card.el-radio),
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

.engine-card__icon {
  width: 36px;
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
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    display: inline-block;
    text-align: center;
    height: 20px;
    line-height: 20px;
  }
}

.option-card__name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  height: 20px;
  font-family: PingFangSC, PingFang SC;
  font-weight: 400;
  font-size: 14px;
  color: var(--color-third-party-Langfuse);
  line-height: 20px;
  text-align: left;
  font-style: normal;
  margin-right: 8px;
  max-width: calc(100% - 60px);
}

.option-card__description {
  display: -webkit-box;
  overflow: hidden;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  width: 108%;
  font-family: PingFangSC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  color: rgba(0,0,0,0.65);
  line-height: 18px;
  text-align: left;
  font-style: normal;
}

:deep(.option-card.el-radio.is-disabled) {
  .option-card__icon,
  .option-card__content {
    opacity: 0.55;
  }
}

.engine-config-grid {
  margin-top: 2px;

  :deep(.el-form-item) {
    margin-bottom: 12px;
  }
}

</style>
