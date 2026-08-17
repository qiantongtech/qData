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
    <GuideTip tip-id="dpp/tasker/dpptaskerddv.list" />
    <el-container>
      <DeptTree
        :api="api"
        :extraParams="{
          projectCode: userStore.projectCode,
          projectId: userStore.projectId,
        }"
        :editable="true"
        :leftWidth="leftWidth"
        :placeholder="
          td('dpp.developTask.inputCategoryName', 'Please enter data development category name')
        "
        ref="DeptTreeRef"
        @node-click="handleNodeClick"
        :title="td('dpp.developTask.dataDevCategory', 'Data Development Category')"
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
              :visible-count="3"
              @query="handleQuery"
              @reset="resetQuery"
            />
          </template>
          <template #actions-data>
            <el-button type="primary" plain @click="handleAdd">
              <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td("common.button.add", "Add") }}
            </el-button>
          </template>

          <qt-table v-bind="tableStore" ref="tableRef">
            <template #name="{ row }">
              <div class="name-label task-title">
                <div
                  class="justify task-title-row"
                  @click="
                    routeTo('/dpp/task/developTask/detail', {
                      ...row,
                      info: true,
                    })
                  "
                >
                  <img
                    :src="getDatasourceIcon(row.datasourceType)"
                    alt=""
                    class="datasource-icon"
                    v-if="getDatasourceIcon(row.datasourceType)"
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
                <div class="text-ellipsis desc-text">
                  {{ row.description || "-" }}
                </div>
              </div>
            </template>
            <template #releaseState="{ row }">
              <div class="flex-column fz12">
                <div class="flex-center">
                  <span class="black-label mr5">{{ td("dpp.developTask.taskStatus", "Task Status") }}:</span>
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
                      td("dpp.developTask.scheduleStatus", "Schedule Status")
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
                  <el-icon class="mr5 fz14"><Clock /></el-icon>
                  <span
                    class="text-ellipsis cron-text"
                    :title="cronToZh(row.cronExpression)"
                  >
                    {{ cronToZh(row.cronExpression) || "-" }}
                  </span>
                </div>
                <div class="flex-center">
                  <span class="mr5">{{
                      td("dpp.developTask.executeStrategy", "Execution Strategy")
                    }}:</span>
                  <dict-tag
                    :options="dpp_etl_task_execution_type"
                    :value="row.executionType"
                  />
                </div>
                <div class="flex-center mt5">
                  <span class="mr5">{{
                      td("dpp.integratioTask.scheduler", "Scheduler")
                    }}:</span>
                  <span
                      class="text-ellipsis cron-text"
                      :title="`${getSchedulerLabel(row.scheduler)} `"
                  >
                    {{ getSchedulerLabel(row.scheduler) }}
                  </span>
                </div>
              </div>
            </template>
            <template #lastExecute="{ row }">
              <div
                class="last-execute-status"
                @click="openTaskLogDialog(row)"
              >
                <StatusTag
                  size="small"
                  :status="row.currentStatus"
                />
              </div>
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
                @click="routeTo('/dpp/task/developTask/edit', row)"
                >{{
                  td("dpp.developTask.configureTask", "Configure Task")
                }}</el-button>
              <el-button
                link
                type="primary"
                icon="view"
                @click="
                  routeTo('/dpp/task/developTask/detail', {
                    ...row,
                    info: true,
                  })
                "
                >{{ td("common.button.details", "Details") }}</el-button>

              <el-popover placement="bottom" :width="170" trigger="click">
                <template #reference>
                  <el-button link type="primary" icon="ArrowDown">{{
                    td("common.button.more", "More")
                  }}</el-button>
                </template>
                <div style="width: 120px" class="butgdlist">
                  <el-button
                    link
                    style="padding-left: 14px"
                    type="primary"
                    icon="Operation"
                    @click="handleJobLog(row)"
                    :disabled="row.schedulerState == '1'"
                    v-if="row.processType != 1"
                    >{{
                      td("dpp.developTask.schedulePeriod", "Schedule Period")
                    }}</el-button>
                  <el-button
                    link
                    type="primary"
                    icon="Stopwatch"
                    @click="handleDataView(row)"
                    >{{
                      td("dpp.developTask.runInstance", "Run Instance")
                    }}</el-button>
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    :disabled="row.status != 1"
                    :loading="executeOnceLoading"
                    @click="handleExecuteOnce(row)"
                    >{{
                      td("dpp.developTask.executeOnce", "Execute Once")
                    }}</el-button>
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    v-if="
                      row.datasourceType === 'FlinkStream' && row.taskInstanceId
                    "
                    @click="handleExecuteStop(row)"
                    >{{ td("dpp.developTask.stop", "Stop") }}</el-button>
                  <el-button
                    link
                    type="primary"
                    icon="View"
                    @click="openTaskLogDialog(row)"
                    >{{
                      td("dpp.developTask.realTimeLog", "Real-time Log")
                    }}</el-button>
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="row.status == 1"
                    @click="handleDelete(row)"
                    >{{ td("common.button.delete", "Delete") }}</el-button>
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
      :status="runningTasksQuery.status"
      @update:visible="showRunningTasks = $event"
      @viewRealTimeLog="viewRealTimeLog"
      @viewInstance="viewInstance"
      @taskClick="openTaskDetail"
      @loadMore="loadMoreTasks"
      @refresh="refreshRunningTasks"
    />
    <instance
      :visible="DataView"
      :taskType="3"
      @update:visible="DataView = $event"
      @confirm="submitForm"
      :data="form"
      :title="td('dpp.developTask.runInstance', 'Run Instance')"
    />
    <TaskLogDialog ref="taskLogDialogRef" />
    <el-dialog
      :title="td('dpp.developTask.schedulePeriod', 'Schedule Period')"
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
      <!--      <crontab-->
      <!--        ref="crontabRef"-->
      <!--        @hide="openCron = false"-->
      <!--        @fill="crontabFill"-->
      <!--        :expression="expression"-->
      <!--        :Crontab="false"-->
      <!--      >-->
      <!--      </crontab>-->
    </el-dialog>
    <add
      :visible="taskConfigDialogVisible"
      :title="td('dpp.developTask.addTask', 'Add Task')"
      @update:visible="taskConfigDialogVisible = $event"
      @save="handleSave"
      @confirm="handleConfirm"
      :data="taskForm"
      :deptOptions="deptOptions"
      :userList="userList"
      :info="route.query.info"
    />
  </div>
