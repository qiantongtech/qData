<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
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
          td('dpp.integratioTask.inputCategoryName', '请输入数据集成类目名称')
        "
        ref="DeptTreeRef"
        :title="
          td('dpp.integratioTask.dataIntegrationCategory', '数据集成类目')
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
              >{{ td("common.button.add", "新增") }}
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
                      td("dpp.integratioTask.taskStatus", "任务状态")
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
                      td("dpp.integratioTask.scheduleStatus", "调度状态")
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
                      td("dpp.integratioTask.notExecuted", "未执行")
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
                  td("dpp.integratioTask.configureTask", "配置任务")
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
                >{{ td("common.button.details", "详情") }}</el-button
              >
              <el-popover placement="bottom" :width="150" trigger="click">
                <template #reference>
                  <el-button link type="primary" icon="ArrowDown">{{
                    td("common.button.more", "更多")
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
                      td("dpp.integratioTask.scheduleCycle", "调度周期")
                    }}</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="Stopwatch"
                    @click="handleDataView(row)"
                    >{{
                      td("dpp.integratioTask.runInstance", "运行实例")
                    }}</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    :disabled="row.status != 1"
                    @click="handleExecuteOnce(row)"
                    >{{
                      td("dpp.integratioTask.executeOnce", "执行一次")
                    }}</el-button
                  >
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="row.status == 1"
                    @click="handleDelete(row)"
                    >{{ td("common.button.delete", "删除") }}</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="CopyDocument"
                    :disabled="row.status == 1"
                    @click="handleClone(row)"
                  >
                    {{ td("dpp.cleanRule.clone", "克隆") }}
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
      :title="td('dpp.integratioTask.runInstance', '运行实例')"
    />
    <el-dialog
      :title="td('dpp.integratioTask.scheduleCycle', '调度周期')"
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
    <!-- 新增 -->
    <add
      :visible="taskConfigDialogVisible"
      :title="td('dpp.cleanRule.addTask', '新增任务')"
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
const userStore = useUserStore();
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

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

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
} = proxy.useDict(
  "dpp_etl_task_status",
  "dpp_etl_task_execution_type",
  "dpp_etl_task_instance"
);

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
      label: td("common.texts.number", "编号"),
      prop: "id",
      width: 60,
      sortable: true,
    },
    {
      label: td("dpp.integratioTask.taskInfo", "任务信息"),
      prop: "name",
      align: "left",
      slot: "name",
      width: 300,
    },
    {
      label: td("dpp.integratioTask.runControl", "运行控制"),
      prop: "status",
      width: 190,
      slot: "releaseState",
      align: "left",
    },
    {
      label: td("dpp.integratioTask.scheduleCycle", "调度周期"),
      prop: "cronExpression",
      width: 220,
      slot: "cronExpression",
      align: "left",
    },
    {
      label: td("dpp.integratioTask.recentExecution", "最近执行"),
      width: 160,
      slot: "lastExecute",
      align: "left",
    },

    {
      label: td("dpp.integratioTask.personCharge", "责任人"),
      width: 120,
      slot: "personChargeName",
      align: "left",
    },
    {
      label: td("common.texts.createdBy", "创建人"),
      slot: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: true,
    },
    {
      label: td("common.texts.createdTime", "创建时间"),
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      date: true,
      width: 150,
      align: "left",
    },

    {
      label: td("common.texts.operation", "操作"),
      align: "center",
      fixed: "right",
      slot: "action",
      width: 240,
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
        name: td("dpp.integratioTask.dataIntegrationCategory", "数据集成类目"),
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
      label: td("dpp.integratioTask.taskName", "任务名称"),
      prop: "name",
      align: "left",
      component: {
        is: "input",
        placeholder: td("dpp.integratioTask.inputTaskName", "请输入任务名称"),
      },
    },
    {
      label: td("dpp.integratioTask.taskStatus", "任务状态"),
      prop: "status",
      component: {
        is: "select",
        placeholder: td(
          "dpp.integratioTask.selectTaskStatus",
          "请选择任务状态"
        ),
        options: dpp_etl_task_status,
      },
    },
    {
      label: td("dpp.integratioTask.personCharge", "责任人"),
      prop: "personCharge",
      component: {
        is: "tree-select",
        data: userList,
        props: { value: "userId", label: "nickName", children: "children" },
        valueKey: "ID",
        placeholder: td(
          "dpp.integratioTask.selectPersonCharge",
          "请选择责任人"
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

// 部门树
const leftWidth = ref(300);
const DeptTreeRef = ref(null);
function handleNodeClick(data) {
  tableStore.params.catCode = data.code;
  handleQuery();
}
// 任务配置
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
      proxy.$modal.msgSuccess(td("common.message.msgOpSuccess", "操作成功"));
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
      proxy.$modal.msgSuccess(td("common.message.msgOpSuccess", "操作成功"));
      handleQuery();
      routeTo("/dpp/task/integratioTask/edit", res.data);
    }
  });
};

// Actions
function handleStatusChange(id, row) {
  const text =
    row.status == "1"
      ? td("dpp.integratioTask.online", "上线")
      : td("dpp.integratioTask.offline", "下线");
  proxy.$modal
    .confirm(
      td(
        "dpp.integratioTask.confirmTaskStatus",
        '确认要"' + text + '","' + row.name + '"数据集成任务吗？',
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
            td("common.message.msgOpSuccess", "操作成功")
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
      ? td("dpp.integratioTask.online", "上线")
      : td("dpp.integratioTask.offline", "下线");
  proxy.$modal
    .confirm(
      td(
        "dpp.integratioTask.confirmScheduleStatus",
        '确认要"' + text + '","' + row.name + '"数据集成调度状态吗？',
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
            td("common.message.msgOpSuccess", "操作成功")
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
    proxy.$modal.msgSuccess(td("common.message.msgOpSuccess", "操作成功"));
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
  if (!row?.id) {
    proxy.$modal.msgWarning(
      td("dpp.integratioTask.invalidTaskId", "无效的任务id，请刷新后重试")
    );
    return;
  }
  try {
    const res = await startDppEtlTask(row.id);
    if (Number(res?.code) === 200) {
      proxy.$modal.msgSuccess(
        td("dpp.integratioTask.executeSuccess", "执行成功")
      );
    } else {
      proxy.$modal.msgWarning(
        res?.msg ||
          td("dpp.integratioTask.executeFailed", "执行失败，请联系管理员")
      );
    }
  } catch (e) {
    //
  }
};

const handleClone = (row) => {
  proxy.$modal
    .confirm(
      td(
        "dpp.integratioTask.confirmClone",
        `确定要克隆任务【${row.name}】吗？`,
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
        '是否确认删除数据集成任务编号为"' + ids + '"的数据项？',
        { ids }
      )
    )
    .then(function () {
      return delDppEtlTask(ids);
    })
    .then(() => {
      handleQuery();
      proxy.$modal.msgSuccess(td("common.message.deleteSuccess", "删除成功"));
    })
    .catch(() => {});
}

// Utils
const getDatasourceIcon = (json) => {
  let type = json && JSON.parse(json).taskType;
  switch (type) {
    case "FLINK":
      return new URL("@/assets/images/common/flink.svg", import.meta.url).href;
    case "SPARK":
      return new URL("@/assets/images/common/spark.svg", import.meta.url).href;
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
