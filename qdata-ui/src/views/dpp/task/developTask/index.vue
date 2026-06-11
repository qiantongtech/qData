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

<template>333
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
        :placeholder="'请输入数据开发类目名称'"
        ref="DeptTreeRef"
        @node-click="handleNodeClick"
        title="数据开发类目"
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
              >{{ t("common.button.add") }}
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
                  <span class="black-label mr5">任务状态:</span>
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
                  <span class="black-label mr5">调度状态:</span>
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
                  <span class="mr5">执行策略:</span>
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
                    <el-tag type="infos">未执行</el-tag>
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
                >配置任务</el-button
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
                >{{ t("common.button.details") }}</el-button
              >

              <el-popover placement="bottom" :width="150" trigger="click">
                <template #reference>
                  <el-button link type="primary" icon="ArrowDown">{{
                    t("common.button.more")
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
                    >调度周期</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="Stopwatch"
                    @click="handleDataView(row)"
                    v-if="row.processType == 1 && row.status == 1"
                    >停止任务</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="Stopwatch"
                    @click="handleDataView(row)"
                    v-if="row.processType == 1 && row.status != 1"
                    >运行实例</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    :disabled="row.status != 1"
                    @click="handleExecuteOnce(row)"
                    >执行一次</el-button
                  >
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    v-if="
                      row.datasourceType === 'FlinkStream' && row.taskInstanceId
                    "
                    @click="handleExecuteStop(row)"
                    >停止</el-button
                  >
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="row.status == 1"
                    @click="handleDelete(row)"
                    >{{ t("common.button.delete") }}</el-button
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
      title="运行实例"
    />
    <el-dialog
      title="调度周期"
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
      title="新增任务"
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
import { useI18n } from "vue-i18n";
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

const { t } = useI18n();
const { proxy } = getCurrentInstance();

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
const getExecutionType = (executionType) => {
  if (!executionType) return null;
  const item = typaOptions.find(
    (i) => String(i.value).toLowerCase() === String(executionType).toLowerCase()
  );
  if (!item) return null;
  return {
    ...item,
    elTagType: item.elTagType, // 默认 info
  };
};

const getStatus = (status) => {
  if (status == "-1") {
    return "-1";
  } else {
    return "0";
  }
};
// 任务配置
const taskConfigDialogVisible = ref(false);
const deptOptions = ref([]);
let userList = ref([]);
let taskForm = ref({});
const handleAdd = () => {
  taskConfigDialogVisible.value = true;
};
// 保存并关闭
const handleSave = (form) => {
  const parms = {
    ...form,
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    type: "3", //数据开发新增标识
  };
  createEtlTaskFront(parms).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess(t("common.message.msgOpSuccess"));
      handleQuery();
    }
  });
};
// 保存并完善
const handleConfirm = (form) => {
  const parms = {
    ...form,
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
    type: "3", //数据开发新增标识
  };
  createEtlTaskFront(parms).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess(t("common.message.msgOpSuccess"));
      handleQuery();
      routeTo("/dpp/task/developTask/edit", {
        ...res.data,
      });
    }
  });
};

const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽

let startX = 0; // 鼠标按下时的初始位置// 初始左侧宽度
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
    const delta = event.clientX - startX; // 计算鼠标移动距离
    leftWidth.value += delta; // 修改左侧宽度
    startX = event.clientX; // 更新起始位置
    // 使用 requestAnimationFrame 来减少页面重绘频率
    requestAnimationFrame(() => {});
  }
};
/** 下拉树结构 */
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
/** 运行实例按钮操作 */
function handleJobLog(data) {
  row.value = "";
  row.value = data || "";
  openCron.value = true;
  expression.value = data.cronExpression || "";
  console.log("🚀 ~ handleJobLog ~ expression.value:", expression.value);
}
function handleschedulerState(id, row, e) {
  const text = row.schedulerState == "1" ? "上线" : "下线";

  // 弹出确认框
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"数据开发调度状态吗？')
    .then(function () {
      loading.value = true;
      // 调用后台接口更新调度状态
      updateReleaseSchedule({
        id,
        schedulerState: row.schedulerState,
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
      })
        .then((response) => {
          proxy.$modal.msgSuccess(t("common.message.msgOpSuccess"));
        })
        .catch((error) => {
          // 处理失败时的恢复操作
          row.schedulerState = row.schedulerState === "1" ? "0" : "1"; // 恢复之前的状态
        })
        .finally(() => {
          loading.value = false; // 无论成功失败都停止加载
        });
    })
    .catch((error) => {
      // 失败时恢复状态
      row.schedulerState = row.schedulerState == "1" ? "0" : "1";
    });
}

