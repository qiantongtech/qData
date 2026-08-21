<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).
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
            'dpp.instance.developTask.inputCategoryName',
            'Please enter data development category name'
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
              <!-- Keep the log button available for later enablement. -->
              <el-button
                v-show="showLogButton"
                link
                type="primary"
                icon="View"
                @click="openTaskLog(row)"
              >
                {{ td("dpp.instance.developTask.viewLog", "View Log") }}
              </el-button>
              <el-button
                link
                type="warning"
                icon="Download"
                @click="downloadLog(row)"
                @mousedown="(event) => event.preventDefault()"
              >
                {{ td("dpp.instance.developTask.downloadLog", "Download Log") }}
              </el-button>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <TaskLogDialog ref="logDialogRef" instance-type="node" />
  </div>
</template>

<script setup name="Develop">
import { computed, getCurrentInstance, reactive, ref, watch } from "vue";
import useDefaultLang from "@/composables/useDefaultLang";
import useUserStore from "@/store/system/user";
import { listAttDataDevCat } from "@/api/att/cat/dataDevCat/dataDevCat";
import {
  getEtlNodeInstanceStatistics,
  listDppEtlNodeInstance,
} from "@/api/dpp/instance/integratio";
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
    name: td("dpp.instance.developTask.runningNodes", "Running Nodes"),
    value: statistics.runningCount,
    unit: td("dpp.instance.developTask.nodeUnit", "times"),
    iconSrc: runningTaskIcon,
    status: "running",
    todayOnly: false,
    tip: td(
      "dpp.instance.developTask.runningNodesTip",
      "Number of node instances currently running"
    ),
  },
  {
    name: td("dpp.instance.developTask.todayErrorNodes", "Today's Failed Nodes"),
    value: statistics.todayErrorCount,
    unit: td("dpp.instance.developTask.nodeUnit", "times"),
    iconSrc: todayErrorTaskIcon,
    status: "failed",
    todayOnly: true,
    tip: td(
      "dpp.instance.developTask.todayErrorNodesTip",
      "Number of node instances that failed today"
    ),
  },
  {
    name: td("dpp.instance.developTask.todayExecutions", "Today's Executions"),
    value: statistics.todayExecuteCount,
    unit: td("dpp.instance.developTask.nodeUnit", "times"),
    iconSrc: todayExecutionIcon,
    status: null,
    todayOnly: true,
    tip: td(
      "dpp.instance.developTask.todayExecutionsTip",
      "Number of node instances started today"
    ),
  },
  {
    name: td("dpp.instance.developTask.todaySuccessRate", "Today's Success Rate"),
    value: statistics.todaySuccessRate,
    unit: "%",
    iconSrc: todaySuccessRateIcon,
    status: "success",
    todayOnly: true,
    tip: td(
      "dpp.instance.developTask.todaySuccessRateTip",
      "Successful completed node instances divided by all completed node instances today"
    ),
  },
]);

const tableColumns = computed(() => [
  {
    label: td("dpp.instance.developTask.id", "No."),
    prop: "id",
    sortable: true,
    width: 150,
  },
  {
    label: td("dpp.instance.developTask.nodeInstanceName", "Node Instance Name"),
    prop: "name",
    align: "left",
    minWidth: 260,
    showOverflowTooltip: { effect: "light" },
  },
  {
    label: td("dpp.instance.developTask.taskName", "Task Name"),
    prop: "taskInstanceName",
    align: "left",
    minWidth: 280,
    showOverflowTooltip: { effect: "light" },
  },
  {
    label: td("dpp.instance.developTask.executionType", "Execution Type"),
    prop: "commandType",
    width: 130,
    dict: "dpp_etl_task_instance_command_type",
  },
  {
    label: td("dpp.instance.developTask.executionStatus", "Execution Status"),
    prop: "currentStatus",
    width: 120,
    slot: "status",
  },
  {
    label: td("dpp.instance.developTask.recentExecutionTime", "Recent Execution Time"),
    prop: "startTime",
    width: 330,
    sortable: true,
    sortableKey: "start_time",
    slot: "executeTime",
  },
  {
    label: td("dpp.instance.developTask.runDuration", "Duration"),
    prop: "duration",
    width: 130,
    slot: "duration",
  },
  {
    label: td("dpp.instance.developTask.responsiblePerson", "Responsible Person"),
    prop: "personChargeName",
    width: 130,
  },
  {
    label: td("dpp.instance.developTask.createBy", "Created By"),
    prop: "createBy",
    width: 120,
    showOverflowTooltip: true,
  },
  {
    label: td("dpp.instance.developTask.createTime", "Created Time"),
    prop: "createTime",
    sortable: true,
    sortableKey: "create_time",
    date: true,
    width: 160,
  },
  {
    label: td("dpp.instance.developTask.operation", "Operation"),
    width: 150,
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
  func: listDppEtlNodeInstance,
  params: {
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    catCode: null,
    taskType: "3",
    name: null,
    taskInstanceName: null,
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
        td("dpp.instance.developTask.nodeInstanceName", "Node Instance Name")
      ),
      prop: "name",
      component: {
        is: "input",
        placeholder: computed(() =>
          td(
            "dpp.instance.developTask.inputNodeInstanceName",
            "Please enter node instance name"
          )
        ),
      },
    },
    {
      label: computed(() => td("dpp.instance.developTask.taskName", "Task Name")),
      prop: "taskInstanceName",
      component: {
        is: "input",
        placeholder: computed(() =>
          td("dpp.instance.developTask.inputTaskName", "Please enter task name")
        ),
      },
    },
    {
      label: computed(() =>
        td("dpp.instance.developTask.executionStatus", "Execution Status")
      ),
      prop: "status",
      component: {
        is: "select",
        options: dpp_task_current_status,
        placeholder: computed(() =>
          td(
            "dpp.instance.developTask.selectExecutionStatus",
            "Please select execution status"
          )
        ),
      },
    },
    {
      label: computed(() =>
        td("dpp.instance.developTask.executionTime", "Execution Time")
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
  tableStore.params.taskInstanceName = null;
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
    const response = await getEtlNodeInstanceStatistics({
      projectId: userStore.projectId,
      projectCode: userStore.projectCode,
      taskType: "3",
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
  const response = await listAttDataDevCat({
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    validFlag: true,
  });
  const children = proxy.handleTree(response.data || [], "id", "parentId");
  deptOptions.value = [
    {
      name: td("dpp.instance.developTask.dataDevCategory", "Data Development Category"),
      value: "",
      id: 0,
      children,
    },
  ];
}

function openTaskLog(row) {
  logDialogRef.value?.open(row.id);
}

function downloadLog(row) {
  proxy.download(
    "/dpp/etlNodeInstance/downloadLog",
    { nodeInstanceId: row.id },
    `${row.name || "node"}.log`
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
      "dpp.instance.developTask.dataDevCategory",
      "Data Development Category"
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
