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
      />
      <el-main class="main-content">
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
            <el-button type="primary" plain @click="handleAdd">
              <i class="iconfont-mini icon-xinzeng mr5"></i
              >{{ td("common.button.add", "Add") }}
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
                  <span class="black-label mr5"
                    >{{ td("dpp.developTask.taskStatus", "Task Status") }}:</span
                  >
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
                  <span class="black-label mr5"
                    >{{
                      td("dpp.developTask.scheduleStatus", "Schedule Status")
                    }}:</span
                  >
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
                  <span class="mr5"
                    >{{
                      td("dpp.developTask.executeStrategy", "Execution Strategy")
                    }}:</span
                  >
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
              <div class="flex-column fz14 last-execute-col">
                <template v-if="row.lastExecuteTime">
                  <div class="mb5">
                    <dict-tag
                      v-if="
                        row.lastExecuteStatus !== null &&
                        row.lastExecuteStatus !== undefined &&
                        row.lastExecuteStatus !== ''
                      "
                      :options="dpp_etl_task_instance"
                      :value="row.lastExecuteStatus"
                    />
                    <span v-else>-</span>
                  </div>
                  <span>
                    {{ parseTime(row.lastExecuteTime, "{y}-{m}-{d} {h}:{i}") }}
                  </span>
                </template>
                <template v-else>
                  <div class="mb5">
                    <el-tag type="infos">{{
                      td("dpp.developTask.notExecuted", "Not Executed")
                    }}</el-tag>
                  </div>
                  <span>-</span>
                </template>
              </div>
            </template>
            <template #personChargeName="{ row }">
              <div class="flex-column fz14">
                <span
                  class="text-ellipsis person-charge-ellipsis"
                  :title="row.personCharge"
                  >{{ row.personChargeName || "-" }}</span
                >
                <span>{{ row.contactNumber || "-" }}</span>
              </div>
            </template>
            <template #createBy="{ row }">
              <div class="flex-column fz14">
                <span
                  class="text-ellipsis person-charge-ellipsis"
                  :title="row.personCharge"
                  >{{ row.createBy || "-" }}</span
                >
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
                }}</el-button
              >
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
                >{{ td("common.button.details", "Details") }}</el-button
              >

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
                    v-if="row.processType != 1"
                    >{{
                      td("dpp.developTask.schedulePeriod", "Schedule Period")
                    }}</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="Stopwatch"
                    @click="handleDataView(row)"
                    v-if="row.processType == 1 && row.status == 1"
                    >{{ td("dpp.developTask.stopTask", "Stop Task") }}</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="Stopwatch"
                    @click="handleDataView(row)"
                    v-if="row.processType == 1 && row.status != 1"
                    >{{
                      td("dpp.developTask.runInstance", "Run Instance")
                    }}</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    :disabled="row.status != 1"
                    :loading="executeOnceLoading"
                    @click="handleExecuteOnce(row)"
                    >{{
                      td("dpp.developTask.executeOnce", "Execute Once")
                    }}</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    v-if="
                      row.datasourceType === 'FlinkStream' && row.taskInstanceId
                    "
                    @click="handleExecuteStop(row)"
                    >{{ td("dpp.developTask.stop", "Stop") }}</el-button
                  >
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="row.status == 1"
                    @click="handleDelete(row)"
                    >{{ td("common.button.delete", "Delete") }}</el-button
                  >
                </div>
              </el-popover>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>
    <instance
      :visible="DataView"
      :taskType="3"
      @update:visible="DataView = $event"
      @confirm="submitForm"
      :data="form"
      :title="td('dpp.developTask.runningInstance', 'Running Instance')"
    />
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
  addDppEtlTask,
  updateDppEtlTask,
  updateReleaseSchedule,
  updateReleaseJobTask,
  releaseTaskCrontab,
  startDppEtlTask,
  createEtlTaskFront,
} from "@/api/dpp/task/index.js";
import { usePageRefresh } from "@/composables/usePageRefresh";
import { getDatasourceIcon } from "@/utils/datasource";
import { execute } from "@/api/dpp/task";
import { cronToZh } from "@/utils/cronUtils";
import {
  listAttDataDevCat,
  getAttDataDevCat,
  addAttDataDevCat,
  updateAttDataDevCat,
  delAttDataDevCat,
} from "@/api/att/cat/dataDevCat/dataDevCat";
import Crontab from "@/components/Crontab/index.vue";
import instance from "@/views/dpp/components/instance.vue";
import useUserStore from "@/store/system/user";