/** 改变启用状态值 */
function handleStatusChange(id, row, e) {
  const text = row.status == "1" ? "上线" : "下线";

  // 弹出确认框
  proxy.$modal
    .confirm('确认要"' + text + '","' + row.name + '"数据开发任务吗？')
    .then(function () {
      loading.value = true; // 开始加载
      // 调用后台接口更新发布状态
      updateReleaseJobTask({
        id,
        releaseState: row.status,
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
      })
        .then((response) => {
          proxy.$modal.msgSuccess(t("common.message.msgOpSuccess"));
          handleQuery();
        })
        .catch((error) => {
          // 失败时恢复状态
          row.status = row.status === "1" ? "0" : "1";
        })
        .finally(() => {
          loading.value = false; // 无论成功失败都停止加载
        });
    })
    .catch((error) => {
      // 失败时恢复状态
      row.status = row.status === "1" ? "0" : "1";
    });
}
/** 确定后回传值 */
function crontabFill(value) {
  row.value.crontab = value;
  releaseTaskCrontab({
    crontab: row.value.crontab,
    projectCode: userStore.projectCode,
    projectId: userStore.projectId,
    id: row.value.id,
  }).then((response) => {
    proxy.$modal.msgSuccess(t("common.message.msgOpSuccess"));
    handleQuery();
  });
}
const handleExecuteOnce = async (row) => {
  if (!row?.id) {
    proxy.$modal.msgWarning("无效的任务id，请刷新后重试");
    return;
  }
  loading.value = true;
  try {
    const res = await startDppEtlTask(row.id);

    if (Number(res?.code) === 200) {
      proxy.$modal.msgSuccess("执行成功");
    } else {
      proxy.$modal.msgWarning(res?.msg || "执行失败，请联系管理员");
    }
  } finally {
    setTimeout(() => {
      loading.value = false;
      handleQuery();
    }, 1000);
  }
};

const handleExecuteStop = async (row) => {
  if (!row?.taskInstanceId) {
    proxy.$modal.msgWarning("当前任务无法停止，请刷新后重试");
    return;
  }
  loading.value = true;
  try {
    const res = await execute(row.taskInstanceId, "STOP");
    if (Number(res?.code) === 200) {
      proxy.$modal.msgSuccess("执行成功");
    } else {
      proxy.$modal.msgWarning(res?.msg || "执行失败，请联系管理员");
    }
  } finally {
    setTimeout(() => {
      loading.value = false;
      handleQuery();
    }, 2000);
  }
};

let DataView = ref(false);
/** 运行实例接口 */
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
    { label: t("common.texts.number"), prop: "id", width: 60, sortable: true },
    {
      label: "任务信息",
      prop: "name",
      align: "left",
      slot: "name",
      width: 300,
    },
    {
      label: "运行控制",
      prop: "status",
      width: 130,
      slot: "releaseState",
      align: "left",
    },
    {
      label: "调度周期",
      prop: "cronExpression",
      width: 200,
      slot: "cronExpression",
      align: "left",
    },
    {
      label: "最近执行",
      width: 160,
      slot: "lastExecute",
      align: "left",
    },

    {
      label: "责任人",
      width: 120,
      slot: "personChargeName",
      align: "left",
    },
    {
      label: t("common.texts.createdBy"),
      slot: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: true,
    },
    {
      label: t("common.texts.createdTime"),
      prop: "createTime",
      sortable: true,
      sortableKey: "create_time",
      date: true,
      width: 150,
      align: "left",
    },

    {
      label: t("common.texts.operation"),
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

const searchStore = reactive({
  items: [
    {
      label: "任务名称",
      prop: "name",
      align: "left",
      component: { is: "input", placeholder: "请输入任务名称" },
    },
    {
      label: "任务状态",
      prop: "status",
      component: {
        is: "select",
        placeholder: "请选择任务状态",
        options: dpp_etl_task_status,
      },
    },
    {
      label: "数据连接类型",
      prop: "datasourceType",
      component: {
        is: "select",
        placeholder: "请选择数据连接类型",
        options: typaOptions,
      },
    },
    {
      label: "处理类型",
      prop: "processType",
      component: {
        is: "select",
        placeholder: "请选择处理类型",
        options: [
          { label: "流处理", value: "1" },
          { label: "批处理", value: "2" },
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

// 监听 id 变化
watch(
  () => userStore.projectCode,
  (newId) => {
    handleQuery();
    getDeptTree();
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);

function getList() {
  tableRef.value?.getList();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    type: null,
    name: null,
    status: null,
  };
  proxy.resetForm("dppEtlTaskRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}
const DeptTreeRef = ref(null);
/** 重置按钮操作 */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = "";
  getList();
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["dppEtlTaskRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDppEtlTask(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(t("common.message.editSuccess"));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        addDppEtlTask(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess(t("common.message.addSuccess"));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除数据开发任务编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDppEtlTask(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(t("common.message.deleteSuccess"));
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
<style lang="scss" src="@/assets/system/styles/table-style-optimized.scss"></style>