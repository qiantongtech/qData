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
    <GuideTip tip-id="dpp/tasker/dppEtlTask.list" />
    <el-container>
      <DeptTree
        :api="api"
        :editable="true"
        :leftWidth="leftWidth"
        :placeholder="
          td('dpp.integratioTask.inputCategoryName', 'Please enter data integration category name')
        "
        ref="DeptTreeRef"
        :title="
          td('dpp.integratioTask.dataIntegrationCategory', 'Data Integration Category')
        "
        @node-click="handleNodeClick"
        :extraParams="{
          projectCode: userStore.projectCode,
          projectId: userStore.projectId,
        }"
        :headerOffset="statsCardHeight"
      />
      <el-main class="main-content">
        <StatsCardContainer
          ref="statsCardContainerRef"
          :cards="statsCards"
          :selectedIndex="selectedStatsIndex"
          :showPanel="showRunningTasks"
          :statsTime="statsTime"
          @cardClick="toggleRunningTasks"
          @refresh="loadStatistics"
        />

        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQuery"
              @reset="resetQuery"
            />
          </template>
          <template #actions-data>
            <el-button
              type="primary"
              plain
              @click="openTaskConfigDialog('QUARTZ')"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>
              {{ td("dpp.integratioTask.dataxAdd", "新增DATAX任务") }}
            </el-button>
            <el-button
              type="primary"
              plain
              @click="openTaskConfigDialog('DOLPHINSCHEDULER')"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>
              {{ td("dpp.integratioTask.sparkAdd", "新增SPARK任务") }}
            </el-button>
          </template>

          <qt-table v-bind="tableStore" ref="tableRef">
            <template #name="{ row }">
              <div class="name-label task-title">
                <div
                  class="justify task-title-row"
                  @click="
                    routeTo('/dpp/task/integratioTask/detail', {
                      ...row,
                      info: true,
                    })
                  "
                >
                  <img
                    :src="getDatasourceIcon(row.draftJson)"
                    alt=""
                    class="datasource-icon"
                    v-if="getDatasourceIcon(row.draftJson)"
                  />
                  <el-link
                    type="primary"
                    :underline="false"
                    class="task-name-text task-name-ellipsis"
                    :title="row.name"
                  >
                    {{ row.name || "-" }}
                  </el-link>
                  <el-tag
                    type="primary"
                    :underline="false"
                    class="task-cat-ellipsis"
                    :title="row.catName"
                  >
                    {{ row.catName || "-" }}
                  </el-tag>
                </div>
                <div class="text-ellipsis desc-text" :title="row.description">
                  {{ row.description || "-" }}
                </div>
              </div>
            </template>
            <template #releaseState="{ row }">
              <div class="flex-column fz12">
                <div class="flex-center">
                  <span class="black-label mr5">{{
                      td("dpp.integratioTask.taskStatus", "Task Status")
                    }}:</span>
                  <el-switch
                    v-model="row.status"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="1"
                    :inactive-value="getStatus(row.status)"
                    @change="handleStatusChange(row.id, row)"
                    :disabled="row.status == '-1'"
                  />
                </div>
                <div class="flex-center">
                  <span class="black-label mr5">{{
                      td("dpp.integratioTask.scheduleStatus", "Schedule Status")
                    }}:</span>
                  <el-switch
                    v-model="row.schedulerState"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="1"
                    inactive-value="0"
                    :disabled="row.status != '1'"
                    @change="handleschedulerState(row.id, row)"
                  />
                </div>
              </div>
            </template>
            <template #cronExpression="{ row }">
              <div class="flex-column fz14 grey-black-text">
                <div class="flex-center mb5">
                  <el-icon class="mr5"><Clock /></el-icon>
                  <span
                    class="text-ellipsis cron-text"
                    :title="cronToZh(row.cronExpression)"
                  >
                    {{ cronToZh(row.cronExpression) || "-" }}
                  </span>
                </div>
                <div class="flex-center">
                  <span class="mr5">{{ td("dpp.integratioTask.executionStrategy") }}:</span>
                  <dict-tag
                    :options="dpp_etl_task_execution_type"
                    :value="row.executionType"
                  />
                </div>
                <div class="flex-center mt5 scheduler-info-row">
                  <span class="mr5 scheduler-info-label">{{
                    td("dpp.integratioTask.scheduler", "Scheduler")
                  }}:</span>
                  <span
                    class="text-ellipsis cron-text scheduler-info-value"
                    :title="`${getSchedulerLabel(row.scheduler)}`"
                  >
                    {{ getSchedulerLabel(row.scheduler) }}
                  </span>
                </div>
              </div>
            </template>
            <template #lastExecute="{ row }">
              <StatusTag
                size="small"
                :status="row.currentStatus"
                @click="openTaskLogDialog(row)"
              />
              <div class="last-execute-info">
                <div class="last-execute-info__row">
                  <span class="last-execute-info__value">
                    {{ row.lastExecuteTime ? timeAgo(row.lastExecuteTime) : "-" }}
                  </span>
                  <span class="ml10 last-execute-info__value">
                    {{ row.duration || "-" }}
                  </span>
                </div>
              </div>
            </template>
            <template #personChargeName="{ row }">
              <div class="flex-column fz14">
                <span
                  class="text-ellipsis person-charge-ellipsis"
                  :title="row.personCharge"
                  >{{ row.personChargeName || "-" }}</span>
                <span>{{ row.contactNumber || "-" }}</span>
              </div>
            </template>
            <template #createBy="{ row }">
              <div class="flex-column fz14">
                <span
                  class="text-ellipsis person-charge-ellipsis"
                  :title="row.personCharge"
                  >{{ row.createBy || "-" }}</span>
                <span>{{ row.createUserContactNumber || "-" }}</span>
              </div>
            </template>
            <template #executionType="{ row }">
              <dict-tag
                :options="dpp_etl_task_execution_type"
                :value="row.executionType"
              />
            </template>
            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                :disabled="row.status == 1"
                @click="routeTo('/dpp/task/integratioTask/edit', row)"
                >{{
                  td("dpp.integratioTask.configureTask", "Configure Task")
                }}</el-button>
              <el-button
                link
                type="primary"
                icon="view"
                @click="
                  routeTo('/dpp/task/integratioTask/detail', {
                    ...row,
                    info: true,
                  })
                "
                >{{ td("common.button.details", "Details") }}</el-button>
              <el-popover placement="bottom" :width="150" trigger="click">
                <template #reference>
                  <el-button link type="primary" icon="ArrowDown">{{
                    td("common.button.more", "More")
                  }}</el-button>
                </template>
                <div style="width: 100px" class="butgdlist">
                  <el-button
                    link
                    style="padding-left: 14px"
                    type="primary"
                    icon="Operation"
                    @click="handleJobLog(row)"
                    :disabled="row.schedulerState == '1'"
                    >{{
                      td("dpp.integratioTask.scheduleCycle", "Schedule Cycle")
                    }}</el-button>
                  <el-button
                    link
                    type="primary"
                    icon="Stopwatch"
                    @click="handleDataView(row)"
                    >{{
                      td("dpp.integratioTask.runInstance", "Run Instance")
                    }}</el-button>
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    :disabled="row.status != 1"
                    :loading="executeLoading[row.id]"
                    @click="handleExecuteOnce(row)"
                    >{{
                      td("dpp.integratioTask.executeOnce", "Execute Once")
                    }}</el-button>
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="row.status == 1"
                    :loading="deleteLoading[row.id]"
                    @click="handleDelete(row)"
                    >{{ td("common.button.delete", "Delete") }}</el-button>
                  <el-button
                    link
                    type="primary"
                    icon="CopyDocument"
                    :disabled="row.status == 1"
                    @click="handleClone(row)"
                  >
                    {{ td("dpp.cleanRule.clone", "Clone") }}
                  </el-button>
                </div>
              </el-popover>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>
    <RunningTasksPanel
      :visible="showRunningTasks"
      :title="currentPanelTitle"
      :tasks="runningTasks"
      :loading="runningTasksQuery.loading"
      :total="runningTasksQuery.total"
      @update:visible="showRunningTasks = $event"
      @viewRealTimeLog="viewRealTimeLog"
      @viewInstance="viewInstance"
      @taskClick="openTaskDetail"
      @loadMore="loadMoreTasks"
      @refresh="refreshRunningTasks"
    />
    <instance
      :visible="DataView"
      :taskType="1"
      @update:visible="DataView = $event"
      @confirm="submitForm"
      :data="form"
      :title="td('dpp.integratioTask.runInstance', 'Run Instance')"
    />
    <TaskLogDialog ref="taskLogDialogRef" />
    <el-dialog
      :title="td('dpp.integratioTask.scheduleCycle', '调度信息')"
      v-model="openCron"
      :append-to="$refs['app-container']"
      destroy-on-close
      :appendTo="'#app'"
    >
      <crontab
        ref="crontabRef"
        @hide="openCron = false"
        @fill="crontabFill"
        :expression="expression"
      >
      </crontab>
    </el-dialog>
    <!-- New -->
    <add
      :visible="taskConfigDialogVisible"
      :title="td('dpp.cleanRule.addTask', 'Add Task')"
      @update:visible="taskConfigDialogVisible = $event"
      @save="handleSave"
      @confirm="handleConfirm"
      :data="nodeData"
      :userList="userList"
      :info="route.query.info"
      :catCode="tableStore.params.catCode"
      :deptOptions="deptOptions"
      :defaultScheduler="selectedScheduler"
    />
  </div>