</template>

<script setup name="DppDevelopTask">
import useDefaultLang from "@/composables/useDefaultLang";
import { treeData } from "@/views/dpp/task/developTask/data";
import {
  listDppEtlTask,
  delDppEtlTask,
  updateReleaseSchedule,
  updateReleaseJobTask,
  releaseTaskCrontab,
  startDppEtlTask,
  createEtlTaskFront,
  getEtlTaskStatistics,
  getEtlTaskInstanceList,
} from "@/api/dpp/task/index.js";
import { usePageRefresh } from "@/composables/usePageRefresh";
import { getDatasourceIcon } from "@/utils/datasource";
import { execute } from "@/api/dpp/task";
import { cronToZh } from "@/utils/cronUtils";
import { timeAgo } from "@/utils/time";
import {
  listAttDataDevCat,
  getAttDataDevCat,
  addAttDataDevCat,
  updateAttDataDevCat,
  delAttDataDevCat,
} from "@/api/att/cat/dataDevCat/dataDevCat";
import Crontab from "@/components/Crontab/index.vue";
import instance from "@/views/dpp/components/logs/instance.vue";
import TaskLogDialog from "@/views/dpp/components/logs/taskLog.vue";
import RunningTasksPanel from "@/views/dpp/components/logs/RunningTasksPanel.vue";
import StatsCardContainer from "@/views/dpp/components/logs/StatsCardContainer.vue";
import StatusTag from "@/views/dpp/components/logs/StatusTag.vue";
import runningTaskIcon from "@/assets/dpp/etl/instance/running-task.svg";
import todayErrorTaskIcon from "@/assets/dpp/etl/instance/today-error-task.svg";
import todayExecutionIcon from "@/assets/dpp/etl/instance/today-execution.svg";
import todaySuccessRateIcon from "@/assets/dpp/etl/instance/today-success-rate.svg";
import useUserStore from "@/store/system/user";

