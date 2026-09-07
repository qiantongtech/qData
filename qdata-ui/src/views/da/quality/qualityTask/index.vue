<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co.,江苏 Qiantong Technology Co., Ltd.
  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <div class="app-container" ref="app-container">
    <GuideTip tip-id="da/dataQuality/dataQualityTasks.list" />

    <el-container style="height: 100%">
      <DeptTree
        :deptOptions="deptOptions"
        :leftWidth="leftWidth"
        :placeholder="td('da.qualityTask.deptTreePlaceholder')"
        ref="DeptTreeRef"
        @node-click="handleNodeClick"
      />
      <el-main class="main-content">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
          <!-- 搜索栏插槽 -->
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStore.params"
              @query="handleQuery"
              @reset="resetQuery"
              :tableRef="tableRef"
            />
          </template>

          <!-- 数据操作按钮插槽 -->
          <template #actions-data>
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  plain
                  @click="routeTo('/da/quality/qualityTask/add', { row: null })"
                  v-hasPermi="['da:qualityTask:add']"
                >
                  <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
                </el-button>
              </el-col>
            </el-row>
          </template>

          <!-- 表格组件 -->
          <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
            <template #cycle="{ row }">
              {{ cronToZh(row.cycle) || "-" }}
            </template>

            <template #statusHeader>
              <div class="justify-center" style="display: flex; align-items: center; justify-content: center;">
                <span>{{ td('da.qualityTask.columnVisibility.status') }}</span>
                <el-tooltip effect="light" :content="td('da.qualityTask.statusTooltip')" placement="top">
                  <el-icon class="tip-icon" style="margin-left: 4px;">
                    <InfoFilled />
                  </el-icon>
                </el-tooltip>
              </div>
            </template>

            <template #status="{ row }">
              <el-switch
                v-model="row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(row)"
              />
            </template>

            <!-- 操作列插槽 -->
            <template #action="{ row }">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="routeTo('/da/quality/qualityTask/edit', { ...row })"
                v-hasPermi="['da:qualityTask:edit']"
                :disabled="row.status != 1"
              >
                {{ td('da.qualityTask.config') }}
              </el-button>
              <el-button
                link
                type="primary"
                icon="view"
                @click="routeTo('/da/quality/qualityTask/detail', { ...row, info: true })"
                v-hasPermi="['da:qualityTask:info']"
              >
                {{ td('common.button.details') }}
              </el-button>

              <el-popover placement="bottom" :width="150" trigger="click">
                <template #reference>
                  <el-button link type="primary" icon="ArrowDown">{{ td('common.button.more') }}</el-button>
                </template>
                <div style="width: 100px" class="butgdlist">
                  <el-button
                    link
                    type="primary"
                    icon="VideoPlay"
                    style="padding-left: 14px"
                    :loading="executeOnceLoading"
                    @click="handleExecuteOnce(row)"
                    v-hasPermi="['da:qualityTask:once']"
                    :disabled="row.status == 1"
                  >
                    {{ td('da.qualityTask.executeOnce') }}
                  </el-button>
                  <el-button
                    link
                    type="primary"
                    icon="Stopwatch"
                    @click="handleDataView(row)"
                    v-hasPermi="['da:qualityTask:edit']"
                  >
                    {{ td('da.qualityTask.executionLog') }}
                  </el-button>
                  <el-button
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="row.status != 1"
                    @click="handleDelete(row)"
                    v-hasPermi="['da:qualityTask:remove']"
                  >
                    {{ td('common.button.delete') }}
                  </el-button>
                  <el-button
                    link
                    icon="Operation"
                    @click="handleJobLog(row)"
                    type="primary"
                    :disabled="row.status != 1"
                    v-hasPermi="['da:qualityTask:schedule']"
                  >
                    {{ td('da.qualityTask.schedulePeriodLabel') }}
                  </el-button>
                </div>
              </el-popover>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <DataViewDialog
      :visible="DataView"
      :taskType="3"
      @update:visible="DataView = $event"
      :data="form"
      :title="td('da.qualityTask.executionLog')"
    />
    <el-dialog
      :title="td('da.qualityTask.cronTitle')"
      v-model="openCron"
      :append-to="$refs['app-container']"
      destroy-on-close
      :appendTo="'#app'"
    >
      <crontab ref="crontabRef" @hide="openCron = false" @fill="crontabFill" :expression="expression" />
    </el-dialog>
  </div>