</template>

<script setup name="DppIntegratioTask">
import useDefaultLang from "@/composables/useDefaultLang";
import {
  listDppEtlTask,
  delDppEtlTask,
  updateReleaseSchedule,
  updateReleaseJobTask,
  releaseTaskCrontab,
  startDppEtlTask,
  createEtlTaskFront,
  copyCreateEtl,
  getEtlTaskStatistics,
  getEtlTaskInstanceList,
} from "@/api/dpp/task/index.js";
import { usePageRefresh } from "@/composables/usePageRefresh";
import { cronToZh } from "@/utils/cronUtils";
import { timeAgo } from "@/utils/time";
import Crontab from "@/components/Crontab/index.vue";
import instance from "@/views/dpp/components/logs/instance.vue";
import TaskLogDialog from "@/views/dpp/components/logs/taskLog.vue";
import { useRoute, useRouter } from "vue-router";
import useUserStore from "@/store/system/user";
import {
  listAttTaskCat,
  getAttTaskCat,
  addAttTaskCat,
  updateAttTaskCat,
  delAttTaskCat,
} from "@/api/att/cat/taskCat/taskCat";
import DeptTree from "@/components/DeptTree";
import add from "./add/add.vue";
import { deptUserTree } from "@/api/system/system/user.js";
import RunningTasksPanel from "@/views/dpp/components/logs/RunningTasksPanel.vue";
import StatsCardContainer from "@/views/dpp/components/logs/StatsCardContainer.vue";
import StatusTag from "@/views/dpp/components/logs/StatusTag.vue";
import {
  ref,
  reactive,
  computed,
  getCurrentInstance,
  watch,
  onMounted,
  onUnmounted,
  nextTick,
} from "vue";