const userStore = useUserStore();
import { useRoute, useRouter } from "vue-router";
import DeptTree from "@/components/DeptTree";
import add from "./add/add.vue";
import { deptUserTree } from "@/api/system/system/user.js";
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

const { td, locale } = useDefaultLang();
const { proxy } = getCurrentInstance();
const statsCardContainerRef = ref(null);
const statsCardHeight = ref(0);
const executeOnceLoading = ref(false);

const api = {
  list: listAttDataDevCat,
  get: getAttDataDevCat,
  add: addAttDataDevCat,
  update: updateAttDataDevCat,
  del: delAttDataDevCat,
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
const typaOptions = treeData.map((item) => {
  return {
    ...item,
    label: item.label,
    value: item.value,
  };
});
const schedulerOptions = [
  { label: "Quartz", value: "QUARTZ" },
  { label: "DolphinScheduler", value: "DOLPHINSCHEDULER" },
];
const getSchedulerLabel = (value) => {
  return schedulerOptions.find((item) => item.value == value)?.label || value || "-";
};
const getStatus = (status) => {
  if (status == "-1") {
    return "-1";
  } else {
    return "0";
  }
};
// Task configuration
const taskConfigDialogVisible = ref(false);
const deptOptions = ref([]);
let userList = ref([]);
let taskForm = ref({});
const handleAdd = () => {
  taskConfigDialogVisible.value = true;
};
// Save and close
const handleSave = (form) => {
  const parms = {
    ...form,
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    type: "3", //New identifier for data development
  };
  createEtlTaskFront(parms).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess(td("common.message.msgOpSuccess", "Operation successful"));
      handleQuery();
    }
  });
};
// Save and improve
const handleConfirm = (form) => {
  const parms = {
    ...form,
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    type: "3", //New identifier for data development
  };
  createEtlTaskFront(parms).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess(td("common.message.msgOpSuccess", "Operation successful"));
      handleQuery();
      routeTo("/dpp/task/developTask/edit", {
        ...res.data,
      });
    }
  });
};

const leftWidth = ref(300); // Initial left width
/** Drop down tree structure */
function getDeptTree() {
  api
    .list({
      projectId: userStore.projectId,
      projectCode: userStore.projectCode,
      validFlag: true,
    })
    .then((response) => {
      deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    });
  deptUserTree().then((res) => {
    userList.value = res.data;
  });
}
function handleNodeClick(data) {
  tableStore.params.catCode = data.code;
  handleQuery();
}

const statistics = reactive({
  runningCount: 0,
  todayErrorCount: 0,
  todayExecuteCount: 0,
  todaySuccessRate: 0,
});
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

