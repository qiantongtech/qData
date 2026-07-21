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
            <el-button type="primary" plain @click="openTaskConfigDialog">
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
                  <span class="black-label mr5"
                    >{{
                      td("dpp.integratioTask.taskStatus", "Task Status")
                    }}:</span
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
                      td("dpp.integratioTask.scheduleStatus", "Schedule Status")
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
                  <el-icon class="mr5"><Clock /></el-icon>
                  <span
                    class="text-ellipsis cron-text"
                    :title="cronToZh(row.cronExpression)"
                  >
                    {{ cronToZh(row.cronExpression) || "-" }}
                  </span>
                </div>
                <div class="flex-center">
                  <span class="mr5"
                    >{{ td("dpp.integratioTask.executionStrategy") }}:</span
                  >
                  <dict-tag
                    :options="dpp_etl_task_execution_type"
                    :value="row.executionType"
                  />
                </div>
                <div class="flex-center mt5">
                  <span class="mr5">{{
                    td("dpp.integratioTask.schedulerEngine", "Scheduler")
                  }}:</span>
                  <span
                    class="text-ellipsis cron-text"
                    :title="`${getSchedulerLabel(row.scheduler)}`"
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
                    <el-tag type="infos" class="not-executed-tag">{{
                      td("dpp.integratioTask.notExecuted", "Not Executed")
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
                @click="routeTo('/dpp/task/integratioTask/edit', row)"
                >{{
                  td("dpp.integratioTask.configureTask", "Configure Task")
                }}</el-button
              >
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
                    >{{
                      td("dpp.integratioTask.scheduleCycle", "Schedule Cycle")
                    }}</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="Stopwatch"
                    @click="handleDataView(row)"
                    >{{
                      td("dpp.integratioTask.runInstance", "Run Instance")
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
                      td("dpp.integratioTask.executeOnce", "Execute Once")
                    }}</el-button
                  >
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="row.status == 1"
                    @click="handleDelete(row)"
                    >{{ td("common.button.delete", "Delete") }}</el-button
                  >
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
    <instance
      :visible="DataView"
      :taskType="1"
      @update:visible="DataView = $event"
      @confirm="submitForm"
      :data="form"
      :title="td('dpp.integratioTask.runInstance', 'Run Instance')"
    />
    <el-dialog
      :title="td('dpp.integratioTask.scheduleCycle', 'Schedule Cycle')"
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
    />
  </div>
</template>

<script setup name="DppIntegratioTask">
import useDefaultLang from "@/composables/useDefaultLang";
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
  copyCreateEtl,
} from "@/api/dpp/task/index.js";
import { usePageRefresh } from "@/composables/usePageRefresh";
import { cronToZh } from "@/utils/cronUtils";
import Crontab from "@/components/Crontab/index.vue";
import instance from "@/views/dpp/components/instance.vue";
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
import { ref, reactive, getCurrentInstance, watch } from "vue";

const userStore = useUserStore();
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const executeOnceLoading = ref(false);

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
  dpp_etl_task_instance,
  scheduler_type
} = proxy.useDict(
  "dpp_etl_task_status",
  "dpp_etl_task_execution_type",
  "dpp_etl_task_instance",
    "scheduler_type"
);

// Get scheduler enum values.
const getSchedulerLabel = (value) => {
  // Convert scheduler enum values into user-friendly names in the list.
  return scheduler_type.value.find((item) => item.value == value)?.label || value || "-";
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
      label: td("dpp.integratioTask.scheduleCycle", "Schedule Cycle"),
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
    var children = proxy.handleTree(response.data, "id", "parentId");
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
  return listDppEtlTask(p);
}

function handleQuery() {
  tableRef.value.getList();
}

function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = "";
  handleQuery();
}

// department tree
const leftWidth = ref(300);
const DeptTreeRef = ref(null);
function handleNodeClick(data) {
  tableStore.params.catCode = data.code;
  handleQuery();
}
// Task configuration
const taskConfigDialogVisible = ref(false);
let nodeData = ref({ taskConfig: {}, name: null });

const openTaskConfigDialog = () => {
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
        .then((response) => {
          proxy.$modal.msgSuccess(
            td("common.message.msgOpSuccess", "Operation successful")
          );
          handleQuery();
        })
        .catch((error) => {
          row.status = row.status === "1" ? "0" : "1";
        });
    })
    .catch((error) => {
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
        .then((response) => {
          proxy.$modal.msgSuccess(
            td("common.message.msgOpSuccess", "Operation successful")
          );
          handleQuery();
        })
        .catch((error) => {
          row.schedulerState = row.schedulerState == "1" ? "0" : "1";
        });
    })
    .catch((error) => {
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
  }).then((response) => {
    proxy.$modal.msgSuccess(td("common.message.msgOpSuccess", "Operation successful"));
    handleQuery();
  });
}

const DataView = ref(false);
const form = ref({});
function handleDataView(row) {
  form.value = row;
  DataView.value = true;
}

function submitForm() {
  handleQuery();
}

const handleExecuteOnce = async (row) => {
  if (executeOnceLoading.value) return;
  if (!row?.id) {
    proxy.$modal.msgWarning(
      td("dpp.integratioTask.invalidTaskId", "Invalid task ID, please refresh and retry")
    );
    return;
  }
  executeOnceLoading.value = true;
  try {
    const res = await startDppEtlTask(row.id);
    if (Number(res?.code) === 200) {
      proxy.$modal.msgSuccess(
        td("dpp.integratioTask.executeSuccess", "Executed successfully")
      );
    } else {
      proxy.$modal.msgWarning(
        res?.msg ||
          td("dpp.integratioTask.executeFailed", "Execution failed, please contact administrator")
      );
    }
  } catch (e) {
    //
  } finally {
    executeOnceLoading.value = false;
  }
};

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
      return delDppEtlTask(ids);
    })
    .then(() => {
      handleQuery();
      proxy.$modal.msgSuccess(td("common.message.deleteSuccess", "Deleted successfully"));
    })
    .catch(() => {});
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

// Initialization
watch(
  () => userStore.projectId,
  () => {
    handleQuery();
    getDeptTree();
  }
);

if (userStore.projectId) {
  getDeptTree();
}
usePageRefresh("integratioTask", () => handleQuery());
</script>

<style lang="scss" src="@/assets/styles/system/table-style-optimized.scss"></style>