const userStore = useUserStore();
const { td, locale } = useDefaultLang();
const { proxy } = getCurrentInstance();
const statsCardContainerRef = ref(null);
const statsCardHeight = ref(0);
const executeLoading = reactive({});
const deleteLoading = reactive({});

const api = {
  list: listAttTaskCat,
  get: getAttTaskCat,
  add: addAttTaskCat,
  update: updateAttTaskCat,
  del: delAttTaskCat,
};
const {
  dpp_etl_task_status,
  dpp_etl_task_execution_type,
  dpp_task_current_status,
} = proxy.useDict(
  "dpp_etl_task_status",
  "dpp_etl_task_execution_type",
  "dpp_task_current_status"
);

const scheduler_type = [
  { label: "Quartz", value: "QUARTZ" },
  { label: "DolphinScheduler", value: "DOLPHINSCHEDULER" },
];

// Get scheduler enum values.
const getSchedulerLabel = (value) => {
  // Convert scheduler enum values into user-friendly names in the list.
  return scheduler_type.find((item) => item.value == value)?.label || value || "-";
};

const route = useRoute();
const router = useRouter();

// Table and Search Store
const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
    },
  },
  columns: [
    {
      label: td("common.texts.number", "No."),
      prop: "id",
      width: 60,
      sortable: true,
    },
    {
      label: td("dpp.integratioTask.taskInfo", "Task Info"),
      prop: "name",
      align: "left",
      slot: "name",
      width: 300,
    },
    {
      label: td("dpp.integratioTask.runControl", "Run Control"),
      prop: "status",
      width: 190,
      slot: "releaseState",
      align: "left",
    },
    {
      label: td("dpp.integratioTask.dispatchInformation", "调度信息"),
      prop: "cronExpression",
      width: 260,
      slot: "cronExpression",
      align: "left",
    },
    {
      label: td("dpp.integratioTask.recentExecution", "Recent Execution"),
      width: 160,
      slot: "lastExecute",
      align: "left",
    },

    {
      label: td("dpp.integratioTask.personCharge", "Responsible Person"),
      width: 120,
      slot: "personChargeName",
      align: "left",
    },
    {
      label: td("common.texts.createdBy", "Created By"),
      slot: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: true,
    },
    {
      label: td("common.texts.createdTime", "Created Time"),
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      date: true,
      width: 150,
      align: "left",
    },

    {
      label: td("common.texts.operation", "Operation"),
      align: "center",
      fixed: "right",
      slot: "action",
      width: 300,
    },
  ],
  func: listWrapper,
  params: {
    catCode: null,
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    currentStatus: null,
    executeTime: [],
  },
});