</template>

<script setup name="QualityTask">
import {
  createEtlTaskFront
} from "@/api/dpp/task/index.js";
import { cronToZh } from "@/utils/cronUtils";
import Crontab from "@/components/Crontab/index.vue";
import DataViewDialog from "./components/instance.vue";
import { useRoute, useRouter } from "vue-router";
import useUserStore from "@/store/system/user";
import DeptTree from "@/components/DeptTree";
import { ref, reactive, toRefs, getCurrentInstance, onActivated } from "vue";
import { listAttQualityCat } from "@/api/att/cat/qualityCat/qualityCat.js";
import useDefaultLang from "@/composables/useDefaultLang";
import {
  listDppQualityTask,
  delDppQualityTask,
  updateDppQualityTaskStatus,
  startDppQualityTask,
  updateDaDiscoveryTaskCronExpression
} from "@/api/da/quality/qualityTask";

const userStore = useUserStore();
const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const executeOnceLoading = ref(false);
const { da_discovery_task_status, dpp_etl_task_execution_type } =
  proxy.useDict(
    "da_discovery_task_status",
    "dpp_etl_task_execution_type"
  );

const tableRef = ref(null);
const DeptTreeRef = ref(null);

const searchStore = reactive({
  items: [
    {
      label: td("da.qualityTask.taskName"),
      prop: "taskName",
      component: {
        is: "input",
        placeholder: td("da.qualityTask.taskNamePlaceholder"),
      },
    },
    {
      label: td("da.qualityTask.taskStatus"),
      prop: "status",
      component: {
        is: "select",
        options: da_discovery_task_status,
        placeholder: td("da.qualityTask.taskStatusPlaceholder"),
      },
    },
  ],
});

