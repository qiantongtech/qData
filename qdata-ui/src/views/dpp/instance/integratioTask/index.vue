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
  <div ref="appContainerRef" class="app-container">
    <el-container>
      <DeptTree
        ref="deptTreeRef"
        :dept-options="deptOptions"
        :left-width="leftWidth"
        :placeholder="
          td(
            'dpp.instance.integratioTask.inputCategoryName',
            'Please enter data integration category name'
          )
        "
        @node-click="handleNodeClick"
      />

      <el-main class="main-content">
        <StatsCardContainer
          :cards="statsCards"
          :selected-index="selectedStatsIndex"
          :show-panel="false"
          :stats-time="statsTime"
          :loading="statisticsLoading"
          @card-click="handleStatsCardClick"
          @refresh="loadStatistics"
        />

        <qt-wrap :columns="tableStore.columns" :table-ref="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQuery"
              @reset="resetQuery"
            />
          </template>

          <qt-table v-bind="tableStore" ref="tableRef">
            <template #status="{ row }">
              <StatusTag
                class="log-status"
                :status="row.currentStatus"
                @click="openTaskLog(row)"
              />
            </template>

            <template #executeTime="{ row }">
              <div class="execute-time-range">
                <span>{{ formatDateTime(row.startTime) }}</span>
                <span class="execute-time-range__separator">-</span>
                <span>{{ formatDateTime(row.endTime) }}</span>
              </div>
            </template>

            <template #duration="{ row }">
              <span>{{ row.duration || "-" }}</span>
            </template>

            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="View"
                @click="openDetail(row)"
              >
                {{ td("dpp.instance.integratioTask.detail", "Detail") }}
              </el-button>
              <el-button
                v-show="showLogButton"
                link
                type="primary"
                icon="Document"
                @click="openTaskLog(row)"
              >
                {{ td("dpp.instance.integratioTask.viewLog", "View Log") }}
              </el-button>
              <el-button
                link
                type="warning"
                icon="Download"
                @click="downloadLog(row)"
                @mousedown="(event) => event.preventDefault()"
              >
                {{
                  td(
                    "dpp.instance.integratioTask.downloadLog",
                    "Download Log"
                  )
                }}
              </el-button>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <TaskLogDialog ref="logDialogRef" />
  </div>
</template>

<script setup name="Integratio">
import { computed, getCurrentInstance, reactive, ref, watch } from "vue";
import { useRouter } from "vue-router";
import useDefaultLang from "@/composables/useDefaultLang";
import useUserStore from "@/store/system/user";
import { listAttTaskCat } from "@/api/att/cat/taskCat/taskCat";
import {
  getEtlTaskInstanceStatistics,
  listDppEtlTaskInstance,
} from "@/api/dpp/instance/job";
import DeptTree from "@/components/DeptTree/index.vue";
import StatsCardContainer from "@/views/dpp/components/logs/StatsCardContainer.vue";
import StatusTag from "@/views/dpp/components/logs/StatusTag.vue";
import TaskLogDialog from "@/views/dpp/components/logs/taskLog.vue";
import runningTaskIcon from "@/assets/dpp/etl/instance/running-task.svg";
import todayErrorTaskIcon from "@/assets/dpp/etl/instance/today-error-task.svg";
import todayExecutionIcon from "@/assets/dpp/etl/instance/today-execution.svg";
import todaySuccessRateIcon from "@/assets/dpp/etl/instance/today-success-rate.svg";

const { proxy } = getCurrentInstance();
const { td, locale } = useDefaultLang();
const userStore = useUserStore();
const router = useRouter();
const { dpp_task_current_status } = proxy.useDict("dpp_task_current_status");

const appContainerRef = ref(null);
const deptTreeRef = ref(null);
const tableRef = ref(null);
const logDialogRef = ref(null);
const deptOptions = ref([]);
const leftWidth = ref(300);
const selectedStatsIndex = ref(-1);
const statsTime = ref("");
const statisticsLoading = ref(false);
const showLogButton = ref(false);

const statistics = reactive({
  runningCount: 0,
  todayErrorCount: 0,
  todayExecuteCount: 0,
  todaySuccessRate: 0,
});