const statsCards = computed(() => [
  {
    name: td("dpp.developTask.runningTasks", "Running Tasks"),
    value: statistics.runningCount,
    unit: td("dpp.developTask.taskUnit", "times"),
    iconClass: "icon-blue",
    iconSrc: runningTaskIcon,
    type: "running",
    tip: td(
      "dpp.developTask.runningTasksTip",
      "Number of tasks with at least one running instance"
    ),
  },
  {
    name: td("dpp.developTask.todayErrorTasks", "Today's Failed Tasks"),
    value: statistics.todayErrorCount,
    unit: td("dpp.developTask.taskUnit", "times"),
    iconClass: "icon-orange",
    iconSrc: todayErrorTaskIcon,
    type: "failed",
    tip: td(
      "dpp.developTask.todayErrorTasksTip",
      "Number of tasks whose latest execution failed today"
    ),
  },
  {
    name: td("dpp.developTask.todayExecutions", "Today's Executions"),
    value: statistics.todayExecuteCount,
    unit: td("dpp.developTask.taskUnit", "times"),
    iconClass: "icon-blue",
    iconSrc: todayExecutionIcon,
    type: "",
    tip: td(
      "dpp.developTask.todayExecutionsTip",
      "Number of execution instances created today"
    ),
  },
  {
    name: td("dpp.developTask.todaySuccessRate", "Today's Success Rate"),
    value: statistics.todaySuccessRate,
    unit: "%",
    iconClass: "icon-green",
    iconSrc: todaySuccessRateIcon,
    type: "success",
    tip: td(
      "dpp.developTask.todaySuccessRateTip",
      "Successful completed instances divided by all completed instances today"
    ),
  },
]);

const currentPanelTitle = computed(() => {
  if (!showRunningTasks.value || selectedStatsIndex.value < 0) return "";
  return statsCards.value[selectedStatsIndex.value]?.name || "";
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

async function loadStatistics() {
  try {
    const res = await getEtlTaskStatistics({
      projectId: userStore.projectId,
      projectCode: userStore.projectCode,
      taskType: "3",
    });
    if (Number(res?.code) === 200) {
      const data = res.data || {};
      statistics.runningCount = data.runningCount || 0;
      statistics.todayErrorCount = data.todayErrorCount || 0;
      statistics.todayExecuteCount = data.todayExecuteCount || 0;
      statistics.todaySuccessRate = data.todaySuccessRate || 0;
      statsTime.value = proxy.parseTime(
        data.statisticsTime || new Date(),
        "{y}-{m}-{d} {h}:{i}:{s}"
      );
    }
  } catch (error) {
    console.error("Failed to load data development task statistics", error);
  }
}

async function loadRunningTasks(index = 0, isLoadMore = false) {
  if (runningTasksQuery.loading) return;
  runningTasksQuery.loading = true;
  if (!isLoadMore) {
    runningTasksQuery.pageNum = 1;
    runningTasks.value = [];
    runningTasksQuery.total = 0;
    runningTasksQuery.status =
      index === 0 ? "running" : index === 1 ? "failed" : index === 3 ? "success" : null;
  } else {
    runningTasksQuery.pageNum += 1;
  }
  const today = proxy.parseTime(new Date(), "{y}-{m}-{d}");
  try {
    const res = await getEtlTaskInstanceList({
      pageNum: runningTasksQuery.pageNum,
      pageSize: runningTasksQuery.pageSize,
      taskType: "3",
      status: runningTasksQuery.status,
      projectId: userStore.projectId,
      projectCode: userStore.projectCode,
      startTime: index === 0 ? undefined : `${today} 00:00:00`,
      endTime: index === 0 ? undefined : `${today} 23:59:59`,
    });
    if (Number(res?.code) === 200) {
      const newTasks = res.data?.rows || [];
      runningTasks.value = isLoadMore
        ? [...runningTasks.value, ...newTasks]
        : newTasks;
      runningTasksQuery.total = res.data?.total || 0;
      showRunningTasks.value = false;
    }
  } catch (error) {
    if (isLoadMore) runningTasksQuery.pageNum -= 1;
    console.error("Failed to load data development task instances", error);
  } finally {
    runningTasksQuery.loading = false;
  }
}

function loadMoreTasks() {
  if (noMoreTasks.value || runningTasksQuery.loading) return;
  loadRunningTasks(selectedStatsIndex.value, true);
}

function refreshRunningTasks() {
  loadRunningTasks(selectedStatsIndex.value);
}

function viewRealTimeLog(task) {
  taskLogDialogRef.value?.open(task.id);
}

function viewInstance(task) {
  handleDataView({
    ...task,
    id: task.taskId || task.id,
  });
}

function openTaskDetail(task) {
  routeTo("/dpp/task/developTask/detail", {
    ...task,
    id: task.taskId || task.id,
    info: true,
  });
}
const route = useRoute();
let openCron = ref(false);
let row = ref();
let expression = ref("");
/** Run instance button action */
function handleJobLog(data) {
  row.value = "";
  row.value = data || "";
  openCron.value = true;
  expression.value = data.cronExpression || "";
  console.log("🚀 ~ handleJobLog ~ expression.value:", expression.value);
}
function handleschedulerState(id, row) {
  const text =
    row.schedulerState == "1"
      ? td("dpp.developTask.online", "Online")
      : td("dpp.developTask.offline", "Offline");

  // Confirmation box pops up
  proxy.$modal
    .confirm(
      td(
        "dpp.developTask.confirmScheduleStatus",
        'Are you sure to "{action}" data development schedule status "{name}"?'
      )
        .replace("{action}", text)
        .replace("{name}", row.name)
    )
    .then(function () {
      loading.value = true;
      // Call the background interface to update the scheduling status
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
        })
        .catch(() => {
          // Recovery operations in case of processing failure
          row.schedulerState = row.schedulerState === "1" ? "0" : "1"; // Restore previous state
        })
        .finally(() => {
          loading.value = false; // Stop loading regardless of success or failure
        });
    })
    .catch(() => {
      // Restoring state on failure
      row.schedulerState = row.schedulerState == "1" ? "0" : "1";
    });
}