// User list for search
let userList = ref([]);
let deptOptions = ref([]);
function getDeptTree() {
  listAttTaskCat({
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    validFlag: true,
  }).then((response) => {
    deptOptions.value = [];
    let children = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td("dpp.integratioTask.dataIntegrationCategory", "Data Integration Category"),
        value: "",
        id: 0,
        children: children,
      },
    ];
  });
  deptUserTree().then((res) => {
    userList.value = res.data;
  });
}

const searchStore = reactive({
  items: [
    {
      label: td("dpp.integratioTask.taskName", "Task Name"),
      prop: "name",
      align: "left",
      component: {
        is: "input",
        placeholder: td("dpp.integratioTask.inputTaskName", "Please enter task name"),
      },
    },
    {
      label: td("dpp.integratioTask.taskStatus", "Task Status"),
      prop: "status",
      component: {
        is: "select",
        placeholder: td(
          "dpp.integratioTask.selectTaskStatus",
          "Please select task status"
        ),
        options: dpp_etl_task_status,
      },
    },
    {
      label: td("dpp.integratioTask.executeStatus", "Execution Status"),
      prop: "currentStatus",
      component: {
        is: "select",
        options: dpp_task_current_status,
        placeholder: td(
          "dpp.integratioTask.selectExecuteStatus",
          "Please select execution status"
        ),
      },
    },
    {
      label: td("dpp.integratioTask.executeTime", "Execution Time"),
      prop: "executeTime",
      style: { width: "320px" },
      component: {
        is: "date-picker",
        type: "daterange",
        startPlaceholder: td("common.date.startDate", "Start date"),
        endPlaceholder: td("common.date.endDate", "End date"),
      },
    },
    {
      label: td("dpp.integratioTask.personCharge", "Responsible Person"),
      prop: "personCharge",
      component: {
        is: "tree-select",
        data: userList,
        props: { value: "userId", label: "nickName", children: "children" },
        valueKey: "ID",
        placeholder: td(
          "dpp.integratioTask.selectPersonCharge",
          "Please select responsible person"
        ),
        checkStrictly: true,
      },
    },
  ],
});

function listWrapper(params) {
  const p = { ...params };
  p.projectId = userStore.projectId;
  p.projectCode = userStore.projectCode;
  if (p.executeTime && p.executeTime.length === 2) {
    p.startTime = `${p.executeTime[0]} 00:00:00`;
    p.endTime = `${p.executeTime[1]} 23:59:59`;
  }
  delete p.executeTime;
  return listDppEtlTask(p);
}

function handleQuery() {
  tableRef.value.getList();
  loadStatistics();
}