const statsCards = computed(() => [
  {
    name: td("dpp.integratioTask.runningTasks", "Running Tasks"),
    value: statistics.runningCount,
    unit: td("dpp.integratioTask.taskUnit", "times"),
    iconSrc: runningTaskIcon,
    status: "running",
    todayOnly: false,
    tip: td(
      "dpp.integratioTask.runningTasksTip",
      "Number of tasks with at least one running instance"
    ),
  },
  {
    name: td("dpp.integratioTask.todayErrorTasks", "Today's Failed Tasks"),
    value: statistics.todayErrorCount,
    unit: td("dpp.integratioTask.taskUnit", "times"),
    iconSrc: todayErrorTaskIcon,
    status: "failed",
    todayOnly: true,
    tip: td(
      "dpp.integratioTask.todayErrorTasksTip",
      "Number of tasks whose latest execution failed"
    ),
  },
  {
    name: td("dpp.integratioTask.todayExecutions", "Today's Executions"),
    value: statistics.todayExecuteCount,
    unit: td("dpp.integratioTask.taskUnit", "times"),
    iconSrc: todayExecutionIcon,
    status: null,
    todayOnly: true,
    tip: td(
      "dpp.integratioTask.todayExecutionsTip",
      "Number of execution instances created today"
    ),
  },
  {
    name: td("dpp.integratioTask.todaySuccessRate", "Today's Success Rate"),
    value: statistics.todaySuccessRate,
    unit: "%",
    iconSrc: todaySuccessRateIcon,
    status: "success",
    todayOnly: true,
    tip: td(
      "dpp.integratioTask.todaySuccessRateTip",
      "Successful completed instances divided by all completed instances today"
    ),
  },
]);

const tableColumns = computed(() => [
  {
    label: td("dpp.instance.integratioTask.id", "No."),
    prop: "id",
    sortable: true,
    width: 150,
  },
  {
    label: td(
      "dpp.instance.integratioTask.taskInstanceName",
      "Task Instance Name"
    ),
    prop: "name",
    align: "left",
    minWidth: 280,
    showOverflowTooltip: { effect: "light" },
  },
  {
    label: td("dpp.instance.integratioTask.executionType", "Execution Type"),
    prop: "commandType",
    width: 130,
    dict: "dpp_etl_task_instance_command_type",
  },
  {
    label: td(
      "dpp.instance.integratioTask.executionStatus",
      "Execution Status"
    ),
    prop: "currentStatus",
    width: 120,
    slot: "status",
  },
  {
    label: td(
      "dpp.instance.integratioTask.recentExecutionTime",
      "Recent Execution Time"
    ),
    prop: "startTime",
    width: 330,
    sortable: true,
    sortableKey: "start_time",
    slot: "executeTime",
  },
  {
    label: td("dpp.instance.integratioTask.runDuration", "Duration"),
    prop: "duration",
    width: 130,
    slot: "duration",
  },
  {
    label: td(
      "dpp.instance.integratioTask.responsiblePerson",
      "Responsible Person"
    ),
    prop: "personChargeName",
    width: 130,
  },
  {
    label: td("dpp.instance.integratioTask.createBy", "Created By"),
    prop: "createBy",
    width: 120,
    showOverflowTooltip: true,
  },
  {
    label: td("dpp.instance.integratioTask.createTime", "Created Time"),
    prop: "createTime",
    sortable: true,
    sortableKey: "create_time",
    date: true,
    width: 160,
  },
  {
    label: td("dpp.instance.integratioTask.operation", "Operation"),
    width: 200,
    align: "center",
    fixed: "right",
    slot: "action",
  },
]);

const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "start_time", order: "descending" },
    },
  },
  columns: tableColumns,
  func: listDppEtlTaskInstance,
  params: {
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    catCode: null,
    taskType: "1",
    name: null,
    status: null,
    time: [],
  },
  events: {
    formatParams(params) {
      const { time, ...query } = { ...params };
      if (Array.isArray(time) && time.length === 2) {
        query.startTime = `${time[0]} 00:00:00`;
        query.endTime = `${time[1]} 23:59:59`;
      }
      return query;
    },
  },
});