const userStore = useUserStore();
import { useRoute, useRouter } from "vue-router";
import DeptTree from "@/components/DeptTree";
import add from "./add/add.vue";
import { deptUserTree } from "@/api/system/system/user.js";
import { ref, reactive, getCurrentInstance, watch, toRefs } from "vue";
import {checkApi} from "@/api/ds/api/api.js";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
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
  dpp_etl_task_instance,
  datasource_type,
  dpp_etl_task_process_type,
} = proxy.useDict(
  "dpp_etl_task_status",
  "dpp_etl_task_execution_type",
  "dpp_etl_task_instance",
  "datasource_type",
  "dpp_etl_task_process_type"
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
const getExecutionType = (executionType) => {
  if (!executionType) return null;
  const item = typaOptions.find(
    (i) => String(i.value).toLowerCase() === String(executionType).toLowerCase()
  );
  if (!item) return null;
  return {
    ...item,
    elTagType: item.elTagType, // Default info
  };
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
const isResizing = ref(false); // Determine whether dragging is in progress

let startX = 0; // Initial position when mouse is pressed // Initial left width
const startResize = (event) => {
  isResizing.value = true;
  startX = event.clientX;
  document.addEventListener("mousemove", updateResize);
  document.addEventListener("mouseup", stopResize);
};
const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener("mousemove", updateResize);
  document.removeEventListener("mouseup", stopResize);
};
const updateResize = (event) => {
  if (isResizing.value) {
    const delta = event.clientX - startX; // Calculate mouse movement distance
    leftWidth.value += delta; // Modify left width
    startX = event.clientX; // Update starting position
    // Use requestAnimationFrame to reduce page redraw frequency
    requestAnimationFrame(() => {});
  }
};
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
function handleschedulerState(id, row, e) {
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
        .then((response) => {
          proxy.$modal.msgSuccess(
            td("common.message.msgOpSuccess", "Operation successful")
          );
        })
        .catch((error) => {
          // Recovery operations in case of processing failure
          row.schedulerState = row.schedulerState === "1" ? "0" : "1"; // Restore previous state
        })
        .finally(() => {
          loading.value = false; // Stop loading regardless of success or failure
        });
    })
    .catch((error) => {
      // Restoring state on failure
      row.schedulerState = row.schedulerState == "1" ? "0" : "1";
    });
}

/** Change enabled status value */
function handleStatusChange(id, row, e) {
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
        .then((response) => {
          proxy.$modal.msgSuccess(
            td("common.message.msgOpSuccess", "Operation successful")
          );
          handleQuery();
        })
        .catch((error) => {
          // Restoring state on failure
          row.status = row.status === "1" ? "0" : "1";
        })
        .finally(() => {
          loading.value = false; // Stop loading regardless of success or failure
        });
    })
    .catch((error) => {
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
  }).then((response) => {
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
/** Run instance interface */
function handleDataView(row) {
  form.value = row;
  DataView.value = true;
}

const open = ref(false);
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
const rules = ref({});

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
      label: td("dpp.developTask.scheduleCycle", "Schedule Cycle"),
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
      label: td("dpp.developTask.processType", "Process Type"),
      prop: "processType",
      component: {
        is: "select",
        placeholder: td("dpp.developTask.selectProcessType", "Please select process type"),
        options: [
          { label: td("dpp.developTask.streamProcess", "Stream Processing"), value: "1" },
          { label: td("dpp.developTask.batchProcess", "Batch Processing"), value: "2" },
        ],
      },
    },
  ],
});

function listWrapper(params) {
  const p = { ...params };
  p.projectId = userStore.projectId;
  p.projectCode = userStore.projectCode;
  p.type = "3";
  return listDppEtlTask(p);
}

// Monitor id changes
watch(
  () => userStore.projectCode,
  (newId) => {
    handleQuery();
    getDeptTree();
  },
  { immediate: true } // `immediate` is true, which means that a watch will be executed immediately when the page is loaded.
);

function getList() {
  tableRef.value?.getList();
}

// form reset
function reset() {
  form.value = {
    id: null,
    type: null,
    name: null,
    status: null,
  };
  proxy.resetForm("dppEtlTaskRef");
}

/** Search button action */
function handleQuery() {
  getList();
}
const DeptTreeRef = ref(null);
/** reset button action */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = "";
  getList();
}
/** submit button */
function submitForm() {
  proxy.$refs["dppEtlTaskRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDppEtlTask(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(
              td("common.message.editSuccess", "Updated successfully")
            );
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        addDppEtlTask(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(
              td("common.message.addSuccess", "Added successfully")
            );
            open.value = false;
            getList();
          })
          .catch((error) => {});
      }
    }
  });
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
usePageRefresh("developTask", () => getList());
getDeptTree();
</script>
<style lang="scss" src="@/assets/styles/system/table-style-optimized.scss"></style>