function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = "";
  tableStore.params.currentStatus = null;
  tableStore.params.executeTime = [];
  handleQuery();
}

// department tree
const leftWidth = ref(300);
const DeptTreeRef = ref(null);
function handleNodeClick(data) {
  tableStore.params.catCode = data.code;
  handleQuery();
}

import runningTaskIcon from "@/assets/dpp/etl/instance/running-task.svg";
import todayErrorTaskIcon from "@/assets/dpp/etl/instance/today-error-task.svg";
import todayExecutionIcon from "@/assets/dpp/etl/instance/today-execution.svg";
import todaySuccessRateIcon from "@/assets/dpp/etl/instance/today-success-rate.svg";

const statsCards = ref([
  {
    name: td("dpp.integratioTask.runningTasks", "Running Tasks"),
    value: 0,
    unit: td("dpp.integratioTask.taskUnit", "次"),
    iconClass: "icon-blue",
    iconSrc: runningTaskIcon,
    type: "running",
    tip: td(
      "dpp.integratioTask.runningTasksTip",
      "Number of tasks with at least one running instance"
    ),
  },
  {
    name: td("dpp.integratioTask.todayErrorTasks", "Today Error Tasks"),
    value: 0,
    unit: td("dpp.integratioTask.taskUnit", "次"),
    iconClass: "icon-orange",
    iconSrc: todayErrorTaskIcon,
    type: "failed",
    tip: td(
      "dpp.integratioTask.todayErrorTasksTip",
      "Number of tasks whose latest execution failed"
    ),
  },
  {
    name: td("dpp.integratioTask.todayExecutions", "Today Executions"),
    value: 0,
    unit: td("dpp.integratioTask.taskUnit", "次"),
    iconClass: "icon-blue",
    iconSrc: todayExecutionIcon,
    type: "",
    tip: td(
      "dpp.integratioTask.todayExecutionsTip",
      "Number of execution instances created today"
    ),
  },
  {
    name: td("dpp.integratioTask.todaySuccessRate", "Today Success Rate"),
    value: 0,
    unit: "%",
    iconClass: "icon-green",
    iconSrc: todaySuccessRateIcon,
    type: "success",
    tip: td(
      "dpp.integratioTask.todaySuccessRateTip",
      "Successful completed instances divided by all completed instances today"
    ),
  },
]);

function syncPageI18n() {
  const columnLabels = [
    td("common.texts.number", "No."),
    td("dpp.integratioTask.taskInfo", "Task Info"),
    td("dpp.integratioTask.runControl", "Run Control"),
    td("dpp.integratioTask.dispatchInformation", "Scheduling Information"),
    td("dpp.integratioTask.recentExecution", "Recent Execution"),
    td("dpp.integratioTask.personCharge", "Responsible Person"),
    td("common.texts.createdBy", "Created By"),
    td("common.texts.createdTime", "Created Time"),
    td("common.texts.operation", "Operation"),
  ];
  tableStore.columns.forEach((column, index) => {
    column.label = columnLabels[index];
  });

  searchStore.items[0].label = td("dpp.integratioTask.taskName", "Task Name");
  searchStore.items[0].component.placeholder = td(
    "dpp.integratioTask.inputTaskName",
    "Please enter task name"
  );
  searchStore.items[1].label = td("dpp.integratioTask.taskStatus", "Task Status");
  searchStore.items[1].component.placeholder = td(
    "dpp.integratioTask.selectTaskStatus",
    "Please select task status"
  );
  searchStore.items[2].label = td("dpp.integratioTask.executeStatus", "Execution Status");
  searchStore.items[2].component.placeholder = td(
    "dpp.integratioTask.selectExecuteStatus",
    "Please select execution status"
  );
  searchStore.items[3].label = td("dpp.integratioTask.executeTime", "Execution Time");
  searchStore.items[3].component.startPlaceholder = td("common.date.startDate", "Start date");
  searchStore.items[3].component.endPlaceholder = td("common.date.endDate", "End date");
  searchStore.items[4].label = td("dpp.integratioTask.personCharge", "Responsible Person");
  searchStore.items[4].component.placeholder = td(
    "dpp.integratioTask.selectPersonCharge",
    "Please select responsible person"
  );

  const cardTexts = [
    ["runningTasks", "Running Tasks", "runningTasksTip", "Number of tasks with at least one running instance"],
    ["todayErrorTasks", "Today Error Tasks", "todayErrorTasksTip", "Number of tasks whose latest execution failed"],
    ["todayExecutions", "Today Executions", "todayExecutionsTip", "Number of execution instances created today"],
    ["todaySuccessRate", "Today Success Rate", "todaySuccessRateTip", "Successful completed instances divided by all completed instances today"],
  ];
  statsCards.value.forEach((card, index) => {
    const [nameKey, nameFallback, tipKey, tipFallback] = cardTexts[index];
    card.name = td(`dpp.integratioTask.${nameKey}`, nameFallback);
    card.tip = td(`dpp.integratioTask.${tipKey}`, tipFallback);
    if (index < 3) card.unit = td("dpp.integratioTask.taskUnit", "次");
  });
}