/** Change enabled status value */
function handleStatusChange(id, row) {
  const text =
    row.status == "1"
      ? td("dpp.developTask.online", "Online")
      : td("dpp.developTask.offline", "Offline");

  // Confirmation box pops up
  proxy.$modal
    .confirm(
      td(
        "dpp.developTask.confirmTaskStatus",
        'Are you sure to "{action}" data development task "{name}"?'
      )
        .replace("{action}", text)
        .replace("{name}", row.name)
    )
    .then(function () {
      loading.value = true; // Start loading
      // Call the background interface to update the publishing status
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
          // Restoring state on failure
          row.status = row.status === "1" ? "0" : "1";
        })
        .finally(() => {
          loading.value = false; // Stop loading regardless of success or failure
        });
    })
    .catch(() => {
      // Restoring state on failure
      row.status = row.status === "1" ? "0" : "1";
    });
}
/** Return value after confirmation */
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
const handleExecuteOnce = async (row) => {
  if (executeOnceLoading.value) return;
  if (!row?.id) {
    proxy.$modal.msgWarning(
      td("dpp.developTask.invalidTaskId", "Invalid task ID, please refresh and retry")
    );
    return;
  }
  executeOnceLoading.value = true;
  try {
    const res = await startDppEtlTask(row.id);

    if (Number(res?.code) === 200) {
      proxy.$modal.msgSuccess(td("dpp.developTask.executeSuccess", "Executed successfully"));
    } else {
      proxy.$modal.msgWarning(
        res?.msg ||
          td("dpp.developTask.executeFailed", "Execution failed, please contact administrator")
      );
    }
  } finally {
    executeOnceLoading.value = false;
    handleQuery();
  }
};

const handleExecuteStop = async (row) => {
  if (!row?.taskInstanceId) {
    proxy.$modal.msgWarning(
      td("dpp.developTask.cannotStop", "Current task cannot be stopped, please refresh and retry")
    );
    return;
  }
  loading.value = true;
  try {
    const res = await execute(row.taskInstanceId, "STOP");
    if (Number(res?.code) === 200) {
      proxy.$modal.msgSuccess(td("dpp.developTask.executeSuccess", "Executed successfully"));
    } else {
      proxy.$modal.msgWarning(
        res?.msg ||
          td("dpp.developTask.executeFailed", "Execution failed, please contact administrator")
      );
    }
  } finally {
    setTimeout(() => {
      loading.value = false;
      handleQuery();
    }, 2000);
  }
};

