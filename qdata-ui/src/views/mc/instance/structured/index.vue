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
  <div class="app-container" v-loading="store.loading">
    <el-container>
      <!-- <SourceSystemTree
        ref="sourceSystemTreeRef"
        @node-click="handleNodeClick"
        @data-loaded="handleTreeDataLoaded"
      /> -->
      <el-main class="main-content">
        <qt-wrap :columns="tableStroe.columns" :tableRef="tableRef">
          <template #search>
            <qt-search-bar
              v-bind="searchStore"
              :params="tableStroe.params"
              @query="handleQueryClick"
              @reset="handleResetQueryClick"
            />
          </template>
          <template #actions-data>
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
          <qt-table v-bind="tableStroe" ref="tableRef">
            <template #task-status="scope">
              <el-switch
                v-model="scope.row.taskStatus"
                active-value="1"
                inactive-value="0"
              />
            </template>

            <template #date-range="{ row }">
              {{ row.startTime }} ~ {{ row.endTime }}
            </template>

            <template #handle="{ row }">
              <el-button
                link
                type="primary"
                icon="View"
                @click="handleViewClick(row)"
              >
                {{ td("mc.task.structured.viewLog") }}
              </el-button>
              <el-button
                link
                type="warning"
                icon="Download"
                @click="handleDownloadClick(row)"
              >
                {{ td("mc.task.structured.downloadLog") }}
              </el-button>
            </template>
          </qt-table>
        </qt-wrap>
      </el-main>
    </el-container>

    <LogDialog v-model="dialog.open" v-bind="dialog" />
  </div>
</template>

<script setup name="InstanceStructured">
import { useI18n } from "vue-i18n";
import { reactive, computed, getCurrentInstance, ref } from "vue";
import { listTaskInstance, delTaskInstance } from "@/api/mc/task/taskInstance";
import { getParentLabelPath } from "@/utils/anivia";
import { getTaskInstanceLog } from "@/api/mc/task/taskInstanceLog";
import LogDialog from "@/components/LogDialog/index.vue";
import SourceSystemTree from "@/views/mc/task/structured/components/SourceSystemTree.vue";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { t } = useI18n();
const { proxy } = getCurrentInstance();

const sourceSystemTreeRef = ref();
const store = reactive({
  treeDomains: [],
  loading: false,
  rows: [],
});

const tableRef = ref(null);
const tableStroe = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
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
      width: 60,
    },
    {
      label: td("mc.task.structured.taskName"),
      prop: "name",
      minWidth: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.sourceSystem"),
      prop: "sourceSystemName",
      minWidth: 240,
      align: "left",
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("mc.task.structured.collectTableCount"),
      prop: "totalCount",
      width: 140,
    },
    {
      label: td("mc.task.structured.collectStatus"),
      prop: "status",
      width: 140,
      dict: "mc_task_instance_status",
    },
    {
      label: td("mc.task.structured.collectDuration"),
      prop: "duration",
      width: 120,
    },
    {
      label: td("mc.task.structured.collectTimeRange"),
      slot: "date-range",
      width: 340,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: td("common.texts.createdBy"),
      prop: "createBy",
      width: 120,
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
      slot: "handle",
      width: 220,
      fixed: "right",
    },
  ],
  func: listTaskInstance,
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

const dialog = reactive({
  open: false,
  content: "",
});

// 获取来源系统路径
const getDomainPath = computed(() => {
  return function (id) {
    let domainName = getParentLabelPath(store.treeDomains, id, {
      idKey: "id",
      labelKey: "name",
      childrenKey: "children",
    });
    const idx = domainName.indexOf("/");
    return idx == -1 ? domainName : domainName.slice(idx + 1);
  };
});

function handleTreeDataLoaded({ treeData, flatData }) {
  store.treeDomains = treeData;
}

// 节点单击事件
function handleNodeClick(data) {
  // 清除之前的筛选
  tableStroe.params.sourceSystemId = undefined;
  tableStroe.params.datasourceId = undefined;
  tableStroe.params.taskId = undefined;

  if (data.type === "SOURCE") {
    tableStroe.params.sourceSystemId = data.id;
  } else if (data.type === "DATASOURCE") {
    tableStroe.params.datasourceId = data.id;
  } else if (data.type === "DATABASE") {
    tableStroe.params.taskId = data.taskId;
  }
  tableRef.value.getList();
}

// 搜索按钮操作
function handleQueryClick() {
  tableRef.value?.getList();
}

// 重置按钮操作
function handleResetQueryClick() {
  if (sourceSystemTreeRef.value?.resetTree) {
    sourceSystemTreeRef.value.resetTree();
  }
  tableStroe.params.sourceSystemId = null;
  tableStroe.params.datasourceId = null;
  tableStroe.params.taskId = null;
  tableRef.value?.resetQuery();
}

function handleViewClick(row) {
  getTaskInstanceLog(row.id).then((res) => {
    dialog.content = res.data?.logContent || td("common.noLog");
    dialog.open = true;
  });
}

// 下载日志
function handleDownloadClick(row) {
  getTaskInstanceLog(row.id).then((res) => {
    const content = res.data?.logContent || td("common.noLog");
    const taskName = row.name || 'task';
    const instanceId = String(row.id).replace(/[^\w\-]/g, '_');
    const fileName = td('mc.instance.structured.logFileName', "{name}_{id}_日志.log", {name: taskName, id: instanceId});
    proxy.downloadContent(content, fileName);
  });
}

// 删除选中行
function handleDeleteColumnClick() {
  if (!store.rows.length) return;
  ElMessageBox.confirm(
    td("mc.task.structured.confirmDeleteSelected", {
      count: store.rows.length,
      notDeleteCount: 0,
    }),
    td("common.message.systemPrompt"),
    {
      confirmButtonText: td("common.button.confirm"),
      cancelButtonText: td("common.button.cancel"),
      type: "warning",
    }
  )
    .then(() => {
      const ids = store.rows.map((item) => item.id);
      return delTaskInstance(ids);
    })
    .then(() => {
      ElMessage.success(t("common.message.deleteSuccess"));
      tableRef.value.getList();
    });
}
</script>