watch(locale, syncPageI18n);
const statsTime = ref("");
const showRunningTasks = ref(false);
const selectedStatsIndex = ref(-1);
const runningTasks = ref([]);
const runningTasksQuery = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  status: "running",
  loading: false,
});

const noMoreTasks = computed(
  () =>
    runningTasks.value.length >= runningTasksQuery.total &&
    runningTasksQuery.total > 0
);

async function toggleRunningTasks(index) {
  selectedStatsIndex.value = index;
  showRunningTasks.value = false;
  const status = statsCards.value[index].type;
  tableStore.params.currentStatus = status;
  if (status === "running") {
    tableStore.params.executeTime = [];
  } else {
    const today = proxy.parseTime(new Date(), "{y}-{m}-{d}");
    tableStore.params.executeTime = [today, today];
  }
  handleQuery();
  await loadRunningTasks(index);
}

function openTaskDetail(task) {
  handleDataView(task);
}

function viewRealTimeLog(task) {
  taskLogDialogRef.value?.open(task.id);
}

function viewInstance(task) {
  handleDataView(task);
}

function loadMoreTasks() {
  if (noMoreTasks.value || runningTasksQuery.loading) return;
  loadRunningTasks(selectedStatsIndex.value, true);
}

function refreshRunningTasks() {
  loadRunningTasks(selectedStatsIndex.value);
}

const currentPanelTitle = computed(() => {
  if (!showRunningTasks.value || selectedStatsIndex.value < 0) return "";
  return statsCards.value[selectedStatsIndex.value]?.name || "";
});

async function loadStatistics() {
  try {
    const res = await getEtlTaskStatistics({
      projectId: userStore.projectId,
      projectCode: userStore.projectCode,
    });
    if (Number(res?.code) === 200) {
      const data = res.data || {};
      statsCards.value[0].value = data.runningCount || 0;
      statsCards.value[1].value = data.todayErrorCount || 0;
      statsCards.value[2].value = data.todayExecuteCount || 0;
      statsCards.value[3].value = data.todaySuccessRate || 0;
      statsTime.value = proxy.parseTime(new Date(), "{y}-{m}-{d} {h}:{i}:{s}");
    }
  } catch (error) {
    console.error("Failed to load integration task statistics", error);
  }
}

async function loadRunningTasks(index = 0, isLoadMore = false) {
  if (runningTasksQuery.loading) return;
  runningTasksQuery.loading = true;
  if (!isLoadMore) {
    runningTasksQuery.pageNum = 1;
    runningTasks.value = [];
    runningTasksQuery.total = 0;
    runningTasksQuery.status = index === 0 ? "running" : index === 1 ? "6" : index === 3 ? "7" : null;
  } else {
    runningTasksQuery.pageNum += 1;
  }
  try {
    const res = await getEtlTaskInstanceList({
      pageNum: runningTasksQuery.pageNum,
      pageSize: runningTasksQuery.pageSize,
      taskType: "1",
      status: runningTasksQuery.status,
      projectId: userStore.projectId,
      projectCode: userStore.projectCode,
    });
    if (Number(res?.code) === 200) {
      const newTasks = res.data?.rows || [];
      runningTasks.value = isLoadMore
        ? [...runningTasks.value, ...newTasks]
        : newTasks;
      runningTasksQuery.total = res.data?.total || 0;
    }
  } catch (error) {
    if (isLoadMore) runningTasksQuery.pageNum -= 1;
    console.error("Failed to load integration task instances", error);
  } finally {
    runningTasksQuery.loading = false;
  }
}