let DataView = ref(false);
const taskLogDialogRef = ref(null);
/** Run instance interface */
function handleDataView(row) {
  form.value = row;
  DataView.value = true;
}

const loading = ref(false);
const ids = ref([]);
const router = useRouter();

const form = ref({
  id: "",
  code: "",
  taskType: "",
  name: "name",
  version: "",
  componentType: "",
  type: "3",
  taskConfig: {
    name: "",
    catCode: "",
    personCharge: "",
    contactNumber: "",
    releaseState: "0",
    description: "",
  },
  taskParams: {
    sqlType: "1",
    type: "",
    sql: "",
    typaCode: "DM",
    localParams: [],
    datasources: {
      datasourceId: "",
      datasourceType: "",
      dbname: "",
    },
  },
});
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
      label: td("dpp.developTask.taskInfo", "Task Info"),
      prop: "name",
      align: "left",
      slot: "name",
      width: 300,
    },
    {
      label: td("dpp.developTask.runControl", "Run Control"),
      prop: "status",
      width: 190,
      slot: "releaseState",
      align: "left",
    },
    {
      label: td("dpp.developTask.dispatchInformation", "调度信息"),
      prop: "cronExpression",
      width: 260,
      slot: "cronExpression",
      align: "left",
    },
    {
      label: td("dpp.developTask.recentExecution", "Recent Execution"),
      width: 160,
      slot: "lastExecute",
      align: "left",
    },

    {
      label: td("dpp.developTask.responsiblePerson", "Responsible Person"),
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

const searchStore = reactive({
  items: [
    {
      label: td("dpp.developTask.taskName", "Task Name"),
      prop: "name",
      align: "left",
      component: {
        is: "input",
        placeholder: td("dpp.developTask.inputTaskName", "Please enter task name"),
      },
    },
    {
      label: td("dpp.developTask.taskStatus", "Task Status"),
      prop: "status",
      component: {
        is: "select",
        placeholder: td("dpp.developTask.selectTaskStatus", "Please select task status"),
        options: dpp_etl_task_status,
      },
    },
    {
      label: td("dpp.developTask.executeStatus", "Execution Status"),
      prop: "currentStatus",
      component: {
        is: "select",
        options: dpp_task_current_status,
        placeholder: td(
          "dpp.developTask.selectExecuteStatus",
          "Please select execution status"
        ),
      },
    },
    {
      label: td("dpp.developTask.datasourceType", "Data Connection Type"),
      prop: "datasourceType",
      component: {
        is: "select",
        placeholder: td(
          "dpp.developTask.selectDatasourceType",
          "Please select data connection type"
        ),
        options: typaOptions,
      },
    },
    {
      label: td("dpp.developTask.executeTime", "Execution Time"),
      prop: "executeTime",
      style: { width: "320px" },
      component: {
        is: "date-picker",
        type: "daterange",
        startPlaceholder: td("common.date.startDate", "Start date"),
        endPlaceholder: td("common.date.endDate", "End date"),
      },
    },
  ],
});

function listWrapper(params) {
  const p = { ...params };
  p.projectId = userStore.projectId;
  p.projectCode = userStore.projectCode;
  p.type = "3";
  if (p.executeTime && p.executeTime.length === 2) {
    p.startTime = `${p.executeTime[0]} 00:00:00`;
    p.endTime = `${p.executeTime[1]} 23:59:59`;
  }
  delete p.executeTime;
  return listDppEtlTask(p);
}

function openTaskLogDialog(row) {
  if (!row?.taskInstanceId) {
    proxy.$modal.msgWarning(
      td("dpp.developTask.noExecutionLog", "No recent execution log")
    );
    return;
  }
  taskLogDialogRef.value?.open(row.taskInstanceId);
}

function getList() {
  tableRef.value?.getList();
}

/** Search button action */
function handleQuery() {
  if (!userStore.projectId) {
    proxy.$modal.msgWarning(
      td("dpp.developTask.projectRequired", "所属项目不能为空")
    );
    return;
  }
  getList();
  loadStatistics();
}
const DeptTreeRef = ref(null);
/** reset button action */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = "";
  tableStore.params.currentStatus = null;
  tableStore.params.executeTime = [];
  getList();
}
/** submit button */
function submitForm() {
  handleQuery();
}