const tableStore = reactive({
  config: {
    initResquest: true,
  },
  columns: [
    {
      label: td("da.qualityTask.columnVisibility.id"),
      prop: "id",
      width: 60,
      align: "left",
      sortable: true,
    },
    {
      label: td("da.qualityTask.columnVisibility.taskName"),
      prop: "taskName",
      width: 260,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td("da.qualityTask.columnVisibility.belongCat"),
      prop: "catName",
      width: 160,
      align: "left",
      tag: { class: "task-cat-ellipsis" },
    },
    {
      label: td("da.qualityTask.columnVisibility.description"),
      prop: "description",
      width: 256,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td("da.qualityTask.columnVisibility.inspectionTargetCount"),
      prop: "taskObjNum",
      width: 100,
      align: "left",
    },
    {
      label: td("da.qualityTask.columnVisibility.inspectionRuleCount"),
      prop: "taskEvaluateNum",
      width: 160,
      align: "left",
    },
    {
      label: td("da.qualityTask.columnVisibility.executionStrategy"),
      prop: "strategy",
      width: 160,
      align: "left",
      dict: "dpp_etl_task_execution_type",
    },
    {
      label: td("da.qualityTask.columnVisibility.schedulePeriod"),
      prop: "cycle",
      width: 240,
      align: "left",
      slot: "cycle",
    },
    {
      label: td("da.qualityTask.columnVisibility.lastExecutionTime"),
      prop: "lastExecuteTime",
      width: 160,
      align: "left",
      sortable: true,
      date: true,
    },
    {
      label: td("da.qualityTask.columnVisibility.createdBy"),
      prop: "createBy",
      width: 120,
      align: "left",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: td("da.qualityTask.columnVisibility.createdTime"),
      prop: "createTime",
      width: 160,
      align: "left",
      sortable: true,
      date: true,
    },
    {
      label: td("da.qualityTask.columnVisibility.status"),
      prop: "status",
      width: 150,
      align: "center",
      slot: "status",
      headerSlot: "statusHeader",
    },
    {
      label: td("common.texts.operation"),
      slot: "action",
      width: 240,
      align: "center",
      fixed: "right",
    },
  ],
  func: (params) => {
    params.projectCode = userStore.projectCode;
    params.projectId = userStore.projectId;
    return listDppQualityTask(params);
  },
  params: {
    pageNum: 1,
    pageSize: 10,
    taskName: null,
    status: null,
    catCode: null,
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const deptOptions = ref([]);
const leftWidth = ref(300); // Initial left width
/** Dropdown tree structure */
function getDeptTree() {
  listAttQualityCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: td('da.qualityTask.catRootName'),
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}
function handleNodeClick(data) {
  tableStore.params.catCode = data.code;
  tableStore.params.pageNum = 1;
}
const route = useRoute();
let openCron = ref(false);
let row = ref();
let expression = ref("");
/** Run instance button operation */
function handleJobLog(data) {
  row.value = "";
  row.value = data || "";
  openCron.value = true;
  expression.value = data.cycle || "";
}
/** Toggle enable status value */
function handleStatusChange(row, e) {
  const text = row?.status == "1" ? td('da.qualityTask.offline') : td('da.qualityTask.online');
  proxy.$modal
    .confirm(td('da.qualityTask.confirmStatusChange', '', { text, name: row.taskName }))
    .then(function () {
      updateDppQualityTaskStatus({
        id: row.id,
        status: Number(row.status)
      })
        .then((response) => {
          proxy.$modal.msgSuccess(td('common.message.msgOpSuccess'));
          
          tableRef.value.getList();
        })
        .catch((error) => {
          row.status = row.status === "1" ? "0" : "1";
        });
    })
    .catch((error) => {
      row.status = row.status === "1" ? "0" : "1";
    });
}
/** Return value after confirmation */
function crontabFill(value) {
  row.value.crontab = value;
  updateDaDiscoveryTaskCronExpression({
    cycle: row.value.cycle,
    status: '1',
    id: Number(row.value.id),
  }).then((response) => {
    proxy.$modal.msgSuccess(td('common.message.msgOpSuccess'));
    tableRef.value.getList();
  });
}
const handleExecuteOnce = async (row) => {
  if (executeOnceLoading.value) return;
  if (!row?.id) {
    proxy.$modal.msgWarning(td('da.qualityTask.invalidTaskId'));
    return;
  }
  executeOnceLoading.value = true;
  try {
    const res = await startDppQualityTask(row.id);

    if (Number(res?.code) === 200) {
      proxy.$modal.msgSuccess(td('da.qualityTask.executeSuccess'));
      tableRef.value.getList();
    } else {
      proxy.$modal.msgWarning(res?.msg || td('da.qualityTask.executeFailed'));
    }
  } finally {
    executeOnceLoading.value = false;
  }
};
let DataView = ref(false);
/** Run instance API */
function handleDataView(row) {
  form.value = row;
  DataView.value = true;
}

const open = ref(false);
const router = useRouter();
const data = reactive({
  form: {

  },
  rules: {},
});

const { form, rules } = toRefs(data);

/** Search button operation */
function handleQuery() {
  tableStore.params.pageNum = 1;
}

/** Reset button operation */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  tableStore.params.catCode = "";
  tableStore.params.pageNum = 1;
  handleQuery();
}
/** Delete button operation */
function handleDelete(row) {
  const _ids = row.id;
  proxy.$modal
    .confirm(td('da.qualityTask.confirmDelete', '', { id: _ids }))
    .then(function () {
      return delDppQualityTask(_ids);
    })
    .then(() => {
      tableRef.value.getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => { });
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
          id: row?.id,
          info: row?.info,
        },
      });
    }
  }
}

onActivated(() => {
  tableRef.value?.getList();
});
getDeptTree();
</script>

<style scoped lang="scss">
::v-deep {
  .selectlist .el-tag.el-tag--info {
    background: #f3f8ff !important;
    border: 0px solid #6ba7ff !important;
    color: #2666fb !important;
  }
}

.app-container {
  margin: 13px 15px;
}

.el-main {
  padding: 2px 0px;
}
</style>