// Task configuration
const taskConfigDialogVisible = ref(false);
const selectedScheduler = ref("");
let nodeData = ref({ taskConfig: {}, name: null });

const openTaskConfigDialog = (scheduler) => {
  selectedScheduler.value = scheduler || scheduler_type.value[0]?.value;
  nodeData.value = { taskConfig: {}, name: null };
  taskConfigDialogVisible.value = true;
};

const handleSave = (form) => {
  const parms = {
    ...form,
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    draftJson: JSON.stringify({ ...form }),
  };
  createEtlTaskFront(parms).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess(td("common.message.msgOpSuccess", "Operation successful"));
      handleQuery();
    }
  });
};

const handleConfirm = (form) => {
  const parms = {
    ...form,
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    draftJson: JSON.stringify({ ...form }),
  };
  createEtlTaskFront(parms).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess(td("common.message.msgOpSuccess", "Operation successful"));
      handleQuery();
      routeTo("/dpp/task/integratioTask/edit", res.data);
    }
  });
};

// Actions
function handleStatusChange(id, row) {
  const text =
    row.status == "1"
      ? td("dpp.integratioTask.online", "Online")
      : td("dpp.integratioTask.offline", "Offline");
  proxy.$modal
    .confirm(
      td(
        "dpp.integratioTask.confirmTaskStatus",
        'Are you sure to "{action}" data integration task "{name}"?',
        { action: text, name: row.name }
      )
    )
    .then(function () {
      updateReleaseJobTask({
        id,
        releaseState: row.status,
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
      })
        .then(() => {
          proxy.$modal.msgSuccess(
            td("common.message.msgOpSuccess", "Operation successful")
          );
          handleQuery();
        })
        .catch(() => {
          row.status = row.status === "1" ? "0" : "1";
        });
    })
    .catch(() => {
      row.status = row.status === "1" ? "0" : "1";
    });
}

function handleschedulerState(id, row) {
  const text =
    row.schedulerState == "1"
      ? td("dpp.integratioTask.online", "Online")
      : td("dpp.integratioTask.offline", "Offline");
  proxy.$modal
    .confirm(
      td(
        "dpp.integratioTask.confirmScheduleStatus",
        'Are you sure to "{action}" data integration schedule status "{name}"?',
        { action: text, name: row.name }
      )
    )
    .then(function () {
      updateReleaseSchedule({
        id,
        schedulerState: row.schedulerState,
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
      })
        .then(() => {
          proxy.$modal.msgSuccess(
            td("common.message.msgOpSuccess", "Operation successful")
          );
          handleQuery();
        })
        .catch(() => {
          row.schedulerState = row.schedulerState == "1" ? "0" : "1";
        });
    })
    .catch(() => {
      row.schedulerState = row.schedulerState == "1" ? "0" : "1";
    });
}

function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id: row.id,
          info: row.info,
        },
      });
    }
  }
}

// Cron and Instance
const openCron = ref(false);
const expression = ref("");
const row = ref({});

function handleJobLog(data) {
  row.value = data || {};
  expression.value = data.cronExpression || "";
  openCron.value = true;
}

function crontabFill(value) {
  row.value.crontab = value;
  releaseTaskCrontab({
    crontab: row.value.crontab,
    projectCode: userStore.projectCode,
    projectId: userStore.projectId,
    id: row.value.id,
  }).then(() => {
    proxy.$modal.msgSuccess(td("common.message.msgOpSuccess", "Operation successful"));
    handleQuery();
  });
}

const DataView = ref(false);
const form = ref({});
const taskLogDialogRef = ref(null);
function handleDataView(row) {
  form.value = row;
  DataView.value = true;
}

function submitForm() {
  handleQuery();
}