const searchStore = reactive({
  items: [
    {
      label: computed(() =>
        td(
          "dpp.instance.integratioTask.taskInstanceName",
          "Task Instance Name"
        )
      ),
      prop: "name",
      component: {
        is: "input",
        placeholder: computed(() =>
          td(
            "dpp.instance.integratioTask.inputTaskInstanceName",
            "Please enter task instance name"
          )
        ),
      },
    },
    {
      label: computed(() =>
        td(
          "dpp.instance.integratioTask.executionStatus",
          "Execution Status"
        )
      ),
      prop: "status",
      component: {
        is: "select",
        options: dpp_task_current_status,
        placeholder: computed(() =>
          td(
            "dpp.instance.integratioTask.selectExecutionStatus",
            "Please select execution status"
          )
        ),
      },
    },
    {
      label: computed(() =>
        td("dpp.instance.integratioTask.executionTime", "Execution Time")
      ),
      prop: "time",
      style: { width: "320px" },
      component: {
        is: "date-picker",
        type: "daterange",
        startPlaceholder: computed(() =>
          td("common.form.startDatePlaceholder", "Start date")
        ),
        endPlaceholder: computed(() =>
          td("common.form.endDatePlaceholder", "End date")
        ),
      },
    },
  ],
});

function formatDateTime(value) {
  return value ? proxy.parseTime(value, "{y}-{m}-{d} {h}:{i}:{s}") : "-";
}

function handleQuery() {
  tableRef.value?.getList();
}

function resetQuery() {
  deptTreeRef.value?.resetTree?.();
  tableStore.params.catCode = null;
  tableStore.params.name = null;
  tableStore.params.status = null;
  tableStore.params.time = [];
  selectedStatsIndex.value = -1;
  handleQuery();
}

function handleNodeClick(data) {
  tableStore.params.catCode = data.code;
  handleQuery();
}

function handleStatsCardClick(index) {
  selectedStatsIndex.value = index;
  const card = statsCards.value[index];
  tableStore.params.status = card.status;
  if (card.todayOnly) {
    const today = proxy.parseTime(new Date(), "{y}-{m}-{d}");
    tableStore.params.time = [today, today];
  } else {
    tableStore.params.time = [];
  }
  handleQuery();
}

async function loadStatistics() {
  statisticsLoading.value = true;
  try {
    const response = await getEtlTaskInstanceStatistics({
      projectId: userStore.projectId,
      projectCode: userStore.projectCode,
      taskType: "1",
    });
    if (Number(response?.code) === 200) {
      const data = response.data || {};
      statistics.runningCount = data.runningCount || 0;
      statistics.todayErrorCount = data.todayErrorCount || 0;
      statistics.todayExecuteCount = data.todayExecuteCount || 0;
      statistics.todaySuccessRate = data.todaySuccessRate || 0;
      statsTime.value = proxy.parseTime(
        data.refreshTime || new Date(),
        "{y}-{m}-{d} {h}:{i}:{s}"
      );
    }
  } finally {
    statisticsLoading.value = false;
  }
}

async function loadCategoryTree() {
  const response = await listAttTaskCat({
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    validFlag: true,
  });
  const children = proxy.handleTree(response.data || [], "id", "parentId");
  deptOptions.value = [
    {
      name: td(
        "dpp.instance.integratioTask.dataIntegrationCategory",
        "Data Integration Category"
      ),
      value: "",
      id: 0,
      children,
    },
  ];
}

function openTaskLog(row) {
  logDialogRef.value?.open(row.id);
}

function openDetail(row) {
  router.push({
    path: "/dpp/instance/integratio/detail",
    query: { id: row.id, info: true },
  });
}

function downloadLog(row) {
  proxy.download(
    "/dpp/etlTaskInstance/downloadLog",
    { taskInstanceId: row.id },
    `${row.name || "task"}.log`
  );
}

watch(
  () => [userStore.projectId, userStore.projectCode],
  ([projectId, projectCode]) => {
    tableStore.params.projectId = projectId;
    tableStore.params.projectCode = projectCode;
    if (!projectCode) return;
    loadCategoryTree();
    loadStatistics();
    tableRef.value?.getList();
  },
  { immediate: true }
);

watch(locale, () => {
  if (deptOptions.value[0]) {
    deptOptions.value[0].name = td(
      "dpp.instance.integratioTask.dataIntegrationCategory",
      "Data Integration Category"
    );
  }
});
</script>

<style scoped lang="scss">
.main-content {
  min-width: 0;
  padding: 0 0 0 12px;
}

.log-status {
  cursor: pointer;
}

.execute-time-range {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  white-space: nowrap;

  &__separator {
    color: var(--el-text-color-secondary);
  }
}
</style>