/** Delete button action */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(
      td(
        "dpp.developTask.confirmDelete",
        'Are you sure to delete data development task with ID "{id}"?'
      ).replace("{id}", _ids)
    )
    .then(function () {
      return delDppEtlTask(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td("common.message.deleteSuccess", "Deleted successfully"));
    })
    .catch(() => {});
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

function syncPageI18n() {
  const columnLabels = [
    td("common.texts.number", "No."),
    td("dpp.developTask.taskInfo", "Task Info"),
    td("dpp.developTask.runControl", "Run Control"),
    td("dpp.developTask.dispatchInformation", "Scheduling Information"),
    td("dpp.developTask.recentExecution", "Recent Execution"),
    td("dpp.developTask.responsiblePerson", "Responsible Person"),
    td("common.texts.createdBy", "Created By"),
    td("common.texts.createdTime", "Created Time"),
    td("common.texts.operation", "Operation"),
  ];
  tableStore.columns.forEach((column, index) => {
    column.label = columnLabels[index];
  });

  searchStore.items[0].label = td("dpp.developTask.taskName", "Task Name");
  searchStore.items[0].component.placeholder = td(
    "dpp.developTask.inputTaskName",
    "Please enter task name"
  );
  searchStore.items[1].label = td("dpp.developTask.taskStatus", "Task Status");
  searchStore.items[1].component.placeholder = td(
    "dpp.developTask.selectTaskStatus",
    "Please select task status"
  );
  searchStore.items[2].label = td(
    "dpp.developTask.executeStatus",
    "Execution Status"
  );
  searchStore.items[2].component.placeholder = td(
    "dpp.developTask.selectExecuteStatus",
    "Please select execution status"
  );
  searchStore.items[3].label = td(
    "dpp.developTask.datasourceType",
    "Data Connection Type"
  );
  searchStore.items[3].component.placeholder = td(
    "dpp.developTask.selectDatasourceType",
    "Please select data connection type"
  );
  searchStore.items[4].label = td("dpp.developTask.executeTime", "Execution Time");
  searchStore.items[4].component.startPlaceholder = td(
    "common.date.startDate",
    "Start date"
  );
  searchStore.items[4].component.endPlaceholder = td(
    "common.date.endDate",
    "End date"
  );
}

watch(locale, syncPageI18n);

const updateStatsCardHeight = () => {
  const element = statsCardContainerRef.value?.$el;
  statsCardHeight.value = element ? element.offsetHeight + 20 : 0;
};

let resizeObserver = null;

async function initializePageData() {
  if (!userStore.projectCode) return;
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
  () => [userStore.projectId, userStore.projectCode],
  async ([projectId, projectCode], [oldProjectId, oldProjectCode] = []) => {
    if (
      projectCode &&
      (projectId !== oldProjectId || projectCode !== oldProjectCode)
    ) {
      showRunningTasks.value = false;
      selectedStatsIndex.value = -1;
      await initializePageData();
    }
  }
);

usePageRefresh("developTask", initializePageData);
</script>
<style lang="scss" src="@/assets/styles/system/table-style-optimized.scss"></style>

<style scoped lang="scss">
.last-execute-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-top: 6px;
  font-size: 14px;
  color: #1e293b;
}

.last-execute-status {
  display: inline-flex;
  cursor: pointer;
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