const handleExecuteOnce = async (row) => {
  if (executeLoading[row.id]) return;
  if (!row?.id) {
    proxy.$modal.msgWarning(
      td("dpp.integratioTask.invalidTaskId", "Invalid task ID, please refresh and retry")
    );
    return;
  }
  executeLoading[row.id] = true;
  try {
    const res = await startDppEtlTask(row.id);
    if (Number(res?.code) === 200) {
      proxy.$modal.msgSuccess(
        td("dpp.integratioTask.executeSuccess", "Executed successfully")
      );
      handleQuery();
    } else {
      proxy.$modal.msgWarning(
        res?.msg ||
          td("dpp.integratioTask.executeFailed", "Execution failed, please contact administrator")
      );
    }
  } catch {
    //
  } finally {
    executeLoading[row.id] = false;
  }
};

function openTaskLogDialog(row) {
  if (!row?.taskInstanceId) {
    proxy.$modal.msgWarning(
      td("dpp.integratioTask.noExecutionLog", "暂无最近执行日志")
    );
    return;
  }
  taskLogDialogRef.value?.open(row.taskInstanceId);
}

const handleClone = (row) => {
  proxy.$modal
    .confirm(
      td(
        "dpp.integratioTask.confirmClone",
        'Are you sure to clone task "{name}"?',
        { name: row.name }
      )
    )
    .then(() => {
      return copyCreateEtl({
        id: Number(row.id),
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
      });
    })
    .then(() => {
      handleQuery();
    })
    .catch(() => {});
};

function handleDelete(row) {
  const ids = row.id;
  proxy.$modal
    .confirm(
      td(
        "dpp.integratioTask.confirmDelete",
        'Are you sure to delete data integration task with ID "{ids}"?',
        { ids }
      )
    )
    .then(function () {
      deleteLoading[row.id] = true;
      return delDppEtlTask(ids);
    })
    .then(() => {
      handleQuery();
      proxy.$modal.msgSuccess(td("common.message.deleteSuccess", "Deleted successfully"));
    })
    .catch(() => {})
    .finally(() => {
      deleteLoading[row.id] = false;
    });
}

// Utils
const getDatasourceIcon = (json) => {
  let type = json && JSON.parse(json).taskType;
  switch (type) {
    case "FLINK":
      return new URL("@/assets/images/common/icon-flink-one.svg", import.meta.url).href;
    case "SPARK":
      return new URL("@/assets/images/common/icon-spark-one.svg", import.meta.url).href;
    case "DATAX":
      return new URL("@/assets/images/common/img-datax.png", import.meta.url).href;
    default:
      return null;
  }
};

const getStatus = (status) => {
  if (status == "-1") {
    return "-1";
  } else {
    return "0";
  }
};

const updateStatsCardHeight = () => {
  const element = statsCardContainerRef.value?.$el;
  statsCardHeight.value = element ? element.offsetHeight + 20 : 0;
};

let resizeObserver = null;

async function initializePageData() {
  if (!userStore.projectId) return;
  getDeptTree();
  handleQuery();
}

onMounted(async () => {
  await initializePageData();
  await nextTick();
  const element = statsCardContainerRef.value?.$el;
  if (element && typeof ResizeObserver !== "undefined") {
    resizeObserver = new ResizeObserver(updateStatsCardHeight);
    resizeObserver.observe(element);
  }
  updateStatsCardHeight();
});

onUnmounted(() => {
  resizeObserver?.disconnect();
});

watch(
  () => userStore.projectId,
  async (newProjectId, oldProjectId) => {
    if (newProjectId && newProjectId !== oldProjectId) {
      showRunningTasks.value = false;
      selectedStatsIndex.value = -1;
      await initializePageData();
    }
  }
);

usePageRefresh("integratioTask", initializePageData);
</script>

<style lang="scss" src="@/assets/styles/system/table-style-optimized.scss"></style>

<style scoped lang="scss">
.task-action-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 auto;
}

.task-action-list :deep(.el-button) {
  width: 100%;
  justify-content: center;
  margin-left: 0;
}

.last-execute-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-top: 6px;
  font-size: 14px;
  color: #1e293b;
}

.last-execute-info__row {
  display: flex;
  align-items: center;
  min-width: 0;
}

.last-execute-info__value {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
